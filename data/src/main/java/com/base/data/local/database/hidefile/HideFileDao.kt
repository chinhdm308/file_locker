package com.base.data.local.database.hidefile

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.base.data.local.dao.base.BaseDao

@Dao
interface HideFileDao : BaseDao<HideFileEntity> {

    @Insert
    suspend fun insert(entity: HideFileEntity): Long

    @Query("SELECT * FROM hide_file_table")
    suspend fun loadAll(): List<HideFileEntity>

    @Query("SELECT * FROM hide_file_table WHERE beyondGroupId=:beyondGroupId")
    suspend fun getHideFilesByBeyondGroupId(beyondGroupId: Int): List<HideFileEntity>
}