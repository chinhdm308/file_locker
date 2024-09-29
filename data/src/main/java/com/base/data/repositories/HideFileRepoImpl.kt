package com.base.data.repositories

import com.base.data.di.IoDispatcher
import com.base.data.local.database.hidefile.HideFileDao
import com.base.data.local.database.hidefile.HideFileMapper
import com.base.domain.models.file.HideFile
import com.base.domain.repositories.HideFileRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HideFileRepoImpl @Inject constructor(
    private val hideFileDao: HideFileDao,
    @IoDispatcher val ioDispatcher: CoroutineDispatcher,
    private val hideFileMapper: HideFileMapper
): HideFileRepository{

    override suspend fun insertOrReplace(hideFile: HideFile, isReplacement: Boolean): Long {
        return withContext(ioDispatcher) {
            if (isReplacement) {
                hideFileDao.insertOrReplace(hideFileMapper.transform(hideFile))
            } else {
                hideFileDao.insert(hideFileMapper.transform(hideFile))
            }
        }
    }

    override suspend fun delete(hideFile: HideFile): Long {
        return withContext(ioDispatcher) {
            hideFileDao.delete(hideFileMapper.transform(hideFile)).toLong()
        }
    }

    override suspend fun loadAll(): List<HideFile> {
        return withContext(ioDispatcher) {
            hideFileDao.loadAll().map {
                hideFileMapper.transform(it)
            }
        }
    }

    override suspend fun getHideFilesByBeyondGroupId(beyondGroupId: Int): List<HideFile> {
        return withContext(ioDispatcher) {
            hideFileDao.getHideFilesByBeyondGroupId(beyondGroupId).map {
                hideFileMapper.transform(it)
            }
        }
    }
}