package com.base.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.base.data.local.dao.base.BaseDao
import com.base.data.local.entities.GroupVideoEntity

@Dao
interface GroupVideoDao : BaseDao<GroupVideoEntity> {

    @Insert
    suspend fun insert(groupVideoEntity: GroupVideoEntity): Long

    @Query("SELECT * FROM group_video_table WHERE parentId=:beyondGroupId")
    suspend fun getGroupFilesByBeyondGroupId(beyondGroupId: Int): List<GroupVideoEntity>
}