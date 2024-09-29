package com.base.data.local.database.hideaudio

import com.base.domain.models.audio.HideAudio
import javax.inject.Inject

class HideAudioMapper @Inject constructor() {
    fun transform(type: HideAudio): HideAudioEntity {
        return HideAudioEntity(
            id = type.id?.toInt(),
            beyondGroupId = type.beyondGroupId,
            title = type.title,
            album = type.album,
            artist = type.artist,
            oldPathUrl = type.oldPathUrl,
            displayName = type.displayName,
            mimeType = type.mimeType,
            duration = type.duration,
            newPathUrl = type.newPathUrl,
            size = type.size,
            moveDate = type.moveDate
        )
    }

    fun transform(type: HideAudioEntity): HideAudio {
        return HideAudio(
            id = type.id?.toLong(),
            beyondGroupId = type.beyondGroupId,
            title = type.title,
            album = type.album,
            artist = type.artist,
            oldPathUrl = type.oldPathUrl,
            displayName = type.displayName,
            mimeType = type.mimeType,
            duration = type.duration,
            newPathUrl = type.newPathUrl,
            size = type.size,
            moveDate = type.moveDate
        )
    }
}