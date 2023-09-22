package com.base.domain.usecases.groupvideo

import com.base.domain.models.video.GroupVideo
import com.base.domain.repositories.GroupVideoRepository
import javax.inject.Inject

class InsertGroupVideoUseCase @Inject constructor(
    private val groupVideoRepository: GroupVideoRepository
) {
    suspend fun execute(groupVideo: GroupVideo, isReplacement: Boolean): Long {
        return groupVideoRepository.insertOrReplace(groupVideo, isReplacement)
    }
}