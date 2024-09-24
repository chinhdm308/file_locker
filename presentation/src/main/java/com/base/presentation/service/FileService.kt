package com.base.presentation.service

import android.os.Environment
import com.base.presentation.utils.AppConstants
import com.base.presentation.utils.FileHideUtils.checkHideFile
import com.base.presentation.utils.helper.file.FileManager
import com.base.domain.models.file.FileModel
import com.base.domain.models.file.HideFile
import com.base.domain.usecases.hidefile.DeleteHideFileUseCase
import com.base.domain.usecases.hidefile.GetHideFilesUseCase
import com.base.domain.usecases.hidefile.InsertHideFileUseCase
import kotlinx.coroutines.runBlocking
import java.io.File
import java.util.Date
import javax.inject.Inject


class FileService @Inject constructor(
    private val insertHideFileUseCase: InsertHideFileUseCase,
    private val deleteHideFileUseCase: DeleteHideFileUseCase,
    private val getHideFilesUseCase: GetHideFilesUseCase,
    private val fileManager: FileManager
) {

    fun getFilesByDir(dirString: String?): List<FileModel>? {
        val fileModels: MutableList<FileModel> = mutableListOf()
        val parentFile: File = if (dirString.isNullOrEmpty()) {
            Environment.getRootDirectory()
        } else {
            File(dirString)
        }

        val fileList: Array<out File>? = parentFile.listFiles()
        if (fileList == null || fileList.isEmpty()) {
            return null
        }
        for (file in fileList) {
            val fileModel = FileModel()
            if (file.isDirectory) {
                fileModel.setFileType(FileModel.FILE_DIR)
            } else {
                fileModel.setFileType(FileModel.FILE_FILE)
            }
            fileModel.setName(file.name)
            fileModel.setPath(file.path)
            val numOfItem = file.listFiles()?.filter { fileManager.isHideFile(it).not() }?.size ?: 0
            fileModel.setNumOfItems(numOfItem)
            fileModels.add(fileModel)
        }
        return fileModels
    }

    // 隐藏文件
    suspend fun hideFile(fileModel: FileModel, beyondGroupId: Int): Boolean {
        if (fileModel.getPath() == null) return false
        val fromFile = File(fileModel.getPath()!!)
        if (!fromFile.exists()) {
            return false
        }
        val toPathString: String = AppConstants.getHidePath(fileModel.getPath()!!)
        if (toPathString.isEmpty()) {
            return false
        }
        val toFile = File(toPathString + fileModel.getName() + AppConstants.getSuffix())
        // 复制
        if (fromFile.renameTo(toFile)) {
            // 插入数据库
            val id: Long = insertHideFileUseCase.execute(
                HideFile(
                    id = null,
                    beyondGroupId = beyondGroupId,
                    name = fileModel.getName()!!,
                    oldPathUrl = fileModel.getPath()!!,
                    newPathUrl = toFile.path,
                    moveDate = Date().time
                ),
                isReplacement = true
            )
            if (id >= 0) {
                return true
            }
        } else {
            fromFile.copyTo(toFile)
            fromFile.delete()
            return toFile.exists()
        }
        return false
    }

    // 取消文件隐藏
    suspend fun unHideFile(hideFile: HideFile?): Boolean {
        if (hideFile != null) {
            val fromFile = File(hideFile.newPathUrl)
            val toFile = File(hideFile.oldPathUrl)
            // 插入数据库
            deleteHideFileUseCase.execute(hideFile)

            // 复制
            return if (fromFile.renameTo(toFile)) {
                true
            } else {
                toFile.copyTo(fromFile)
                toFile.delete()
                fromFile.exists()
            }
        }
        return false
    }

    // 获取所有加密文件列表
    suspend fun getHideFiles(beyondGroupId: Int): List<HideFile?> {
        val hideFiles: List<HideFile> = getHideFilesUseCase.execute(beyondGroupId)
        val list = checkHideFile(hideFiles.toMutableList())
        if (list.isNotEmpty()) {
            for (hideFile in list) {
                unHideFile(hideFile)
                deleteFileByPath(hideFile.oldPathUrl)
            }
        }
        return hideFiles
    }

    fun getHideFileCount(): Int = runBlocking {
        val hideFileList: List<HideFile> = getHideFilesUseCase.execute()
        return@runBlocking hideFileList.size
    }

    // 删除指定文件
    fun deleteFileByPath(pathString: String?): Boolean {
        if (pathString.isNullOrEmpty()) {
            return false
        }
        val fileFile = File(pathString)
        return fileFile.delete()
    }

}