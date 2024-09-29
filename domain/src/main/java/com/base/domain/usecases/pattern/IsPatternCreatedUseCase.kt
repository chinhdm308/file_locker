package com.base.domain.usecases.pattern

import com.base.domain.repositories.PatternRepository
import kotlinx.coroutines.flow.lastOrNull
import javax.inject.Inject

class IsPatternCreatedUseCase @Inject constructor(
    private val repository: PatternRepository,
) {
    suspend fun execute(): Boolean {
        return (repository.isPatternCreated().lastOrNull() ?: 0) > 0
    }
}