package com.base.data.repositories

import com.base.data.di.IoDispatcher
import com.base.data.local.dao.GroupAudioDao
import com.base.data.local.mapper.GroupAudioMapper
import com.base.domain.models.audio.GroupAudio
import com.base.domain.repositories.GroupAudioRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GroupAudioRepoImpl @Inject constructor(
    private val groupAudioDao: GroupAudioDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val groupAudioMapper: GroupAudioMapper,
) : GroupAudioRepository {
    override suspend fun delete(groupAudio: GroupAudio): Int {
        return withContext(ioDispatcher) {
            groupAudioDao.delete(groupAudioMapper.transform(groupAudio))
        }
    }

    override suspend fun getGroupFilesByBeyondGroupId(beyondGroupId: Int): List<GroupAudio> {
        return withContext(ioDispatcher) {
            groupAudioDao.getGroupFilesByBeyondGroupId(beyondGroupId).map {
                groupAudioMapper.transform(it)
            }
        }
    }

    override suspend fun insertOrReplace(groupAudio: GroupAudio, isReplacement: Boolean): Long {
        return withContext(ioDispatcher) {
            if (isReplacement) {
                groupAudioDao.insertOrReplace(groupAudioMapper.transform(groupAudio))
            } else {
                groupAudioDao.insert(groupAudioMapper.transform(groupAudio))
            }
        }
    }
}