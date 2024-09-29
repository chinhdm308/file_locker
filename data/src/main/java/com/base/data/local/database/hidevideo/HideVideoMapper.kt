package com.base.data.local.database.hidevideo

import com.base.domain.models.video.HideVideo
import javax.inject.Inject

class HideVideoMapper @Inject constructor() {
    fun transform(type: HideVideo): HideVideoEntity {
        return HideVideoEntity(
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

    fun transform(type: HideVideoEntity): HideVideo {
        return HideVideo(
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