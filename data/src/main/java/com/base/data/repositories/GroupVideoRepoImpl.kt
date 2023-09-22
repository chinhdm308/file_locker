package com.base.data.repositories

import com.base.data.di.IoDispatcher
import com.base.data.local.dao.GroupVideoDao
import com.base.data.local.mapper.GroupVideoMapper
import com.base.domain.models.video.GroupVideo
import com.base.domain.repositories.GroupVideoRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GroupVideoRepoImpl @Inject constructor(
    private val groupVideoDao: GroupVideoDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val groupVideoMapper: GroupVideoMapper,
) : GroupVideoRepository {
    override suspend fun delete(groupVideo: GroupVideo): Int {
        return withContext(ioDispatcher) {
            groupVideoDao.delete(groupVideoMapper.transform(groupVideo))
        }
    }

    override suspend fun getGroupFilesByBeyondGroupId(beyondGroupId: Int): List<GroupVideo> {
        return withContext(ioDispatcher) {
            groupVideoDao.getGroupFilesByBeyondGroupId(beyondGroupId).map {
                groupVideoMapper.transform(it)
            }
        }
    }

    override suspend fun insertOrReplace(groupVideo: GroupVideo, isReplacement: Boolean): Long {
        return withContext(ioDispatcher) {
            if (isReplacement) {
                groupVideoDao.insertOrReplace(groupVideoMapper.transform(groupVideo))
            } else {
                groupVideoDao.insert(groupVideoMapper.transform(groupVideo))
            }
        }
    }
}