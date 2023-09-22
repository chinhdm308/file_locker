package com.base.domain.usecases.groupimage

import com.base.domain.models.image.GroupImage
import com.base.domain.repositories.GroupImageRepository
import javax.inject.Inject

class GetGroupImagesByBeyondGroupIdUseCase @Inject constructor(
    private val groupImageRepository: GroupImageRepository
) {
    suspend fun execute(beyondGroupId: Int): List<GroupImage> {
        return groupImageRepository.getGroupImagesByBeyondGroupId(beyondGroupId)
    }
}