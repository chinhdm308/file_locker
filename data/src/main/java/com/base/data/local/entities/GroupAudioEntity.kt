package com.base.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("group_audio_table")
data class GroupAudioEntity(
    @PrimaryKey val id: Long?,
    val parentId: Int?,
    val name: String?,
    val createDate: Long?
)