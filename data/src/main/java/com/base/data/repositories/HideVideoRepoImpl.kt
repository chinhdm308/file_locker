package com.base.data.repositories

import com.base.data.di.IoDispatcher
import com.base.data.local.dao.HideVideoDao
import com.base.data.local.mapper.HideVideoMapper
import com.base.domain.models.video.HideVideo
import com.base.domain.repositories.HideVideoRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HideVideoRepoImpl @Inject constructor(
    private val hideVideoDao: HideVideoDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val hideVideoMapper: HideVideoMapper
) : HideVideoRepository {
    override suspend fun loadAll(): List<HideVideo> {
        return withContext(ioDispatcher) {
            hideVideoDao.loadAll().map {
                hideVideoMapper.transform(it)
            }
        }
    }

    override suspend fun insertOrReplace(hideVideo: HideVideo): Long {
        return withContext(ioDispatcher) {
            hideVideoDao.insertOrReplace(hideVideoMapper.transform(hideVideo))
        }
    }

    override suspend fun delete(hideVideo: HideVideo) {
        withContext(ioDispatcher) {
            hideVideoDao.delete(hideVideoMapper.transform(hideVideo))
        }
    }

    override suspend fun getHideVideosByBeyondGroupId(beyondGroupId: Int): List<HideVideo> {
        return withContext(ioDispatcher) {
            hideVideoDao.getHideAudiosByBeyondGroupId(beyondGroupId).map {
                hideVideoMapper.transform(it)
            }
        }
    }
}