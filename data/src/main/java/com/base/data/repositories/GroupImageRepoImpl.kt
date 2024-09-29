package com.base.data.repositories

import com.base.data.di.IoDispatcher
import com.base.data.local.database.groupimage.GroupImageDao
import com.base.data.local.database.groupimage.GroupImageMapper
import com.base.domain.models.image.GroupImage
import com.base.domain.repositories.GroupImageRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GroupImageRepoImpl @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val groupImageDao: GroupImageDao,
    private val groupImageMapper: GroupImageMapper
) : GroupImageRepository {
    override suspend fun insertOrReplace(groupImage: GroupImage, isReplacement: Boolean): Long {
        return withContext(ioDispatcher) {
            if (isReplacement) {
                groupImageDao.insertOrReplace(groupImageMapper.transform(groupImage))
            } else {
                groupImageDao.insert(groupImageMapper.transform(groupImage))
            }
        }
    }

    override suspend fun delete(groupImage: GroupImage): Int {
        return withContext(ioDispatcher) {
            groupImageDao.delete(groupImageMapper.transform(groupImage))
        }
    }

    override suspend fun getGroupImagesByBeyondGroupId(beyondGroupId: Int): List<GroupImage> {
        return withContext(ioDispatcher) {
            groupImageDao.getGroupImagesByBeyondGroupId(beyondGroupId).map {
                groupImageMapper.transform(it)
            }
        }
    }
}