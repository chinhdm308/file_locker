package com.base.basemvvmcleanarchitecture.models

import com.base.basemvvmcleanarchitecture.baseadapter.BaseHideAdapter
import com.base.basemvvmcleanarchitecture.utils.transMoney
import com.base.domain.models.audio.AudioModel
import com.base.domain.models.video.HideVideo
import timber.log.Timber


class HideVideoExt(
    id: Long?,
    beyondGroupId: Int,
    title: String,
    album: String?,
    artist: String?,
    oldPathUrl: String,
    displayName: String,
    mimeType: String,
    duration: String,
    newPathUrl: String,
    size: Long,
    moveDate: Long,
) : HideVideo(
    id,
    beyondGroupId,
    title,
    album,
    artist,
    oldPathUrl,
    displayName,
    mimeType,
    duration,
    newPathUrl,
    size,
    moveDate
), BaseHideAdapter.IEnable {

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
        private fun copyVal(hideVideo: HideVideo): HideVideoExt {
            return HideVideoExt(
                hideVideo.id,
                hideVideo.beyondGroupId,
                hideVideo.title,
                hideVideo.album,
                hideVideo.artist,
                hideVideo.oldPathUrl,
                hideVideo.displayName,
                hideVideo.mimeType,
                hideVideo.duration,
                hideVideo.newPathUrl,
                hideVideo.size,
                hideVideo.moveDate
            )
        }

        fun transList(list: List<Any>?): List<HideVideoExt> {
            return list?.map { copyVal(it as HideVideo) } ?: emptyList()
        }
    }

    fun getSizeStr(): String {
        val size = size.toFloat() / 1024 / 1024
        return transMoney(size) + "MB"
    }

    fun transientToModel(): AudioModel? {
        return try {
            AudioModel(
                id = id?.toInt()?.toLong() ?: 0L,
                title = title,
                album = album,
                artist = artist,
                path = newPathUrl,
                displayName = displayName,
                mimeType = mimeType,
                duration = duration.toLong(),
                size = size
            )
        } catch (e: NumberFormatException) {
            Timber.tag("HideAudioExt").e(e.toString())
            null
        }
    }
}