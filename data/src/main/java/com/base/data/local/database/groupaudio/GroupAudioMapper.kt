package com.base.data.local.database.groupaudio

import com.base.domain.models.audio.GroupAudio
import javax.inject.Inject

class GroupAudioMapper @Inject constructor() {
    fun transform(type: GroupAudio): GroupAudioEntity {
        return GroupAudioEntity(
            id = type.getId(),
            parentId = type.getParentId(),
            name = type.getName(),
            createDate = type.getCreateDate()
        )
    }

    fun transform(type: GroupAudioEntity): GroupAudio {
        return GroupAudio(
            id = type.id,
            parentId = type.parentId,
            name = type.name,
            createDate = type.createDate
        )
    }
}