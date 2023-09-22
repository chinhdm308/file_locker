package com.base.domain.usecases.hidevideo

import com.base.domain.models.video.HideVideo
import com.base.domain.repositories.HideVideoRepository
import javax.inject.Inject

class GetHideVideosByBeyondGroupIdUseCase @Inject constructor(
    private val hideVideoRepository: HideVideoRepository
) {
    suspend fun execute(beyondGroupId: Int): List<HideVideo> {
        return hideVideoRepository.getHideVideosByBeyondGroupId(beyondGroupId)
    }
}