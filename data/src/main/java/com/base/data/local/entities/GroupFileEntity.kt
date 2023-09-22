package com.base.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("group_file_table")
data class GroupFileEntity(
    @PrimaryKey var id: Long? = null,
    var parentId: Int? = null,
    var name: String? = null,
    var createDate: Long? = null,
)
