package com.base.data.local.database.groupvideo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("group_video_table")
data class GroupVideoEntity(
    @PrimaryKey val id: Long?,
    val parentId: Int?,
    val name: String?,
    val createDate: Long?
)
