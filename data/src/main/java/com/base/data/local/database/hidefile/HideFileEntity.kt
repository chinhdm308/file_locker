package com.base.data.local.database.hidefile

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("hide_file_table")
data class HideFileEntity(
    @PrimaryKey val id: Int?,
    val beyondGroupId: Int,
    val name: String,
    val oldPathUrl: String,
    val newPathUrl: String,
    val moveDate: Long
)