package com.base.domain.usecases.hidevideo

import com.base.domain.models.video.HideVideo
import com.base.domain.repositories.HideVideoRepository
import javax.inject.Inject

class GetAllHideVideoUseCase @Inject constructor(
    private val hideVideoRepository: HideVideoRepository
) {
    suspend fun execute(): List<HideVideo> {
        return hideVideoRepository.loadAll()
    }
}