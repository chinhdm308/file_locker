package com.base.data.local.database.hidevideo

import androidx.room.Dao
import androidx.room.Query
import com.base.data.local.dao.base.BaseDao

@Dao
interface HideVideoDao : BaseDao<HideVideoEntity> {
    @Query("SELECT * FROM hide_video_table")
    fun loadAll(): List<HideVideoEntity>

    @Query("SELECT * FROM hide_video_table WHERE beyondGroupId=:beyondGroupId")
    fun getHideAudiosByBeyondGroupId(beyondGroupId: Int): List<HideVideoEntity>
}