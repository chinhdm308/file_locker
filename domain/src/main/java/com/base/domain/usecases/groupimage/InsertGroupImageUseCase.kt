package com.base.domain.usecases.groupimage

import com.base.domain.models.image.GroupImage
import com.base.domain.repositories.GroupImageRepository
import javax.inject.Inject

class InsertGroupImageUseCase @Inject constructor(
    private val groupImageRepository: GroupImageRepository
) {
    suspend fun execute(groupImage: GroupImage, isReplacement: Boolean): Long {
        return groupImageRepository.insertOrReplace(groupImage, isReplacement)
    }
}