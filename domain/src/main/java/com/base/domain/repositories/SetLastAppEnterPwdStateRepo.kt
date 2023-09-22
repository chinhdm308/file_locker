package com.base.domain.repositories

interface SetLastAppEnterPwdStateRepo {
    suspend fun setLastAppEnterPwdState(
        lastAppEnterCorrectPwd: Boolean,
        lastAppEnterPwdLeaverDateMilliseconds: Long,
        lastAppEnterPwdErrorCount: Int,
        lastAppEnterPwdDelayTime: Int
    )
}