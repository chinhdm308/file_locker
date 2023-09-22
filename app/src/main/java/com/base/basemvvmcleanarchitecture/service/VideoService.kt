package com.base.basemvvmcleanarchitecture.service

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import com.base.basemvvmcleanarchitecture.utils.AppConstants
import com.base.basemvvmcleanarchitecture.utils.FileHideUtils
import com.base.basemvvmcleanarchitecture.utils.SdCardUtil
import com.base.basemvvmcleanarchitecture.utils.helper.file.FileManager
import com.base.domain.models.video.HideVideo
import com.base.domain.models.video.VideoModel
import com.base.domain.usecases.hidevideo.DeleteHideVideoUseCase
import com.base.domain.usecases.hidevideo.GetAllHideVideoUseCase
import com.base.domain.usecases.hidevideo.GetHideVideosByBeyondGroupIdUseCase
import com.base.domain.usecases.hidevideo.InsertHideVideoUseCase
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


class VideoService @Inject constructor(
    @ApplicationContext val context: Context,
    private val fileManager: FileManager,
    private val insertHideVideoUseCase: InsertHideVideoUseCase,
    private val deleteHideVideoUseCase: DeleteHideVideoUseCase,
    private val getHideVideosByBeyondGroupIdUseCase: GetHideVideosByBeyondGroupIdUseCase,
    private val getAllHideVideoUseCase: GetAllHideVideoUseCase,
) : AbstructProvider {

    override fun getList(): List<Any> {
        val list = mutableListOf<VideoModel>()
        var needCheck = SdCardUtil.needCheckExtSdCard()
        val mPath = SdCardUtil.getExtSdCardPath()
        if (mPath == null) {
            needCheck = false
        }
        val selection =
            StringBuilder("${MediaStore.Video.Media.TITLE} != ''")
        // Display Videos in alphabetical order based on their display name.
        val sortOrder = "${MediaStore.Video.Media.DISPLAY_NAME} ASC"
        val cursor: Cursor? = context.contentResolver.query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            null,
            selection.toString(),
            null,
            sortOrder
        )
//        val cursor: Cursor? = context.contentResolver.query(
//            MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null,
//            null, null
//        )
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID))
                val title =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE))
                val album =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.ALBUM))
                val artist =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.ARTIST))
                val path =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA))
                val displayName =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME))
                val mimeType =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE))
                val duration =
                    cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION))
                        .toLong()
                val size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE))
                if (fileManager.isHideFile(displayName)) continue

                // 判断是否需要添加
                if (needCheck && (mPath != null && path.contains(mPath))) {
                    continue
                }
                val video = VideoModel(
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
                list.add(video)
            }
            cursor.close()
        }

        return list
    }

    // 隐藏视频文件
    suspend fun hideVideo(videoModel: VideoModel, beyondGroupId: Int): Boolean {
        val fromFile = File(videoModel.path)
        if (!fromFile.exists()) {
            return false
        }
        val toPathString: String = AppConstants.getHidePath(videoModel.path)
        if (toPathString.isEmpty()) {
            return false
        }
        val toFile = File(toPathString + videoModel.displayName + AppConstants.getSuffix())
        // 复制
        if (fromFile.renameTo(toFile)) {
            // 插入数据库
            val id: Long = insertHideVideoUseCase.execute(
                HideVideo(
                    id = null,
                    beyondGroupId = beyondGroupId,
                    title = videoModel.title,
                    album = videoModel.album,
                    artist = videoModel.artist,
                    oldPathUrl = videoModel.path,
                    displayName = videoModel.displayName,
                    mimeType = videoModel.mimeType,
                    duration = videoModel.duration.toString(),
                    newPathUrl = toFile.path,
                    size = videoModel.size,
                    moveDate = Date().time
                )
            )
            if (id >= 0) {
                return delSysMedia(videoModel)
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

    // 取消隐藏的视频文件
    suspend fun unHideVideo(hideVideo: HideVideo?): Boolean {
        if (hideVideo != null) {
            val fromFile = File(hideVideo.newPathUrl)
            val toFile = File(hideVideo.oldPathUrl)

            // 插入数据库
            deleteHideVideoUseCase.execute(hideVideo)
            insSysMedia(hideVideo)
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
    private fun delSysMedia(mi: VideoModel): Boolean {
        val cr = context.contentResolver
        return cr.delete(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            MediaStore.Video.Media._ID + "=?",
            arrayOf(mi.id.toString())
        ) > 0
    }

    /**
     * 插入系统媒体记录
     */
    private fun insSysMedia(mi: HideVideo) {
        val cr = context.contentResolver
        val oriFile = File(mi.oldPathUrl)
        val values = ContentValues()
        values.put(
            MediaStore.Video.Media.TITLE,
            mi.displayName.substring(
                0,
                mi.displayName.lastIndexOf(".")
            )
        )
        values.put(MediaStore.Video.Media.DISPLAY_NAME, mi.displayName)
        values.put(MediaStore.Video.Media.DATA, mi.oldPathUrl)
        values.put(MediaStore.Video.Media.DATE_MODIFIED, oriFile.lastModified())
        values.put(MediaStore.Video.Media.SIZE, oriFile.length())
        values.put(MediaStore.Video.Media.MIME_TYPE, mi.mimeType)
        val contentUri = cr.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values)
        if (contentUri != null) {
            mi.id = ContentUris.parseId(contentUri)
        }
    }

    // 获取所有已加密的视频列表
    suspend fun getHideVideos(beyondGroupId: Int): List<HideVideo> {
        val videos = getHideVideosByBeyondGroupIdUseCase.execute(beyondGroupId)
        val list: List<HideVideo> = FileHideUtils.checkHideVideo(videos.toMutableList())
        if (list.isNotEmpty()) {
            for (hideFile in list) {
                deleteVideoByPath(hideFile)
            }
        }
        return videos
    }


    // 获取已隐藏图片的数量不分组别
    fun getHideVideoCount(): Int = runBlocking {
        val hideVideoList: List<HideVideo> = getAllHideVideoUseCase.execute()
        return@runBlocking hideVideoList.size
    }

    // 删除指定视频文件
    suspend fun deleteVideoByPath(hideVideo: HideVideo): Boolean {
        if (hideVideo.newPathUrl.isEmpty()) {
            return false
        }
        val videoFile = File(hideVideo.newPathUrl)
        deleteHideVideoUseCase.execute(hideVideo)
        if (videoFile.delete()) {
            delSysMedia(
                VideoModel(
                    id = hideVideo.id!!,
                    title = hideVideo.title,
                    album = hideVideo.album,
                    artist = hideVideo.artist,
                    path = hideVideo.newPathUrl,
                    displayName = hideVideo.displayName,
                    mimeType = hideVideo.mimeType,
                    duration = hideVideo.duration.toLong(),
                    size = hideVideo.size
                )
            )
            return true
        }
        return false
    }

}