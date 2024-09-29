package com.base.presentation.utils.fingerprint

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.base.data.local.datastore.DataStoreRepository
import com.base.presentation.utils.AppConstants
import com.wei.android.lib.fingerprintidentify.FingerprintIdentify
import com.wei.android.lib.fingerprintidentify.base.BaseFingerprint
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import javax.inject.Inject

class FingerPrintHelper @Inject constructor(
    @ApplicationContext context: Context,
    private val dataStoreRepository: DataStoreRepository,
) : BaseFingerprint.ExceptionListener, CoroutineScope by CoroutineScope(Dispatchers.Default) {

    private val fingerprintIdentify: FingerprintIdentify = FingerprintIdentify(context)

    private val _fingerPrintState = MutableSharedFlow<FingerPrintResultData>()
    val fingerPrintState get() = _fingerPrintState.asSharedFlow()

    private var maxAvailableTimes: Int = AppConstants.DEFAULT_INCORRECT_TIMES

    fun init() {
        maxAvailableTimes = runBlocking {
            dataStoreRepository.getNumOfTimesEnterIncorrectPwd()
        }

        try {
            fingerprintIdentify.setSupportAndroidL(true)
            fingerprintIdentify.init()
            onActive()
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }

    override fun onCatchException(exception: Throwable?) {
        FingerPrintResultData.error(exception?.message ?: "")
    }

    fun pushState(state: FingerPrintResultData) {
        launch { _fingerPrintState.emit(state) }
    }

    private fun onActive() {
        fingerprintIdentify.startIdentify(3, object : BaseFingerprint.IdentifyListener {
            override fun onSucceed() {
                Timber.d("onSucceed")
                pushState(FingerPrintResultData.matched())
            }

            override fun onNotMatch(availableTimes: Int) {
                Timber.d("onNotMatch: $availableTimes")
                pushState(FingerPrintResultData.notMatched(availableTimes))
            }

            override fun onFailed(isDeviceLocked: Boolean) {
                Timber.d("Fingerprint error")
                pushState(FingerPrintResultData.error("Fingerprint error"))
            }

            override fun onStartFailedByDeviceLocked() {
                Timber.d("Fingerprint error")
                pushState(FingerPrintResultData.error("Fingerprint error"))
            }
        })
    }

    fun onInactive() {
        fingerprintIdentify.cancelIdentify()
    }
}