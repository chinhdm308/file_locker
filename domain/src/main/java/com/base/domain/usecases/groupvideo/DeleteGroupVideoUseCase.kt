package com.base.domain.usecases.groupvideo

import com.base.domain.models.video.GroupVideo
import com.base.domain.repositories.GroupVideoRepository
import javax.inject.Inject

class DeleteGroupVideoUseCase @Inject constructor(
    private val groupVideoRepository: GroupVideoRepository
) {
    suspend fun execute(groupVideo: GroupVideo): Int {
        return groupVideoRepository.delete(groupVideo)
    }
}