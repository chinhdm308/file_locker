package com.base.domain.usecases.groupaudio

import com.base.domain.models.audio.GroupAudio
import com.base.domain.repositories.GroupAudioRepository
import javax.inject.Inject

class GetGroupAudiosByBeyondGroupIdUseCase @Inject constructor(
    private val groupAudioRepository: GroupAudioRepository
) {
    suspend fun execute(beyondGroupId: Int): List<GroupAudio> {
        return groupAudioRepository.getGroupFilesByBeyondGroupId(beyondGroupId)
    }
}