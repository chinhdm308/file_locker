package com.base.presentation.service

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import com.base.presentation.utils.AppConstants
import com.base.presentation.utils.FileHideUtils
import com.base.presentation.utils.SdCardUtil
import com.base.presentation.utils.helper.file.FileManager
import com.base.domain.models.audio.AudioModel
import com.base.domain.models.audio.HideAudio
import com.base.domain.usecases.hideaudio.DeleteHideAudioUseCase
import com.base.domain.usecases.hideaudio.GetAllHideAudioUseCase
import com.base.domain.usecases.hideaudio.GetHideAudiosByBeyondGroupIdUseCase
import com.base.domain.usecases.hideaudio.InsertHideAudioUseCase
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.runBlocking
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.Date
import javax.inject.Inject


class AudioService @Inject constructor(
    @ApplicationContext val context: Context,
    private val fileManager: FileManager,
    private val insertHideAudioUseCase: InsertHideAudioUseCase,
    private val deleteHideAudioUseCase: DeleteHideAudioUseCase,
    private val getHideAudiosByBeyondGroupIdUseCase: GetHideAudiosByBeyondGroupIdUseCase,
    private val getAllHideAudioUseCase: GetAllHideAudioUseCase,
) : AbstructProvider {

    override fun getList(): List<Any> {
        val list = mutableListOf<AudioModel>()
        var needCheck = SdCardUtil.needCheckExtSdCard()
        val mPath = SdCardUtil.getExtSdCardPath()
        if (mPath == null) {
            needCheck = false
        }
        val selection =
            StringBuilder("${MediaStore.Audio.Media.IS_MUSIC} != 0 AND ${MediaStore.Audio.Media.TITLE} != ''")
        // Display audios in alphabetical order based on their display name.
        val sortOrder = "${MediaStore.Audio.Media.DISPLAY_NAME} ASC"
        val cursor: Cursor? = context.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            null,
            selection.toString(),
            null,
            sortOrder
        )
//        val cursor: Cursor? = context.contentResolver.query(
//            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null,
//            null, null
//        )
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID))
                val title =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE))
                val album =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM))
                val artist =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST))
                val path =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA))
                val displayName =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME))
                val mimeType =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.MIME_TYPE))
                val duration =
                    cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION))
                        .toLong()
                val size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE))
                if (fileManager.isHideFile(displayName)) continue

                // 判断是否需要添加
                if (needCheck && (mPath != null && path.contains(mPath))) {
                    continue
                }
                val audio = AudioModel(
                    id = id.toLong(),
                    title = title,
                    album = album,
                    artist = artist,
                    path = path,
                    displayName = displayName,
                    mimeType = mimeType,
                    duration = duration,
                    size = size
                )
                list.add(audio)
            }
            cursor.close()
        }

        return list
    }

    // 隐藏音频文件
    suspend fun hideAudio(audioModel: AudioModel, beyondGroupId: Int): Boolean {
        val fromFile = File(audioModel.path)
        if (!fromFile.exists()) {
            return false
        }
        val toPathString: String = AppConstants.getHidePath(audioModel.path)
        if (toPathString.isEmpty()) {
            return false
        }
        val toFile = File(toPathString + audioModel.displayName + AppConstants.getSuffix())
        // 复制
        if (fromFile.renameTo(toFile)) {
            // 插入数据库
            val id: Long = insertHideAudioUseCase.execute(
                HideAudio(
                    id = null,
                    beyondGroupId = beyondGroupId,
                    title = audioModel.title,
                    album = audioModel.album,
                    artist = audioModel.artist,
                    oldPathUrl = audioModel.path,
                    displayName = audioModel.displayName,
                    mimeType = audioModel.mimeType,
                    duration = audioModel.duration.toString(),
                    newPathUrl = toFile.path,
                    size = audioModel.size,
                    moveDate = Date().time
                )
            )
            if (id >= 0) {
                return delSysMedia(audioModel)
            }
        } else {
            fromFile.copyTo(toFile)
            fromFile.delete()
            return toFile.exists()
        }
        return false
    }

    private fun fileMoveToAnotherFolder(sourceFolder: File, destinationFolder: File): Boolean {
        var ismove = false
        var inStream: InputStream? = null
        var outStream: OutputStream? = null
        try {
            inStream = FileInputStream(sourceFolder)
            outStream = FileOutputStream(destinationFolder)
            val buffer = ByteArray(1024 * 4)
            var length: Int
            // copy the file content in bytes
            while (inStream.read(buffer).also { length = it } > 0) {
                outStream.write(buffer, 0, length)
            }
            // delete the original file
            sourceFolder.delete()
            ismove = true
            println("File is copied successful!")
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            inStream!!.close()
            outStream!!.close()
        }
        return ismove
    }

    // 取消隐藏音频文件
    suspend fun unHideAudio(hideAudio: HideAudio?): Boolean {
        if (hideAudio != null) {
            val fromFile = File(hideAudio.newPathUrl)
            val toFile = File(hideAudio.oldPathUrl)

            // 插入数据库
            deleteHideAudioUseCase.execute(hideAudio)
            insSysMedia(hideAudio)
            // 复制
            if (fromFile.renameTo(toFile)) {
                return true
            }
        }
        return false
    }

    /**
     * 删除系统媒体记录及其对应的缩略图记录
     */
    private fun delSysMedia(mi: AudioModel): Boolean {
        val cr = context.contentResolver
        return cr.delete(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            MediaStore.Audio.Media._ID + "=?",
            arrayOf(mi.id.toString())
        ) > 0
    }

    /**
     * 插入系统媒体记录
     */
    private fun insSysMedia(mi: HideAudio) {
        val cr = context.contentResolver
        val oriFile = File(mi.oldPathUrl)
        val values = ContentValues()
        values.put(
            MediaStore.Audio.Media.TITLE,
            mi.displayName.substring(
                0,
                mi.displayName.lastIndexOf(".")
            )
        )
        values.put(MediaStore.Audio.Media.DISPLAY_NAME, mi.displayName)
        values.put(MediaStore.Audio.Media.DATA, mi.oldPathUrl)
        values.put(MediaStore.Audio.Media.DATE_MODIFIED, oriFile.lastModified())
        values.put(MediaStore.Audio.Media.SIZE, oriFile.length())
        values.put(MediaStore.Audio.Media.MIME_TYPE, mi.mimeType)
        val contentUri = cr.insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, values)
        if (contentUri != null) {
            mi.id = ContentUris.parseId(contentUri)
        }
    }

    // 获取所有已加密的音频文件列表
    suspend fun getHideAudios(beyondGroupId: Int): List<HideAudio> {
        val audios = getHideAudiosByBeyondGroupIdUseCase.execute(beyondGroupId)
        val list: List<HideAudio> = FileHideUtils.checkHideAudio(audios.toMutableList())
        if (list.isNotEmpty()) {
            for (hideFile in list) {
                deleteAudioByPath(hideFile)
            }
        }
        return audios
    }


    // 获取已隐藏图片的数量不分组别
    fun getHideAudioCount(): Int = runBlocking {
        val hideAudioList: List<HideAudio> = getAllHideAudioUseCase.execute()
        return@runBlocking hideAudioList.size
    }

    // 删除指定音频文件
    suspend fun deleteAudioByPath(hideAudio: HideAudio): Boolean {
        if (hideAudio.newPathUrl.isEmpty()) {
            return false
        }
        val audioFile = File(hideAudio.newPathUrl)
        deleteHideAudioUseCase.execute(hideAudio)
        if (audioFile.delete()) {
            delSysMedia(
                AudioModel(
                    id = hideAudio.id!!,
                    title = hideAudio.title,
                    album = hideAudio.album,
                    artist = hideAudio.artist,
                    path = hideAudio.newPathUrl,
                    displayName = hideAudio.displayName,
                    mimeType = hideAudio.mimeType,
                    duration = hideAudio.duration.toLong(),
                    size = hideAudio.size
                )
            )
            return true
        }
        return false
    }

}