package com.base.data.repositories

import com.base.data.di.IoDispatcher
import com.base.data.local.database.hideimage.HideImageDao
import com.base.data.local.database.hideimage.HideImageMapper
import com.base.domain.models.image.HideImage
import com.base.domain.repositories.HideImageRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HideImageRepoImpl @Inject constructor(
    private val hideImageDao: HideImageDao,
    @IoDispatcher val ioDispatcher: CoroutineDispatcher,
    private val hideImageMapper: HideImageMapper
) : HideImageRepository {
    override suspend fun loadAll(): List<HideImage> {
        return withContext(ioDispatcher) {
            hideImageDao.loadAll().map {
                hideImageMapper.transform(it)
            }
        }
    }

    override suspend fun insertOrReplace(hideImage: HideImage): Long {
        return withContext(ioDispatcher) {
            hideImageDao.insertOrReplace(hideImageMapper.transform(hideImage))
        }
    }

    override suspend fun delete(hideImage: HideImage) {
        withContext(ioDispatcher) {
            hideImageDao.delete(hideImageMapper.transform(hideImage))
        }
    }

    override suspend fun getHideImagesByBeyondGroupId(beyondGroupId: Int): List<HideImage> {
        return withContext(ioDispatcher) {
            hideImageDao.getHideImagesByBeyondGroupId(beyondGroupId).map {
                hideImageMapper.transform(it)
            }
        }
    }
}