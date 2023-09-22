package com.base.domain.usecases.groupfile

import com.base.domain.models.file.GroupFile
import com.base.domain.repositories.GroupFileRepository
import javax.inject.Inject

class InsertGroupFileUseCase @Inject constructor(
    private val groupFileRepository: GroupFileRepository
) {
    suspend fun execute(groupFile: GroupFile, isReplacement: Boolean): Long {
        return groupFileRepository.insertOrReplace(groupFile, isReplacement)
    }
}