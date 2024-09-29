package com.base.data.local.database.hidefile

import com.base.domain.models.file.HideFile
import javax.inject.Inject

class HideFileMapper @Inject constructor() {

    fun transform(type: HideFile): HideFileEntity {
        return HideFileEntity(
            id = type.id?.toInt(),
            beyondGroupId = type.beyondGroupId,
            name = type.name,
            newPathUrl = type.newPathUrl,
            oldPathUrl = type.oldPathUrl,
            moveDate = type.moveDate
        )
    }

    fun transform(type: HideFileEntity): HideFile {
        return HideFile(
            id = type.id?.toLong(),
            beyondGroupId = type.beyondGroupId,
            name = type.name,
            newPathUrl = type.newPathUrl,
            oldPathUrl = type.oldPathUrl,
            moveDate = type.moveDate
        )
    }
}