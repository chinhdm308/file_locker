package com.base.domain.usecases.hidefile

import com.base.domain.models.file.HideFile
import com.base.domain.repositories.HideFileRepository
import javax.inject.Inject

class InsertHideFileUseCase @Inject constructor(
    private val hideFileRepository: HideFileRepository
) {
    suspend fun execute(hideFile: HideFile, isReplacement: Boolean): Long {
        return hideFileRepository.insertOrReplace(hideFile, isReplacement)
    }

}