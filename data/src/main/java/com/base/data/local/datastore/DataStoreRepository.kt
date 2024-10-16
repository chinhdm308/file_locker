package com.base.data.local.datastore

import com.base.domain.models.pattern.PatternDotMetadata
import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {

    suspend fun setFirstLanguage()

    suspend fun getFirstLanguage(): Boolean

    suspend fun saveToken(token: String)

    suspend fun getToken(): String

    suspend fun clear()

    suspend fun getAppFirstSettingInstanceDone(): Boolean

    suspend fun setAppFirstSettingInstanceDone(flag: Boolean)

    suspend fun getFingerPrintEnabled(): Boolean

    suspend fun setFingerPrintEnable(flag: Boolean)

    suspend fun getIntrudersCatcherEnabled(): Boolean

    suspend fun setIntrudersCatcherEnable(flag: Boolean)

    suspend fun getLastAppEnterCorrectPwd(): Boolean

    suspend fun getLastAppEnterPwdErrorCount(): Int

    suspend fun getLastAppEnterPwdLeaverDateMilliseconds(): Long

    suspend fun getLastAppEnterPwdDelayTime(): Int

    suspend fun setLastAppEnterPwdErrorCount(errorCount: Int)

    suspend fun setLastAppEnterCorrectPwd(flag: Boolean)

    suspend fun setLastAppEnterPwdLeaverDateMilliseconds(time: Long)

    suspend fun setLastAppEnterPwdDelayTime(time: Int)

    suspend fun setPlayWarringSoundState(state: Boolean)

    suspend fun getPlayWarringSoundState(): Boolean

    suspend fun setNumOfTimesEnterIncorrectPwd(errorCount: Int)

    suspend fun getNumOfTimesEnterIncorrectPwd(): Int

    suspend fun getPreventUninstall(): Boolean

    suspend fun setPreventUninstall(flag: Boolean)

    suspend fun getCamouflageIconName(): String

    suspend fun setCamouflageIconName(name: String)

    suspend fun getHiddenDrawingMode(): Boolean

    suspend fun setHiddenDrawingMode(hiddenDrawingPatternMode: Boolean)

    suspend fun savePattern(pattern: PatternDotMetadata)

    fun getPatternObservable(): Flow<PatternDotMetadata>

    suspend fun getPattern(): PatternDotMetadata
}