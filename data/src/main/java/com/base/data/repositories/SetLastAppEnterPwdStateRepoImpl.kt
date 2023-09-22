package com.base.data.repositories

import com.base.data.local.datastore.DataStoreRepository
import com.base.domain.repositories.SetLastAppEnterPwdStateRepo
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import java.util.Date
import javax.inject.Inject

class SetLastAppEnterPwdStateRepoImpl @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
) : SetLastAppEnterPwdStateRepo {
    override suspend fun setLastAppEnterPwdState(
        lastAppEnterCorrectPwd: Boolean,
        lastAppEnterPwdLeaverDateMilliseconds: Long,
        lastAppEnterPwdErrorCount: Int,
        lastAppEnterPwdDelayTime: Int,
    ) {
        Timber.e(
            "锁锁应用现在的状态,上次正确输入密码: $lastAppEnterCorrectPwd\n"
                    + "离开时间为：${Date(lastAppEnterPwdLeaverDateMilliseconds).toGMTString()}\n"
                    + "错误的次数: $lastAppEnterPwdErrorCount\n"
                    + "还有延迟这么多：$lastAppEnterPwdDelayTime"
        )
        dataStoreRepository.setLastAppEnterCorrectPwd(lastAppEnterCorrectPwd)
        dataStoreRepository.setLastAppEnterPwdLeaverDateMilliseconds(lastAppEnterPwdLeaverDateMilliseconds)
        dataStoreRepository.setLastAppEnterPwdDelayTime(lastAppEnterPwdDelayTime)
        dataStoreRepository.setLastAppEnterPwdErrorCount(lastAppEnterPwdErrorCount)
    }
}