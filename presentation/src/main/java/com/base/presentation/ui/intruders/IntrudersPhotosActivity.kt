package com.base.presentation.ui.intruders

import android.os.Bundle
import androidx.activity.viewModels
import com.base.presentation.R
import com.base.presentation.base.BaseVMActivity
import com.base.presentation.databinding.ActivityIntrudersPhotosBinding
import com.base.presentation.utils.collectLifecycleFlow
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class IntrudersPhotosActivity :
    BaseVMActivity<ActivityIntrudersPhotosBinding, IntrudersPhotosViewModel>(
        ActivityIntrudersPhotosBinding::inflate
    ) {

    private val viewModel by viewModels<IntrudersPhotosViewModel>()

    @Inject
    lateinit var intrudersListAdapter: IntrudersListAdapter

    override fun getVM(): IntrudersPhotosViewModel {
        return viewModel
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        window.statusBarColor = getColor(R.color.color_appbar)

        binding.recyclerViewIntrudersPhotosList.adapter = intrudersListAdapter

        binding.btnBack.setOnClickListener { finish() }

        viewModel.loadIntruderPhotos()
    }

    override fun dataObservable() {
        super.dataObservable()

        collectLifecycleFlow(viewModel.intrudersViewState) { state ->
            Timber.d("size:${state.intruderPhotoItemViewStateList.size}")

            intrudersListAdapter.updateIntruderList(state.intruderPhotoItemViewStateList)

            binding.llEmptyPage.visibility = state.getEmptyPageVisibility()
        }
    }
}