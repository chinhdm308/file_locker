package com.base.data.repositories

import com.base.data.di.IoDispatcher
import com.base.data.local.database.groupfile.GroupFileDao
import com.base.data.local.database.groupfile.GroupFileMapper
import com.base.domain.models.file.GroupFile
import com.base.domain.repositories.GroupFileRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GroupFileRepoImpl @Inject constructor(
    private val groupFileDao: GroupFileDao,
    @IoDispatcher val ioDispatcher: CoroutineDispatcher,
    private val groupFileMapper: GroupFileMapper
) : GroupFileRepository {
    override suspend fun insertOrReplace(groupFile: GroupFile, isReplacement: Boolean): Long {
        return withContext(ioDispatcher) {
            if (isReplacement) {
                groupFileDao.insertOrReplace(groupFileMapper.transform(groupFile))
            } else {
                groupFileDao.insert(groupFileMapper.transform(groupFile))
            }
        }
    }

    override suspend fun delete(groupFile: GroupFile): Long {
        return withContext(ioDispatcher) {
            groupFileDao.delete(groupFileMapper.transform(groupFile)).toLong()
        }
    }

    override suspend fun getGroupFilesByBeyondGroupId(beyondGroupId: Int): List<GroupFile> {
        return withContext(ioDispatcher) {
            groupFileDao.getGroupFilesByBeyondGroupId(beyondGroupId).map {
                groupFileMapper.transform(it)
            }
        }
    }

}