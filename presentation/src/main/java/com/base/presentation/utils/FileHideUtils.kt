package com.base.presentation.utils

import com.base.domain.models.audio.HideAudio
import com.base.domain.models.file.HideFile
import com.base.domain.models.image.HideImage
import com.base.domain.models.video.HideVideo
import java.io.File


object FileHideUtils {
    /**
     * Check the hidden file list. If there are deleted files, return to submit and update the database.
     *
     * @param list
     * @return
     */
    fun checkHideFile(list: MutableList<HideFile>): List<HideFile> {
        val listTemp: MutableList<HideFile> = mutableListOf()
        for (i in list.size - 1 downTo -1 + 1) {
            val hideFile: HideFile = list[i]
            if (!File(hideFile.newPathUrl).exists()) {
                listTemp.add(hideFile)
                list.removeAt(i)
            }
        }
        return listTemp
    }

    fun checkHideImage(list: MutableList<HideImage>): List<HideImage> {
        val listTemp: MutableList<HideImage> = mutableListOf()
        for (i in list.size - 1 downTo -1 + 1) {
            val hideFile: HideImage = list[i]
            if (!File(hideFile.newPathUrl).exists()) {
                listTemp.add(hideFile)
                list.removeAt(i)
            }
        }
        return listTemp
    }

    fun checkHideAudio(list: MutableList<HideAudio>): List<HideAudio> {
        val listTemp: MutableList<HideAudio> = mutableListOf()
        for (i in list.size - 1 downTo -1 + 1) {
            val hideFile = list[i]
            if (!File(hideFile.newPathUrl).exists()) {
                listTemp.add(hideFile)
                list.removeAt(i)
            }
        }
        return listTemp
    }

    fun checkHideVideo(list: MutableList<HideVideo>): List<HideVideo> {
        val listTemp: MutableList<HideVideo> = mutableListOf()
        for (i in list.size - 1 downTo -1 + 1) {
            val hideFile: HideVideo = list[i]
            if (!File(hideFile.newPathUrl).exists()) {
                listTemp.add(hideFile)
                list.removeAt(i)
            }
        }
        return listTemp
    }
}