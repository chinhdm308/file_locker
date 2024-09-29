package com.base.data.local.database.hideimage

import com.base.domain.models.image.HideImage
import javax.inject.Inject

class HideImageMapper @Inject constructor() {
    fun transform(type: HideImage): HideImageEntity {
        return HideImageEntity(
            id = type.id,
            beyondGroupId = type.beyondGroupId,
            title = type.title,
            oldPathUrl = type.oldPathUrl,
            displayName = type.displayName,
            mimeType = type.mimeType,
            newPathUrl = type.newPathUrl,
            size = type.size,
            moveDate = type.moveDate
        )
    }

    fun transform(type: HideImageEntity): HideImage {
        return HideImage(
            id = type.id,
            beyondGroupId = type.beyondGroupId,
            title = type.title,
            oldPathUrl = type.oldPathUrl,
            displayName = type.displayName,
            mimeType = type.mimeType,
            newPathUrl = type.newPathUrl,
            size = type.size,
            moveDate = type.moveDate
        )
    }
}