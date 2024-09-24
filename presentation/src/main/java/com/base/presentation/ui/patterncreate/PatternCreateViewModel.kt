package com.base.presentation.ui.patterncreate

import androidx.lifecycle.viewModelScope
import com.base.presentation.base.BaseViewModel
import com.base.presentation.component.patternlockview.PatternViewState
import com.base.data.local.datastore.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PatternCreateViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
) : BaseViewModel() {

    private val _uiState = MutableStateFlow<PatternViewState>(PatternViewState.Initial)
    val uiState: StateFlow<PatternViewState>
        get() = _uiState.asStateFlow()

    private var firstStagePassword: String? = null
    private var secondStagePassword: String? = null

    fun updateViewState(viewState: PatternViewState) {
        viewModelScope.launch {
            _uiState.emit(viewState)
        }
    }

    fun setFirstStagePassword(password: String?) {
        firstStagePassword = password
    }

    fun setSecondStagePassword(password: String?) {
        secondStagePassword = password
        saveNewCreatedPattern()
    }

    private fun saveNewCreatedPattern() {
        viewModelScope.launch {
            dataStoreRepository.setAppFirstSettingInstanceDone(true)
            dataStoreRepository.editNumPassword(secondStagePassword!!)
            _uiState.emit(PatternViewState.NavigateMainScreen)
        }
    }

}