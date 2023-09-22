package com.base.basemvvmcleanarchitecture.base

import android.os.Bundle
import androidx.viewbinding.ViewBinding
import com.base.basemvvmcleanarchitecture.utils.collectLifecycleFlow
import timber.log.Timber

abstract class BaseVMActivity<VB : ViewBinding, VM : BaseViewModel>(
    inflate: ActivityBindingInflater<VB>
) : BaseActivity<VB>(inflate) {

    private lateinit var viewModel: VM

    abstract fun getVM(): VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = getVM()

        collectLifecycleFlow(viewModel.isLoading) {
            Timber.d("LOADING showLoading: $it")
            isLoading(it)
        }
    }
}
