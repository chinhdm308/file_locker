package com.base.presentation.models

import com.base.presentation.baseadapter.BaseHideAdapter
import com.base.presentation.utils.transMoney
import com.base.domain.models.audio.AudioModel


class AudioModelExt(
    id: Long,
    title: String,
    album: String?,
    artist: String?,
    path: String,
    displayName: String,
    mimeType: String,
    duration: Long,
    size: Long,
) : AudioModel(id, title, album, artist, path, displayName, mimeType, duration, size),
    BaseHideAdapter.IEnable {

    /**
     * 是否选中
     */
    private var enable = false

    override fun isEnable(): Boolean {
        return enable
    }

    override fun setEnable(enable: Boolean) {
        this.enable = enable
    }

    companion object {
        private fun copyVal(audioModel: AudioModel): AudioModelExt {
            return AudioModelExt(
                audioModel.id,
                audioModel.title,
                audioModel.album,
                audioModel.artist,
                audioModel.path,
                audioModel.displayName,
                audioModel.mimeType,
                audioModel.duration,
                audioModel.size
            )
        }

        fun transList(list: List<AudioModel>?): List<AudioModelExt> {
            return list?.map { copyVal(it) } ?: emptyList()
        }
    }

    fun getSizeStr(): String {
        val size = size.toFloat() / 1024 / 1024
        return transMoney(size) + "MB"
    }
}