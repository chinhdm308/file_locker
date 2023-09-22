package com.base.domain.repositories

import com.base.domain.models.file.HideFile

interface HideFileRepository {
    suspend fun insertOrReplace(hideFile: HideFile, isReplacement: Boolean): Long

    suspend fun delete(hideFile: HideFile): Long

    suspend fun loadAll(): List<HideFile>

    suspend fun getHideFilesByBeyondGroupId(beyondGroupId: Int): List<HideFile>
}