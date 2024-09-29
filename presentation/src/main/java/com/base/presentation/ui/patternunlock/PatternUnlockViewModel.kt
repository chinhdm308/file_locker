package com.base.presentation.ui.patternunlock

import androidx.lifecycle.viewModelScope
import com.base.presentation.base.BaseViewModel
import com.base.data.local.datastore.DataStoreRepository
import com.base.domain.models.pattern.PatternDot
import com.base.domain.usecases.SetLastAppEnterPwdStateUseCase
import com.base.domain.usecases.pattern.GetPatternUseCase
import com.base.presentation.utils.fingerprint.FingerPrintHelper
import com.base.presentation.utils.helper.PatternChecker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PatternUnlockViewModel @Inject constructor(
    private val fingerPrintHelper: FingerPrintHelper,
    private val dataStoreRepository: DataStoreRepository,
    private val setLastAppEnterPwdStateUseCase: SetLastAppEnterPwdStateUseCase,
    private val getPatternUseCase: GetPatternUseCase,
) : BaseViewModel() {

    private val fingerPrintState = fingerPrintHelper.fingerPrintState
    private val patternDrawnState = MutableSharedFlow<List<PatternDot>>()

    private val _patternValidationViewState = MutableSharedFlow<OverlayViewState>()
    val patternValidationViewState get() = _patternValidationViewState.asSharedFlow()


    init {
        if (getFingerPrintEnabled()) {
            fingerPrintHelper.init()
        }

        val existingPatternObservable = getPatternUseCase.execute().map { it.pattern }

        combine(patternDrawnState, existingPatternObservable) { patternDraw, existingPattern ->
            val isValidated = PatternChecker.checkPatternsEqual(patternDraw, existingPattern)
            _patternValidationViewState.emit(
                OverlayViewState(
                    overlayValidateType = OverlayValidateType.TYPE_PATTERN,
                    isDrawnCorrect = isValidated,
                    isHiddenDrawingMode = getHiddenDrawingMode(),
                    isIntrudersCatcherMode = getIntrudersCatcherEnabled(),
                    isFingerPrintMode = getFingerPrintEnabled()
                )
            )
        }.launchIn(viewModelScope)

        fingerPrintState.onEach {
            _patternValidationViewState.emit(
                OverlayViewState(
                    overlayValidateType = OverlayValidateType.TYPE_FINGERPRINT,
                    fingerPrintResultData = it,
                    isHiddenDrawingMode = getHiddenDrawingMode(),
                    isIntrudersCatcherMode = getIntrudersCatcherEnabled(),
                    isFingerPrintMode = getFingerPrintEnabled()
                )
            )
        }.launchIn(viewModelScope)
    }

    override fun onCleared() {
        super.onCleared()
        fingerPrintHelper.onInactive()
    }

    fun getHiddenDrawingMode(): Boolean = runBlocking {
        dataStoreRepository.getHiddenDrawingMode()
    }

    fun onPatternDrawn(pattern: List<PatternDot>) = viewModelScope.launch {
        patternDrawnState.emit(pattern)
    }

    fun getFingerPrintEnabled() = runBlocking {
        dataStoreRepository.getFingerPrintEnabled()
    }

    fun getLastAppEnterCorrectPwd(): Boolean = runBlocking {
        dataStoreRepository.getLastAppEnterCorrectPwd()
    }

    fun getLastAppEnterPwdErrorCount(): Int = runBlocking {
        dataStoreRepository.getLastAppEnterPwdErrorCount()
    }

    fun getLastAppEnterPwdLeaverDateMilliseconds(): Long = runBlocking {
        dataStoreRepository.getLastAppEnterPwdLeaverDateMilliseconds()
    }

    fun getLastAppEnterPwdDelayTime(): Int = runBlocking {
        dataStoreRepository.getLastAppEnterPwdDelayTime()
    }

    fun setLastAppEnterPwdErrorCount(errorCount: Int) = runBlocking {
        dataStoreRepository.setLastAppEnterPwdErrorCount(errorCount)
    }

    fun setLastAppEnterPwdState(
        lastAppEnterCorrectPwd: Boolean,
        lastAppEnterPwdLeaverDateMilliseconds: Long,
        lastAppEnterPwdErrorCount: Int,
        lastAppEnterPwdDelayTime: Int,
    ) = viewModelScope.launch(NonCancellable) {
        setLastAppEnterPwdStateUseCase.execute(
            lastAppEnterCorrectPwd = lastAppEnterCorrectPwd,
            lastAppEnterPwdLeaverDateMilliseconds = lastAppEnterPwdLeaverDateMilliseconds,
            lastAppEnterPwdDelayTime = lastAppEnterPwdDelayTime,
            lastAppEnterPwdErrorCount = lastAppEnterPwdErrorCount
        )
    }

    fun getPlayWarringSoundState(): Boolean = runBlocking {
        dataStoreRepository.getPlayWarringSoundState()
    }

    fun getIntrudersCatcherEnabled(): Boolean = runBlocking {
        dataStoreRepository.getIntrudersCatcherEnabled()
    }

    fun getNumOfTimesEnterIncorrectPwd(): Int = runBlocking {
        dataStoreRepository.getNumOfTimesEnterIncorrectPwd()
    }
}