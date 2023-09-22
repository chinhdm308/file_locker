package com.base.data.local.mapper

import com.base.data.local.entities.GroupFileEntity
import com.base.domain.models.file.GroupFile
import javax.inject.Inject

class GroupFileMapper @Inject constructor() {
    fun transform(type: GroupFile): GroupFileEntity {
        return GroupFileEntity(
            id = type.getId(),
            parentId = type.getParentId(),
            name = type.getName(),
            createDate = type.getCreateDate()
        )
    }

    fun transform(type: GroupFileEntity): GroupFile {
        return GroupFile(
            id = type.id,
            parentId = type.parentId,
            name = type.name,
            createDate = type.createDate
        )
    }
}