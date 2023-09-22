package com.base.basemvvmcleanarchitecture.base

import android.os.Bundle
import androidx.viewbinding.ViewBinding
import com.base.basemvvmcleanarchitecture.utils.collectLifecycleFlow
import timber.log.Timber

abstract class BaseVMFragment<VB : ViewBinding, VM : BaseViewModel>(
    inflate: FragmentBindingInflater<VB>
) : BaseFragment<VB>(inflate) {
    private lateinit var viewModel: VM

    abstract fun getVM(): VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = getVM()
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)

        collectLifecycleFlow(viewModel.isLoading) {
            Timber.d("LOADING showLoading: $it")
            isLoading(it)
        }
    }
}
