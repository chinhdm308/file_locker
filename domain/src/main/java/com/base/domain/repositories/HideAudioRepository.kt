package com.base.domain.repositories

import com.base.domain.models.audio.HideAudio

interface HideAudioRepository {
    suspend fun loadAll(): List<HideAudio>

    suspend fun insertOrReplace(hideAudio: HideAudio): Long

    suspend fun delete(hideAudio: HideAudio)

    suspend fun getHideAudiosByBeyondGroupId(beyondGroupId: Int): List<HideAudio>
}