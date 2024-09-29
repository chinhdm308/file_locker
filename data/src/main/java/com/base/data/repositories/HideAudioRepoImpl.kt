package com.base.data.repositories

import com.base.data.di.IoDispatcher
import com.base.data.local.database.hideaudio.HideAudioDao
import com.base.data.local.database.hideaudio.HideAudioMapper
import com.base.domain.models.audio.HideAudio
import com.base.domain.repositories.HideAudioRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HideAudioRepoImpl @Inject constructor(
    private val hideAudioDao: HideAudioDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val hideAudioMapper: HideAudioMapper
) : HideAudioRepository {
    override suspend fun loadAll(): List<HideAudio> {
        return withContext(ioDispatcher) {
            hideAudioDao.loadAll().map {
                hideAudioMapper.transform(it)
            }
        }
    }

    override suspend fun insertOrReplace(hideAudio: HideAudio): Long {
        return withContext(ioDispatcher) {
            hideAudioDao.insertOrReplace(hideAudioMapper.transform(hideAudio))
        }
    }

    override suspend fun delete(hideAudio: HideAudio) {
        withContext(ioDispatcher) {
            hideAudioDao.delete(hideAudioMapper.transform(hideAudio))
        }
    }

    override suspend fun getHideAudiosByBeyondGroupId(beyondGroupId: Int): List<HideAudio> {
        return withContext(ioDispatcher) {
            hideAudioDao.getHideAudiosByBeyondGroupId(beyondGroupId).map {
                hideAudioMapper.transform(it)
            }
        }
    }
}