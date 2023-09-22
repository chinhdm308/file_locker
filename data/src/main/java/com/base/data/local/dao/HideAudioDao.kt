package com.base.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.base.data.local.dao.base.BaseDao
import com.base.data.local.entities.HideAudioEntity

@Dao
interface HideAudioDao: BaseDao<HideAudioEntity> {

    @Query("SELECT * FROM hide_audio_table")
    fun loadAll(): List<HideAudioEntity>

    @Query("SELECT * FROM hide_audio_table WHERE beyondGroupId=:beyondGroupId")
    fun getHideAudiosByBeyondGroupId(beyondGroupId: Int): List<HideAudioEntity>
}