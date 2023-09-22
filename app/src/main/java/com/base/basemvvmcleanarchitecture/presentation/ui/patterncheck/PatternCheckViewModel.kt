package com.base.basemvvmcleanarchitecture.presentation.ui.patterncheck

import androidx.lifecycle.viewModelScope
import com.base.basemvvmcleanarchitecture.base.BaseViewModel
import com.base.basemvvmcleanarchitecture.component.patternlockview.PatternViewState
import com.base.basemvvmcleanarchitecture.utils.helper.file.DirectoryType
import com.base.basemvvmcleanarchitecture.utils.helper.file.FileExtension
import com.base.basemvvmcleanarchitecture.utils.helper.file.FileManager
import com.base.basemvvmcleanarchitecture.utils.helper.file.FileOperationRequest
import com.base.data.local.datastore.DataStoreRepository
import com.base.domain.usecases.SetLastAppEnterPwdStateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import java.io.File
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class PatternCheckViewModel @Inject constructor(
    private val fileManager: FileManager,
    private val dataStoreRepository: DataStoreRepository,
    private val setLastAppEnterPwdStateUseCase: SetLastAppEnterPwdStateUseCase
) : BaseViewModel() {

    private val _uiState = MutableStateFlow<PatternViewState>(PatternViewState.Initial)
    val uiState: StateFlow<PatternViewState>
        get() = _uiState.asStateFlow()

    fun updateViewState(viewState: PatternViewState) {
        viewModelScope.launch {
            _uiState.emit(viewState)
        }
    }

    fun isFingerPrintEnabled() = runBlocking {
        dataStoreRepository.isFingerPrintEnable()
    }

    fun readNumPassword(): String = runBlocking {
        dataStoreRepository.readNumPassword()
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
        lastAppEnterPwdDelayTime: Int
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

    fun isIntrudersCatcherEnable(): Boolean = runBlocking {
        dataStoreRepository.isIntrudersCatcherEnable()
    }

    fun getNumOfTimesEnterIncorrectPwd(): Int = runBlocking {
        dataStoreRepository.getNumOfTimesEnterIncorrectPwd()
    }
}