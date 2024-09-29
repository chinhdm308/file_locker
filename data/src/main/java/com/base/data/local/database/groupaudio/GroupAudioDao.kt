package com.base.data.local.database.groupaudio

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.base.data.local.dao.base.BaseDao

@Dao
interface GroupAudioDao : BaseDao<GroupAudioEntity> {

    @Insert
    suspend fun insert(groupAudioEntity: GroupAudioEntity): Long

    @Query("SELECT * FROM group_audio_table WHERE parentId=:beyondGroupId")
    suspend fun getGroupFilesByBeyondGroupId(beyondGroupId: Int): List<GroupAudioEntity>
}