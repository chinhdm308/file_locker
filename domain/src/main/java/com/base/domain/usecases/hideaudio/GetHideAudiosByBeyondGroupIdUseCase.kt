package com.base.domain.usecases.hideaudio

import com.base.domain.models.audio.HideAudio
import com.base.domain.repositories.HideAudioRepository
import javax.inject.Inject

class GetHideAudiosByBeyondGroupIdUseCase @Inject constructor(
    private val hideAudioRepository: HideAudioRepository
) {
    suspend fun execute(beyondGroupId: Int): List<HideAudio> {
        return hideAudioRepository.getHideAudiosByBeyondGroupId(beyondGroupId)
    }
}