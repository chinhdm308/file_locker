package com.base.presentation.models

import com.base.presentation.baseadapter.BaseHideAdapter
import com.base.presentation.utils.transMoney
import com.base.domain.models.video.VideoModel


class VideoModelExt(
    id: Long,
    title: String,
    album: String?,
    artist: String?,
    path: String,
    displayName: String,
    mimeType: String,
    duration: Long,
    size: Long,
) : VideoModel(id, title, album, artist, path, displayName, mimeType, duration, size),
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
        private fun copyVal(videoModel: VideoModel): VideoModelExt {
            return VideoModelExt(
                videoModel.id,
                videoModel.title,
                videoModel.album,
                videoModel.artist,
                videoModel.path,
                videoModel.displayName,
                videoModel.mimeType,
                videoModel.duration,
                videoModel.size
            )
        }

        fun transList(list: List<VideoModel>?): List<VideoModelExt> {
            return list?.map { copyVal(it) } ?: emptyList()
        }
    }

    fun getSizeStr(): String {
        val size = size.toFloat() / 1024 / 1024
        return transMoney(size) + "MB"
    }
}