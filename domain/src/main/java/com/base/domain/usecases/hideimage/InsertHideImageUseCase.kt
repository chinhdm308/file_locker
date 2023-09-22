package com.base.domain.usecases.hideimage

import com.base.domain.models.image.HideImage
import com.base.domain.repositories.HideImageRepository
import javax.inject.Inject

class InsertHideImageUseCase @Inject constructor(
    private val hideImageRepository: HideImageRepository
) {
    suspend fun execute(hideImage: HideImage): Long {
        return hideImageRepository.insertOrReplace(hideImage)
    }
}