package com.base.domain.usecases.groupvideo

import com.base.domain.models.video.GroupVideo
import com.base.domain.repositories.GroupVideoRepository
import javax.inject.Inject

class GetGroupVideosByBeyondGroupIdUseCase @Inject constructor(
    private val groupVideoRepository: GroupVideoRepository
) {
    suspend fun execute(beyondGroupId: Int): List<GroupVideo> {
        return groupVideoRepository.getGroupFilesByBeyondGroupId(beyondGroupId)
    }
}