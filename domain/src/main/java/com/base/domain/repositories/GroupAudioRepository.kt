package com.base.domain.repositories

import com.base.domain.models.audio.GroupAudio

interface GroupAudioRepository {
    suspend fun insertOrReplace(groupAudio: GroupAudio, isReplacement: Boolean): Long

    suspend fun delete(groupAudio: GroupAudio): Int

    suspend fun getGroupFilesByBeyondGroupId(beyondGroupId: Int): List<GroupAudio>
}