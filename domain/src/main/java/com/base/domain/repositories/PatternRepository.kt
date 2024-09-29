package com.base.domain.repositories

import com.base.domain.models.pattern.PatternDotMetadata
import kotlinx.coroutines.flow.Flow

interface PatternRepository {
    suspend fun createPattern(patternDotMetadata: PatternDotMetadata)

    fun isPatternCreated(): Flow<Int>

    fun getPattern(): Flow<PatternDotMetadata>
}