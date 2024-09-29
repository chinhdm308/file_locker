package com.base.data.local.database.hideimage

import androidx.room.Dao
import androidx.room.Query
import com.base.data.local.dao.base.BaseDao

@Dao
interface HideImageDao : BaseDao<HideImageEntity> {
    @Query("SELECT * FROM hide_image_table")
    fun loadAll(): List<HideImageEntity>

    @Query("SELECT * FROM hide_image_table WHERE beyondGroupId=:beyondGroupId")
    fun getHideImagesByBeyondGroupId(beyondGroupId: Int): List<HideImageEntity>
}