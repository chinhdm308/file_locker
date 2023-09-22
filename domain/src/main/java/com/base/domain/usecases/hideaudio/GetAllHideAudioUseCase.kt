package com.base.domain.usecases.hideaudio

import com.base.domain.models.audio.HideAudio
import com.base.domain.repositories.HideAudioRepository
import javax.inject.Inject

class GetAllHideAudioUseCase @Inject constructor(
    private val hideAudioRepository: HideAudioRepository
) {
    suspend fun execute(): List<HideAudio> {
        return hideAudioRepository.loadAll()
    }
}