package com.base.domain.usecases.groupimage

import com.base.domain.models.image.GroupImage
import com.base.domain.repositories.GroupImageRepository
import javax.inject.Inject

class DeleteGroupImageUseCase @Inject constructor(
    private val groupImageRepository: GroupImageRepository
) {
    suspend fun execute(groupImage: GroupImage): Int {
        return groupImageRepository.delete(groupImage)
    }
}