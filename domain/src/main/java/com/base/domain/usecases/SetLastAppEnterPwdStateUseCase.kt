package com.base.domain.usecases

import com.base.domain.repositories.SetLastAppEnterPwdStateRepo
import javax.inject.Inject

class SetLastAppEnterPwdStateUseCase @Inject constructor(
    private val setLastAppEnterPwdStateRepo: SetLastAppEnterPwdStateRepo
) {
    suspend fun execute(
        lastAppEnterCorrectPwd: Boolean,
        lastAppEnterPwdLeaverDateMilliseconds: Long,
        lastAppEnterPwdErrorCount: Int,
        lastAppEnterPwdDelayTime: Int
    ) {
        setLastAppEnterPwdStateRepo.setLastAppEnterPwdState(
            lastAppEnterCorrectPwd,
            lastAppEnterPwdLeaverDateMilliseconds,
            lastAppEnterPwdErrorCount,
            lastAppEnterPwdDelayTime
        )
    }
}