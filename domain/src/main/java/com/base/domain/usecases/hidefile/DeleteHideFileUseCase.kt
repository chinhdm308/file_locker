package com.base.domain.usecases.hidefile

import com.base.domain.models.file.HideFile
import com.base.domain.repositories.HideFileRepository
import javax.inject.Inject

class DeleteHideFileUseCase @Inject constructor(
    private val hideFileRepository: HideFileRepository
) {
    suspend fun execute(hideFile: HideFile): Long {
        return hideFileRepository.delete(hideFile)
    }
}