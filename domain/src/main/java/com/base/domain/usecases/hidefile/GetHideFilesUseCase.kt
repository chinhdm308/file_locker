package com.base.domain.usecases.hidefile

import com.base.domain.models.file.HideFile
import com.base.domain.repositories.HideFileRepository
import javax.inject.Inject

class GetHideFilesUseCase @Inject constructor(
    private val hideFileRepository: HideFileRepository
) {
    suspend fun execute(): List<HideFile> {
        return hideFileRepository.loadAll()
    }

    suspend fun execute(beyondGroupId: Int): List<HideFile> {
        return hideFileRepository.getHideFilesByBeyondGroupId(beyondGroupId)
    }
}