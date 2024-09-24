package com.base.presentation.ui.setting

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.base.presentation.base.BaseViewModel
import com.base.presentation.utils.helper.CamouflageIconHelper
import com.base.data.local.datastore.DataStoreRepository
import com.wei.android.lib.fingerprintidentify.FingerprintIdentify
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    @ApplicationContext app: Context,
    private val dataStoreRepository: DataStoreRepository,
) : BaseViewModel() {

    private val fingerprintIdentify = FingerprintIdentify(app).apply {
        init()
    }

    private val _fingerPrintStatusViewState = MutableStateFlow(
        with(FingerprintIdentify(app)) {
            init()
            FingerPrintStatusViewState(
                isFingerPrintSupported = fingerprintIdentify.isHardwareEnable,
                isFingerPrintRegistered = fingerprintIdentify.isRegisteredFingerprint
            )
        }
    )
    val fingerPrintStatusViewState get() = _fingerPrintStatusViewState.asStateFlow()

    fun setEnableFingerPrint(fingerPrintEnabled: Boolean) {
        viewModelScope.launch {
            dataStoreRepository.setFingerPrintEnable(fingerPrintEnabled)
        }
    }

    fun isFingerPrintEnable(): Boolean = runBlocking {
        dataStoreRepository.isFingerPrintEnable()
    }

    fun isIntrudersCatcherEnabled(): Boolean = runBlocking {
        dataStoreRepository.isIntrudersCatcherEnable()
    }

    fun setEnableIntrudersCatchers(intruderCatcherEnabled: Boolean) {
        viewModelScope.launch {
            dataStoreRepository.setIntrudersCatcherEnable(intruderCatcherEnabled)
        }
    }

    fun getPlayWarringSoundState(): Boolean = runBlocking {
        dataStoreRepository.getPlayWarringSoundState()
    }

    fun setPlayWarringSoundState(state: Boolean) {
        viewModelScope.launch {
            dataStoreRepository.setPlayWarringSoundState(state)
        }
    }

    fun getNumOfTimesEnterIncorrectPwd(): Int = runBlocking {
        dataStoreRepository.getNumOfTimesEnterIncorrectPwd()
    }

    fun setNumOfTimesEnterIncorrectPwd(times: Int) {
        viewModelScope.launch {
            dataStoreRepository.setNumOfTimesEnterIncorrectPwd(times)
        }
    }

    fun setPreventUninstall(flag: Boolean) {
        viewModelScope.launch {
            dataStoreRepository.setPreventUninstall(flag)
        }
    }

    fun getPreventUninstall(): Boolean = runBlocking {
        dataStoreRepository.getPreventUninstall()
    }

    fun setCamouflageIconName(name: String) {
        runBlocking {
            dataStoreRepository.setCamouflageIconName(name)
        }
    }

    fun getCamouflageIconName(): String = runBlocking {
        val name = dataStoreRepository.getCamouflageIconName()
        name.ifEmpty {
            CamouflageIconHelper.CamouflageIconType.DEFAULT.key
        }
    }
}