package com.base.presentation.ui.patterncreate

import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import com.base.presentation.R
import com.base.presentation.base.BaseActivity
import com.base.presentation.databinding.ActivityPatternLockBinding
import com.base.presentation.ui.main.MainActivity
import com.base.presentation.utils.AppConstants
import com.chinchin.patternlockview.PatternLockView
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PatternCreateActivity : BaseActivity<ActivityPatternLockBinding>(ActivityPatternLockBinding::inflate) {

    private val viewModel by viewModels<PatternCreateViewModel>()

    private var mode = AppConstants.RC_CREATE_PATTERN

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)

        window.setBackgroundDrawableResource(R.drawable.bg_gradient_main)
        onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {}
        })

        mode = intent.getStringExtra(AppConstants.EXTRA_PATTERN_MODE) ?: mode
    }

    override fun viewListener() {
        super.viewListener()

        binding.patternLockView.addPatternLockListener(object : SimplePatternListener() {
            override fun onComplete(pattern: MutableList<PatternLockView.Dot>?) {
                if (viewModel.isFirstPattern()) {
                    viewModel.setFirstDrawedPattern(pattern)
                } else {
                    viewModel.setRedrawnPattern(pattern)
                }
                binding.patternLockView.clearPattern()
            }
        })
    }

    override fun dataObservable() {
        super.dataObservable()

        viewModel.patternEventLiveData.observe(this) { viewState ->
            binding.textViewPrompt.text = viewState.getPromptText(this)

            if (viewState.isCreatedNewPattern()) {
                onPatternCreateCompleted()
            }
        }
    }

    private fun onPatternCreateCompleted() {
        if (mode == AppConstants.RC_CREATE_PATTERN) {
            startActivity(Intent(this, MainActivity::class.java))
        }
        finish()
    }
}