package com.base.domain.usecases.pattern

import com.base.domain.models.pattern.PatternDotMetadata
import com.base.domain.repositories.PatternRepository
import javax.inject.Inject

class CreatePatternUseCase @Inject constructor(
    private val repository: PatternRepository
) {
    suspend fun execute(patternDotMetadata: PatternDotMetadata) {
        repository.createPattern(patternDotMetadata)
    }
}