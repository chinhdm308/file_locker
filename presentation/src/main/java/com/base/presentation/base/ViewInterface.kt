package com.base.presentation.base

import android.os.Bundle

interface ViewInterface {
    fun viewListener()
    fun initView(savedInstanceState: Bundle?)
    fun dataObservable()
}
