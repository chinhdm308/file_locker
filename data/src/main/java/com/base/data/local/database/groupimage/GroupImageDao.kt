package com.base.data.local.database.groupimage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.base.data.local.dao.base.BaseDao

@Dao
interface GroupImageDao : BaseDao<GroupImageEntity> {
    @Insert
    suspend fun insert(groupImageEntity: GroupImageEntity): Long

    @Query("SELECT * FROM group_image_table WHERE parentId=:beyondGroupId")
    suspend fun getGroupImagesByBeyondGroupId(beyondGroupId: Int): List<GroupImageEntity>
}