package com.base.data.local.database.groupfile

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.base.data.local.dao.base.BaseDao

@Dao
interface GroupFileDao : BaseDao<GroupFileEntity> {

    @Insert
    suspend fun insert(entity: GroupFileEntity): Long

    @Query("SELECT * FROM group_file_table WHERE parentId=:beyondGroupId")
    suspend fun getGroupFilesByBeyondGroupId(beyondGroupId: Int): List<GroupFileEntity>
}