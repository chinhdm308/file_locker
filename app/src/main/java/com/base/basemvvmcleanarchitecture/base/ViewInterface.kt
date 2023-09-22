package com.base.basemvvmcleanarchitecture.base

import android.os.Bundle

interface ViewInterface {
    fun setOnClick()
    fun initView(savedInstanceState: Bundle?)
    fun bindingStateView()
}
