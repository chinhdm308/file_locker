package com.base.domain.repositories

import com.base.domain.models.image.GroupImage

interface GroupImageRepository {
    suspend fun insertOrReplace(groupImage: GroupImage, isReplacement: Boolean): Long

    suspend fun delete(groupImage: GroupImage): Int

    suspend fun getGroupImagesByBeyondGroupId(beyondGroupId: Int): List<GroupImage>
}