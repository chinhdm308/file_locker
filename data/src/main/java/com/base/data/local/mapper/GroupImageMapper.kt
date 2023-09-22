package com.base.data.local.mapper

import com.base.data.local.entities.GroupImageEntity
import com.base.domain.models.image.GroupImage
import javax.inject.Inject

class GroupImageMapper @Inject constructor() {
    fun transform(type: GroupImage): GroupImageEntity {
        return GroupImageEntity(
            id = type.getId(),
            parentId = type.getParentId(),
            name = type.getName(),
            createDate = type.getCreateDate()
        )
    }

    fun transform(type: GroupImageEntity): GroupImage {
        return GroupImage(
            id = type.id,
            parentId = type.parentId,
            name = type.name,
            createDate = type.createDate
        )
    }
}