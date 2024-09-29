package com.base.data.local.database.hideimage

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("hide_image_table")
class HideImageEntity(
    @PrimaryKey var id: Long?,
    val beyondGroupId: Int,
    val title: String,
    val oldPathUrl: String,
    val displayName: String,
    val mimeType: String,
    val newPathUrl: String,
    val size: Long,
    val moveDate: Long
)