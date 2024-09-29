package com.base.data.local.database.groupimage

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("group_image_table")
data class GroupImageEntity(
    @PrimaryKey val id: Long?,
    val parentId: Int?,
    val name: String?,
    val createDate: Long?
)