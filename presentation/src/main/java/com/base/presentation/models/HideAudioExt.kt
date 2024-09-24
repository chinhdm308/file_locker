package com.base.presentation.models

import com.base.presentation.baseadapter.BaseHideAdapter
import com.base.presentation.utils.transMoney
import com.base.domain.models.audio.AudioModel
import com.base.domain.models.audio.HideAudio
import timber.log.Timber


class HideAudioExt(
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
) : HideAudio(
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
        private fun copyVal(hideAudio: HideAudio): HideAudioExt {
            return HideAudioExt(
                hideAudio.id,
                hideAudio.beyondGroupId,
                hideAudio.title,
                hideAudio.album,
                hideAudio.artist,
                hideAudio.oldPathUrl,
                hideAudio.displayName,
                hideAudio.mimeType,
                hideAudio.duration,
                hideAudio.newPathUrl,
                hideAudio.size,
                hideAudio.moveDate
            )
        }

        fun transList(list: List<Any>?): List<HideAudioExt> {
            return list?.map { copyVal(it as HideAudio) } ?: emptyList()
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