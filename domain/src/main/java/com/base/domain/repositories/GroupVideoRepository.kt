package com.base.domain.repositories

import com.base.domain.models.video.GroupVideo

interface GroupVideoRepository {
    suspend fun insertOrReplace(groupVideo: GroupVideo, isReplacement: Boolean): Long

    suspend fun delete(groupVideo: GroupVideo): Int

    suspend fun getGroupFilesByBeyondGroupId(beyondGroupId: Int): List<GroupVideo>
}