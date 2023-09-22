package com.base.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.base.data.di.IoDispatcher
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataStoreManager @Inject constructor(
    @ApplicationContext val context: Context,
    @IoDispatcher val ioDispatcher: CoroutineDispatcher,
) : DataStoreRepository {

    /**
     * Single data store instance
     */
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(dataStoreName)

    private val dataStore: DataStore<Preferences> by lazy {
        context.dataStore
    }

    companion object {
        private const val dataStoreName = "dataStore_AppName"

        private val keyToken = stringPreferencesKey("keyToken")
        private val keyNumPassword = stringPreferencesKey("keyNumPassword")
        private val keyAppFirstInstanceDone = booleanPreferencesKey("keyAppFirstInstanceDone")
        private val keyIsFingerPrintEnable = booleanPreferencesKey("keyIsFingerPrintEnable")
        private val keyIsIntrudersCatcherEnable =
            booleanPreferencesKey("keyIsIntrudersCatcherEnable")
        private val keyLastAppEnterCorrectPwd = booleanPreferencesKey("keyLastAppEnterCorrectPwd")
        private val keyLastAppEnterPwdErrorCount = intPreferencesKey("keyLastAppEnterPwdErrorCount")
        private val keyLastAppEnterPwdLeaverDateMilliseconds =
            longPreferencesKey("keyLastAppEnterPwdLeaverDateMilliseconds")
        private val keyLastAppEnterPwdDelayTime = intPreferencesKey("keyLastAppEnterPwdDelayTime")
        private val keyPlayWarringSoundState = booleanPreferencesKey("keyPlayWarringSoundState")
        private val keyNumOfTimesEnterIncorrectPwd =
            intPreferencesKey("keyNumOfTimesEnterIncorrectPwd")
        private val keyPreventUninstall = booleanPreferencesKey("keyPreventUninstall")
        private val keyCamouflageIconName = stringPreferencesKey("keyCamouflageIconName")

    }

    override suspend fun saveToken(token: String) = dataStore.put(keyToken, token)

    override suspend fun getToken(): String = dataStore.get(keyToken)

    override suspend fun clear() {
        dataStore.edit { it.clear() }
    }

    override suspend fun readNumPassword(): String {
        return withContext(ioDispatcher) {
            dataStore.get(keyNumPassword)
        }
    }

    override suspend fun editNumPassword(s: String) {
        withContext(ioDispatcher) {
            dataStore.put(keyNumPassword, s)
        }
    }

    override suspend fun getAppFirstSettingInstanceDone(): Boolean {
        return withContext(ioDispatcher) {
            dataStore.get(keyAppFirstInstanceDone)
        }
    }

    override suspend fun setAppFirstSettingInstanceDone(flag: Boolean) {
        withContext(ioDispatcher) {
            dataStore.put(keyAppFirstInstanceDone, flag)
        }
    }

    override suspend fun isFingerPrintEnable(): Boolean {
        return withContext(ioDispatcher) {
            dataStore.get(keyIsFingerPrintEnable)
        }
    }

    override suspend fun setFingerPrintEnable(flag: Boolean) {
        withContext(ioDispatcher) {
            dataStore.put(keyIsFingerPrintEnable, flag)
        }
    }

    override suspend fun isIntrudersCatcherEnable(): Boolean {
        return withContext(ioDispatcher) {
            dataStore.get(keyIsIntrudersCatcherEnable)
        }
    }

    override suspend fun setIntrudersCatcherEnable(flag: Boolean) {
        withContext(ioDispatcher) {
            dataStore.put(keyIsIntrudersCatcherEnable, flag)
        }
    }

    override suspend fun getLastAppEnterCorrectPwd(): Boolean {
        return withContext(ioDispatcher) {
            dataStore.get(keyLastAppEnterCorrectPwd)
        }
    }

    override suspend fun getLastAppEnterPwdErrorCount(): Int {
        return withContext(ioDispatcher) {
            dataStore.get(keyLastAppEnterPwdErrorCount)
        }
    }

    override suspend fun getLastAppEnterPwdLeaverDateMilliseconds(): Long {
        return withContext(ioDispatcher) {
            dataStore.get(keyLastAppEnterPwdLeaverDateMilliseconds)
        }
    }

    override suspend fun getLastAppEnterPwdDelayTime(): Int {
        return withContext(ioDispatcher) {
            dataStore.get(keyLastAppEnterPwdDelayTime)
        }
    }

    override suspend fun setLastAppEnterPwdErrorCount(errorCount: Int) {
        withContext(ioDispatcher) {
            dataStore.put(keyLastAppEnterPwdErrorCount, errorCount)
        }
    }

    override suspend fun setLastAppEnterCorrectPwd(flag: Boolean) {
        withContext(ioDispatcher) {
            dataStore.put(keyLastAppEnterCorrectPwd, flag)
        }
    }

    override suspend fun setLastAppEnterPwdLeaverDateMilliseconds(time: Long) {
        withContext(ioDispatcher) {
            dataStore.put(keyLastAppEnterPwdLeaverDateMilliseconds, time)
        }
    }

    override suspend fun setLastAppEnterPwdDelayTime(time: Int) {
        withContext(ioDispatcher) {
            dataStore.put(keyLastAppEnterPwdDelayTime, time)
        }
    }

    override suspend fun setPlayWarringSoundState(state: Boolean) {
        withContext(ioDispatcher) {
            dataStore.put(keyPlayWarringSoundState, state)
        }
    }

    override suspend fun getPlayWarringSoundState(): Boolean {
        return withContext(ioDispatcher) {
            dataStore.get(keyPlayWarringSoundState)
        }
    }

    override suspend fun setNumOfTimesEnterIncorrectPwd(errorCount: Int) {
        withContext(ioDispatcher) {
            dataStore.put(keyNumOfTimesEnterIncorrectPwd, errorCount)
        }
    }

    override suspend fun getNumOfTimesEnterIncorrectPwd(): Int {
        return withContext(ioDispatcher) {
            dataStore.get(keyNumOfTimesEnterIncorrectPwd)
        }
    }

    override suspend fun getPreventUninstall(): Boolean {
        return withContext(ioDispatcher) {
            dataStore.get(keyPreventUninstall)
        }
    }

    override suspend fun setPreventUninstall(flag: Boolean) {
        withContext(ioDispatcher) {
            dataStore.put(keyPreventUninstall, flag)
        }
    }

    override suspend fun getCamouflageIconName(): String {
        return withContext(ioDispatcher) {
            dataStore.get(keyCamouflageIconName)
        }
    }

    override suspend fun setCamouflageIconName(name: String) {
        withContext(ioDispatcher) {
            dataStore.put(keyCamouflageIconName, name)
        }
    }
}

