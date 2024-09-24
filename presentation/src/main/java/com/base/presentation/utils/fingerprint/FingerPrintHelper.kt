package com.base.presentation.utils.fingerprint

import android.content.Context
import com.wei.android.lib.fingerprintidentify.FingerprintIdentify
import com.wei.android.lib.fingerprintidentify.base.BaseFingerprint
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class FingerPrintHelper @Inject constructor(
    @ApplicationContext context: Context
) : BaseFingerprint.ExceptionListener, CoroutineScope by CoroutineScope(Dispatchers.Default) {

    private val fingerprintIdentify: FingerprintIdentify = FingerprintIdentify(context)

    private val _fingerPrintState = MutableSharedFlow<FingerPrintResultData>()
    val fingerPrintState get() = _fingerPrintState.asSharedFlow()

    fun init() {
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
        launch {
            _fingerPrintState.emit(state)
        }
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