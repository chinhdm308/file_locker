package com.base.domain.usecases.hidevideo

import com.base.domain.models.video.HideVideo
import com.base.domain.repositories.HideVideoRepository
import javax.inject.Inject

class InsertHideVideoUseCase @Inject constructor(
    private val hideVideoRepository: HideVideoRepository
) {
    suspend fun execute(hideVideo: HideVideo): Long {
        return hideVideoRepository.insertOrReplace(hideVideo)
    }
}