package com.base.domain.repositories

import com.base.domain.models.file.GroupFile

interface GroupFileRepository {
    suspend fun insertOrReplace(groupFile: GroupFile, isReplacement: Boolean): Long

    suspend fun delete(groupFile: GroupFile): Long

    suspend fun getGroupFilesByBeyondGroupId(beyondGroupId: Int): List<GroupFile>
}