package com.base.presentation.ui.main

import androidx.lifecycle.viewModelScope
import com.base.presentation.base.BaseViewModel
import com.base.domain.models.audio.HideAudio
import com.base.domain.models.file.HideFile
import com.base.domain.models.image.HideImage
import com.base.domain.models.video.HideVideo
import com.base.domain.usecases.hideaudio.GetAllHideAudioUseCase
import com.base.domain.usecases.hidefile.GetHideFilesUseCase
import com.base.domain.usecases.hideimage.GetAllHideImageUseCase
import com.base.domain.usecases.hidevideo.GetAllHideVideoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getAllHideImageUseCase: GetAllHideImageUseCase,
    private val getAllHideVideoUseCase: GetAllHideVideoUseCase,
    private val getAllHideAudioUseCase: GetAllHideAudioUseCase,
    private val getHideFilesUseCase: GetHideFilesUseCase
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(listOf(0, 0, 0, 0))
    val uiState get() = _uiState.asStateFlow()

    fun init() {
        viewModelScope.launch {
            val deferredImageSize = async { getHideImageCount() }
            val deferredVideoSize = async { getHideVideoCount() }
            val deferredAudioSize = async { getHideAudioCount() }
            val deferredFileSize = async { getHideFileCount() }
            val list =
                awaitAll(deferredImageSize, deferredVideoSize, deferredAudioSize, deferredFileSize)
            _uiState.emit(list)
        }
    }

    suspend fun getHideImageCount(): Int {
        val hideAudioList: List<HideImage> = getAllHideImageUseCase.execute()
        return hideAudioList.size
    }

    suspend fun getHideVideoCount(): Int {
        val hideVideoList: List<HideVideo> = getAllHideVideoUseCase.execute()
        return hideVideoList.size
    }

    suspend fun getHideAudioCount(): Int {
        val hideAudioList: List<HideAudio> = getAllHideAudioUseCase.execute()
        return hideAudioList.size
    }

    suspend fun getHideFileCount(): Int {
        val hideFileList: List<HideFile> = getHideFilesUseCase.execute()
        return hideFileList.size
    }
}