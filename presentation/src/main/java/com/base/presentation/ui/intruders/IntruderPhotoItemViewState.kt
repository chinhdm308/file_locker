package com.base.presentation.ui.intruders

import java.io.File

data class IntruderPhotoItemViewState(val file: File) {

    fun getFilePath(): String = file.absolutePath

    fun getImageSize(): ImageSize = ImageSize.MEDIUM
}