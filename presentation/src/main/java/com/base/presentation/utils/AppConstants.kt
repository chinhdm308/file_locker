package com.base.presentation.utils

import android.os.Environment
import com.base.presentation.R
import com.base.presentation.ui.language.model.LanguageModel
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

    const val EXTRA_PATTERN_MODE = "EXTRA_PATTERN_MODE"
    const val RC_CHANGE_PATTERN = "RC_CHANGE_PATTERN"
    const val RC_CREATE_PATTERN = "RC_CREATE_PATTERN"

    const val DEFAULT_INCORRECT_TIMES = 3

    val SD_PATH: String = Environment.getExternalStorageDirectory().path

    fun getHidePath(pathString: String): String {
        val pathFile = File(pathString)
        return pathFile.path.substring(0, pathFile.path.lastIndexOf('/')) + "/."
    }

    fun getSuffix(): String {
        return ".lock"
    }

    val listLanguages = listOf(
        LanguageModel("English", "en", R.drawable.ic_lang_eng),
        LanguageModel("French", "fr", R.drawable.ic_lang_fran),
        LanguageModel("German", "de", R.drawable.ic_lang_ger),
        LanguageModel("Hindi", "hi", R.drawable.ic_lang_hin),
        LanguageModel("Italian", "it", R.drawable.ic_lang_italy),
        LanguageModel("Japanese", "ja", R.drawable.ic_lang_japanese),
        LanguageModel("Korean", "ko", R.drawable.ic_lang_korean),
        LanguageModel("Portuguese", "pt", R.drawable.ic_lang_pot),
        LanguageModel("Spanish", "es", R.drawable.ic_lang_spain),
        LanguageModel("Vietnamese", "vi", R.drawable.ic_lang_vietnam),
    )
}