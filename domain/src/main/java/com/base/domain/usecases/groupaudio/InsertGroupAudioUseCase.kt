package com.base.domain.usecases.groupaudio

import com.base.domain.models.audio.GroupAudio
import com.base.domain.repositories.GroupAudioRepository
import javax.inject.Inject

class InsertGroupAudioUseCase @Inject constructor(
    private val groupAudioRepository: GroupAudioRepository
) {
    suspend fun execute(groupAudio: GroupAudio, isReplacement: Boolean): Long {
        return groupAudioRepository.insertOrReplace(groupAudio, isReplacement)
    }
}