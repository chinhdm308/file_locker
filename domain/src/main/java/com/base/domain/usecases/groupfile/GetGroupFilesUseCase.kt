package com.base.domain.usecases.groupfile

import com.base.domain.models.file.GroupFile
import com.base.domain.repositories.GroupFileRepository
import javax.inject.Inject

class GetGroupFilesUseCase @Inject constructor(
    private val groupFileRepository: GroupFileRepository
) {
    suspend fun execute(beyondGroupId: Int): List<GroupFile> {
        return groupFileRepository.getGroupFilesByBeyondGroupId(beyondGroupId)
    }
}