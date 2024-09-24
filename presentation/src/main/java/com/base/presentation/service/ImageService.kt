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
import com.base.domain.models.image.HideImage
import com.base.domain.models.image.ImageModel
import com.base.domain.usecases.hideimage.DeleteHideImageUseCase
import com.base.domain.usecases.hideimage.GetAllHideImageUseCase
import com.base.domain.usecases.hideimage.GetHideImagesByBeyondGroupIdUseCase
import com.base.domain.usecases.hideimage.InsertHideImageUseCase
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

class ImageService @Inject constructor(
    @ApplicationContext val context: Context,
    private val fileManager: FileManager,
    private val insertHideImageUseCase: InsertHideImageUseCase,
    private val deleteHideImageUseCase: DeleteHideImageUseCase,
    private val getAllHideImageUseCase: GetAllHideImageUseCase,
    private val getHideImagesByBeyondGroupIdUseCase: GetHideImagesByBeyondGroupIdUseCase
) : AbstructProvider {
    override fun getList(): List<Any> {
        val list = mutableListOf<ImageModel>()
        var needCheck = SdCardUtil.needCheckExtSdCard()
        val mPath = SdCardUtil.getExtSdCardPath()
        if (mPath == null) {
            needCheck = false
        }
        val selection =
            StringBuilder("${MediaStore.Images.Media.TITLE} != ''")
        // Display audios in alphabetical order based on their display name.
        val sortOrder = "${MediaStore.Images.Media.DISPLAY_NAME} ASC"
        val cursor: Cursor? = context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            null,
            selection.toString(),
            null,
            sortOrder
        )
//        val cursor: Cursor? = context.contentResolver.query(
//            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null,
//            null, null
//        )
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID))
                val title =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.TITLE))
                val path =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))
                val displayName =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME))
                val mimeType =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.MIME_TYPE))
                val size =
                    cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE))

                if (fileManager.isHideFile(displayName)) continue

                // 判断是否需要添加
                if (needCheck && (mPath != null && path.contains(mPath))) {
                    continue
                }
                val image = ImageModel(id.toLong(), title, path, displayName, mimeType, size)
                list.add(image)
            }
            cursor.close()
        }

        return list
    }

    // 隐藏图片
    suspend fun hideImage(imageModel: ImageModel, beyondGroupId: Int): Boolean {
        val fromFile = File(imageModel.path)
        if (!fromFile.exists()) {
            return false
        }
        val toPathString: String = AppConstants.getHidePath(imageModel.path)
        if (toPathString.isEmpty()) {
            return false
        }
        val toFile = File(toPathString + imageModel.displayName + AppConstants.getSuffix())
        // 复制
        if (fromFile.renameTo(toFile)) {
            // 插入数据库
            val id: Long = insertHideImageUseCase.execute(
                HideImage(
                    id = null,
                    beyondGroupId = beyondGroupId,
                    title = imageModel.title,
                    oldPathUrl = imageModel.path,
                    displayName = imageModel.displayName,
                    mimeType = imageModel.mimeType,
                    newPathUrl = toFile.path,
                    size = imageModel.size,
                    moveDate = Date().time
                )
            )
            if (id >= 0) {
                return delSysMedia(imageModel)
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
    suspend fun unHideImage(hideImage: HideImage?): Boolean {
        if (hideImage != null) {
            val fromFile = File(hideImage.newPathUrl)
            val toFile = File(hideImage.oldPathUrl)

            // 插入数据库
            deleteHideImageUseCase.execute(hideImage)
            insSysMedia(hideImage)
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
    private fun delSysMedia(mi: ImageModel): Boolean {
        val cr = context.contentResolver
        return cr.delete(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            MediaStore.Images.Media._ID + "=?",
            arrayOf(mi.id.toString())
        ) > 0
    }

    /**
     * 插入系统媒体记录
     */
    private fun insSysMedia(mi: HideImage) {
        val cr = context.contentResolver
        val oriFile = File(mi.oldPathUrl)
        val values = ContentValues()
        values.put(
            MediaStore.Images.Media.TITLE,
            mi.displayName.substring(
                0,
                mi.displayName.lastIndexOf(".")
            )
        )
        values.put(MediaStore.Images.Media.DISPLAY_NAME, mi.displayName)
        values.put(MediaStore.Images.Media.DATA, mi.oldPathUrl)
        values.put(MediaStore.Images.Media.DATE_MODIFIED, oriFile.lastModified())
        values.put(MediaStore.Images.Media.SIZE, oriFile.length())
        values.put(MediaStore.Images.Media.MIME_TYPE, mi.mimeType)
        val contentUri = cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        if (contentUri != null) {
            mi.id = ContentUris.parseId(contentUri)
        }
    }

    // 获取所有已加密的音频文件列表
    suspend fun getHideImages(beyondGroupId: Int): List<HideImage> {
        val images = getHideImagesByBeyondGroupIdUseCase.execute(beyondGroupId)
        val list: List<HideImage> = FileHideUtils.checkHideImage(images.toMutableList())
        if (list.isNotEmpty()) {
            for (hideFile in list) {
                deleteImageByPath(hideFile)
            }
        }
        return images
    }


    // 获取已隐藏图片的数量不分组别
    fun getHideImageCount(): Int = runBlocking {
        val hideAudioList: List<HideImage> = getAllHideImageUseCase.execute()
        return@runBlocking hideAudioList.size
    }

    // 删除指定音频文件
    suspend fun deleteImageByPath(hideImage: HideImage): Boolean {
        if (hideImage.newPathUrl.isEmpty()) {
            return false
        }
        val audioFile = File(hideImage.newPathUrl)
        deleteHideImageUseCase.execute(hideImage)
        if (audioFile.delete()) {
            delSysMedia(
                ImageModel(
                    hideImage.id!!,
                    hideImage.title,
                    hideImage.newPathUrl,
                    hideImage.displayName,
                    hideImage.mimeType,
                    hideImage.size
                )
            )
            return true
        }
        return false
    }
}