package com.base.data.repositories

import com.base.data.di.IoDispatcher
import com.base.data.local.database.pattern.PatternDao
import com.base.data.local.database.pattern.PatternEntity
import com.base.domain.models.pattern.PatternDotMetadata
import com.base.domain.repositories.PatternRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PatternRepoImpl @Inject constructor(
    private val patternDao: PatternDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : PatternRepository {

    override suspend fun createPattern(patternDotMetadata: PatternDotMetadata) {
        withContext(ioDispatcher) {
            patternDao.createPattern(PatternEntity(patternDotMetadata))
        }
    }

    override fun isPatternCreated(): Flow<Int> {
        return patternDao.isPatternCreated().flowOn(ioDispatcher)
    }

    override fun getPattern(): Flow<PatternDotMetadata> {
        return patternDao.getPattern()
            .map { it.patternMetadata }
            .flowOn(ioDispatcher)
    }
}