package com.base.basemvvmcleanarchitecture.presentation.ui.intruders

import androidx.lifecycle.viewModelScope
import com.base.basemvvmcleanarchitecture.base.BaseViewModel
import com.base.basemvvmcleanarchitecture.utils.helper.file.FileExtension
import com.base.basemvvmcleanarchitecture.utils.helper.file.FileManager
import com.base.data.di.IoDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File
import javax.inject.Inject

@HiltViewModel
class IntrudersPhotosViewModel @Inject constructor(
    @IoDispatcher val ioDispatcher: CoroutineDispatcher,
    private val fileManager: FileManager,
) : BaseViewModel() {

    private val _intruderListViewState = MutableSharedFlow<IntrudersViewState>()
    val intrudersViewState get() = _intruderListViewState.asSharedFlow()

    fun loadIntruderPhotos() {
        viewModelScope.launch {
            isLoading(true)
            val subFiles = withContext(ioDispatcher) {
                fileManager.getSubFiles(
                    fileManager.getExternalDirectory(FileManager.SubFolder.INTRUDERS),
                    FileExtension.JPEG
                )
            }
            Timber.d("size:${subFiles.size}")
            isLoading(false)
            _intruderListViewState.emit(IntrudersViewState(mapToViewState(subFiles)))
        }
    }

    private fun mapToViewState(files: List<File>): List<IntruderPhotoItemViewState> {
        val viewStateList = arrayListOf<IntruderPhotoItemViewState>()
        files.forEach { viewStateList.add((IntruderPhotoItemViewState(it))) }
        return viewStateList
    }
}