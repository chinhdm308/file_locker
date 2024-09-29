package com.base.data.local.database.groupvideo

import com.base.domain.models.video.GroupVideo
import javax.inject.Inject

class GroupVideoMapper @Inject constructor() {
    fun transform(type: GroupVideo): GroupVideoEntity {
        return GroupVideoEntity(
            id = type.getId(),
            parentId = type.getParentId(),
            name = type.getName(),
            createDate = type.getCreateDate()
        )
    }

    fun transform(type: GroupVideoEntity): GroupVideo {
        return GroupVideo(
            id = type.id,
            parentId = type.parentId,
            name = type.name,
            createDate = type.createDate
        )
    }
}