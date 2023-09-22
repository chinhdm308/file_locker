package com.base.domain.repositories

import com.base.domain.models.video.HideVideo

interface HideVideoRepository {
    suspend fun loadAll(): List<HideVideo>

    suspend fun insertOrReplace(hideVideo: HideVideo): Long

    suspend fun delete(hideVideo: HideVideo)

    suspend fun getHideVideosByBeyondGroupId(beyondGroupId: Int): List<HideVideo>
}