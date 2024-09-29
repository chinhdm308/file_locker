package com.base.domain.usecases.pattern

import com.base.domain.models.pattern.PatternDotMetadata
import com.base.domain.repositories.PatternRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPatternUseCase @Inject constructor(
    private val patternRepository: PatternRepository,
) {
    fun execute(): Flow<PatternDotMetadata> {
        return patternRepository.getPattern()
    }
}