package com.base.presentation.ui.patterncreate

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.base.data.local.datastore.DataStoreRepository
import com.base.domain.models.pattern.PatternDotMetadata
import com.base.presentation.base.BaseViewModel
import com.base.presentation.utils.extensions.convertToPatternDot
import com.base.presentation.utils.helper.PatternChecker
import com.chinchin.patternlockview.PatternLockView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class PatternCreateViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
) : BaseViewModel() {

    private val _patternEventLiveData = MutableLiveData<CreateNewPatternViewState>().apply {
        value = CreateNewPatternViewState(PatternEvent.INITIALIZE)
    }
    val patternEventLiveData: LiveData<CreateNewPatternViewState> get() = _patternEventLiveData

    private var firstDrawedPattern: ArrayList<PatternLockView.Dot> = arrayListOf()
    private var reDrawedPattern: ArrayList<PatternLockView.Dot> = arrayListOf()


    fun setFirstDrawedPattern(pattern: List<PatternLockView.Dot>?) {
        pattern?.let {
            firstDrawedPattern.clear()
            firstDrawedPattern.addAll(pattern)
            _patternEventLiveData.value = CreateNewPatternViewState(PatternEvent.FIRST_COMPLETED)
        }
    }

    fun setRedrawnPattern(pattern: List<PatternLockView.Dot>?) {
        pattern?.let {
            reDrawedPattern.clear()
            reDrawedPattern.addAll(pattern)
            if (PatternChecker.checkPatternsEqual(firstDrawedPattern.convertToPatternDot(), reDrawedPattern.convertToPatternDot())) {
                saveNewCreatedPattern(firstDrawedPattern)
                _patternEventLiveData.value = CreateNewPatternViewState(PatternEvent.SECOND_COMPLETED)
            } else {
                firstDrawedPattern.clear()
                reDrawedPattern.clear()
                _patternEventLiveData.value = CreateNewPatternViewState(PatternEvent.ERROR)
            }
        }
    }

    fun isFirstPattern(): Boolean = firstDrawedPattern.isEmpty()

    private fun saveNewCreatedPattern(pattern: List<PatternLockView.Dot>) = runBlocking {
        val patternMetadata = PatternDotMetadata(pattern.convertToPatternDot())
        dataStoreRepository.savePattern(patternMetadata)
        dataStoreRepository.setAppFirstSettingInstanceDone(true)
    }

}