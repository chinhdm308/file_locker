package com.base.basemvvmcleanarchitecture.presentation.ui.intruders

import android.os.Bundle
import androidx.activity.viewModels
import com.base.basemvvmcleanarchitecture.R
import com.base.basemvvmcleanarchitecture.base.BaseActivity
import com.base.basemvvmcleanarchitecture.base.BaseVMActivity
import com.base.basemvvmcleanarchitecture.databinding.ActivityIntrudersPhotosBinding
import com.base.basemvvmcleanarchitecture.utils.collectLifecycleFlow
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
        window.statusBarColor = getColor(R.color.color_toolbar_hide_file)
        super.initView(savedInstanceState)

        binding.recyclerViewIntrudersPhotosList.adapter = intrudersListAdapter

        binding.btnBack.setOnClickListener { finish() }

        viewModel.loadIntruderPhotos()
    }

    override fun bindingStateView() {
        super.bindingStateView()

        collectLifecycleFlow(viewModel.intrudersViewState) { state ->
            Timber.d("size:${state.intruderPhotoItemViewStateList.size}")

            intrudersListAdapter.updateIntruderList(state.intruderPhotoItemViewStateList)

            binding.llEmptyPage.visibility = state.getEmptyPageVisibility()
        }
    }
}