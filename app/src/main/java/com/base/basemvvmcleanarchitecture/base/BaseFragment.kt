package com.base.basemvvmcleanarchitecture.base

import android.content.Context
import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<VB : ViewBinding>(
    private val inflate: FragmentBindingInflater<VB>
) : Fragment(), ViewInterface {

    private var _binding: VB? = null
    protected val binding: VB
        get() = _binding
            ?: throw IllegalStateException("Cannot access view after view destroyed or before view creation")

    private var lastTimeClick: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflate.invoke(inflater, container, false)
        return requireNotNull(_binding).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView(savedInstanceState)

        setOnClick()

        bindingStateView()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun setOnClick() {}

    override fun initView(savedInstanceState: Bundle?) {}

    override fun bindingStateView() {}

    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    protected fun isDoubleClick(): Boolean {
        if (SystemClock.elapsedRealtime() - lastTimeClick > 500) {
            lastTimeClick = SystemClock.elapsedRealtime()
            return false
        }
        return true
    }

    fun isLoading(isShow: Boolean) {
        if (activity != null && activity is BaseActivity<*>) {
            (activity as BaseActivity<*>?)!!.isLoading(isShow)
//            if (isShow) {
//                (activity as BaseActivity<*>?)!!.showLoading()
//            } else {
//                (activity as BaseActivity<*>?)!!.hiddenLoading()
//            }
        }
    }
}
