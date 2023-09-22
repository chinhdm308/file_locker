package com.base.domain.usecases.groupaudio

import com.base.domain.models.audio.GroupAudio
import com.base.domain.repositories.GroupAudioRepository
import javax.inject.Inject

class DeleteGroupAudioUseCase @Inject constructor(
    private val groupAudioRepository: GroupAudioRepository
) {
    suspend fun execute(groupAudio: GroupAudio): Int {
        return groupAudioRepository.delete(groupAudio)
    }
}