package com.base.presentation.utils

import android.os.Environment
import java.io.File


object AppConstants {
    //Folder lock
    const val MAIN_FOLDER = ".chinchin_file_hide"
    const val INTRUDER_FOLDER = ".intruders"
    const val FILE_LOCKER_FOLDER = ".file_hide"
    const val IMAGE_LOCKER_FOLDER = ".image_hide"
    const val VIDEO_LOCKER_FOLDER = ".video_hide"
    const val AUDIO_LOCKER_FOLDER = ".audio_hide"
    const val VAULT_LOCKER_FOLDER = ".vault"

    const val EXTRA_TO_APP = "EXTRA_TO_APP"

    val SD_PATH: String = Environment.getExternalStorageDirectory().path

    fun getHidePath(pathString: String): String {
        val pathFile = File(pathString)
        return pathFile.path.substring(0, pathFile.path.lastIndexOf('/')) + "/."
    }

    fun getSuffix(): String {
        return ".lock"
    }
}