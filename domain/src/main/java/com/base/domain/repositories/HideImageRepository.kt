package com.base.domain.repositories

import com.base.domain.models.image.HideImage

interface HideImageRepository {
    suspend fun loadAll(): List<HideImage>

    suspend fun insertOrReplace(hideImage: HideImage): Long

    suspend fun delete(hideImage: HideImage)

    suspend fun getHideImagesByBeyondGroupId(beyondGroupId: Int): List<HideImage>
}