package com.base.basemvvmcleanarchitecture.utils.helper.file

import android.content.Context
import android.os.Environment
import com.base.basemvvmcleanarchitecture.utils.AppConstants
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import java.io.File
import java.util.Locale
import javax.inject.Inject

class FileManager @Inject constructor(@ApplicationContext val context: Context) {

    enum class SubFolder(val subFolderPath: String) {
        VAULT(EXTERNAL_FOLDER_VAULT),
        INTRUDERS(EXTERNAL_FOLDER_INTRUDERS)
    }

    fun createFile(fileOperationRequest: FileOperationRequest, subFolder: SubFolder): File {
        val folder = when (fileOperationRequest.directoryType) {
            DirectoryType.CACHE -> getCacheDir()
            DirectoryType.EXTERNAL -> getExternalDirectory(subFolder)
        }

        return File(
            folder,
            fileOperationRequest.fileName + fileOperationRequest.fileExtension.extension
        )
    }

    fun deleteFile(filePath: String): Boolean {
        return File(filePath).delete()
    }

    fun getFile(fileOperationRequest: FileOperationRequest, subFolder: SubFolder): File {
        val folder = when (fileOperationRequest.directoryType) {
            DirectoryType.CACHE -> getCacheDir()
            DirectoryType.EXTERNAL -> getExternalDirectory(subFolder)
        }

        return File(folder.absolutePath + "/" + fileOperationRequest.fileName + fileOperationRequest.fileExtension.extension)
    }

    fun getSubFiles(folder: File, extension: FileExtension): List<File> {
        if (folder.isFile || folder.exists().not()) {
            return arrayListOf()
        }

        Timber.v("files : " + folder.absolutePath)
        val files = folder.listFiles() ?: arrayOf()
        return files.filter {
            it.name.lowercase(Locale.ROOT).endsWith(extension.extension.lowercase(Locale.ROOT))
        }
    }

    fun createFileInCache(fileName: String): File {
        return File(context.cacheDir, "$fileName.jpg")
    }

    fun isFileExist(filePath: String): Boolean {
        return File(filePath).exists()
    }

    fun isFileInCache(fileName: String): Boolean {
        val cacheFile = File(context.cacheDir, fileName)
        return cacheFile.exists()
    }

    fun getRootExternalDirectory(): File {
        val appPath = Environment.getExternalStorageDirectory().toString() + File.separator + EXTERNAL_FOLDER_ROOT_APP
        val folder = File(appPath)
        if (!folder.exists()) {
            folder.mkdirs()
        }
        return folder
    }

    fun getExternalDirectory(subFolder: SubFolder): File {
        val appPath = getRootExternalDirectory().toString() + File.separator + subFolder.subFolderPath
        val folder = File(appPath)
        if (!folder.exists()) {
            folder.mkdirs()
        }
        return folder
    }

    fun getCacheDir(): File = context.cacheDir

    /**
     * Create a folder in /sdcard/ or /data/data/pkg/files/
     */
    private fun mkDir(context: Context, dir: String): File? {
        var sdPath = getSDPath()
        if (sdPath == null) {
            sdPath = context.filesDir.path
        }
        val path = sdPath + File.separator + dir
        val file = File(path)
        if (!file.exists() && !file.mkdir()) {
            return null
        }
        return file
    }

    /**
     * 缓存.photocat/cache
     */
    fun getCacheFile(context: Context, dir: String = ".photocat/cache"): File? {
        return mkDir(context, dir)
    }

    /**
     * Get SD card path
     *
     * @return
     */
    fun getSDPath(): String? {
        //Determine whether the sd card exists
        val sdCardExist = Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
        return if (sdCardExist) {
            //Get the directory
            Environment.getExternalStorageDirectory().toString()
        } else null
    }

    /**
     * Determine whether a hidden file is hidden by its name
     *
     * @param name
     * @return
     */
    fun isHideFile(name: String?): Boolean {
        return !name.isNullOrEmpty() && name.substring(0, 1) == "."
    }

    fun isHideFile(file: File): Boolean {
        return isHideFile(file.name)
    }

    companion object {
        private const val EXTERNAL_FOLDER_ROOT_APP = AppConstants.MAIN_FOLDER
        private const val EXTERNAL_FOLDER_VAULT = AppConstants.VAULT_LOCKER_FOLDER
        private const val EXTERNAL_FOLDER_INTRUDERS = AppConstants.INTRUDER_FOLDER

        fun getInstance(context: Context): FileManager {
            return FileManager(context)
        }
    }

}