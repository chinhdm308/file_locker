package com.base.basemvvmcleanarchitecture.presentation.ui.patterncreate

import android.content.Intent
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.base.basemvvmcleanarchitecture.R
import com.base.basemvvmcleanarchitecture.component.patternlockview.PatternViewStageState
import com.base.basemvvmcleanarchitecture.component.patternlockview.PatternViewState
import com.base.basemvvmcleanarchitecture.databinding.ActivityPatternLockBinding
import com.base.basemvvmcleanarchitecture.presentation.ui.main.MainActivity
import com.base.basemvvmcleanarchitecture.utils.collectLifecycleFlow
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PatternCreateActivity : AppCompatActivity() {

    private val viewModel by viewModels<PatternCreateViewModel>()

    private lateinit var binding: ActivityPatternLockBinding

    private val mShakeAnim: Animation by lazy {
        AnimationUtils.loadAnimation(this, R.anim.shake_x)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setBackgroundDrawableResource(R.drawable.bg_gradient_main)

        binding = ActivityPatternLockBinding.inflate(layoutInflater)
        setContentView(binding.root)

        onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {}
        })

        binding.patternLockView.setOnChangeStateListener { state ->
            viewModel.updateViewState(state)
        }

        collectLifecycleFlow(viewModel.uiState) { patternViewState ->
            when (patternViewState) {
                is PatternViewState.Initial -> {
                    binding.patternLockView.reset()
//                    binding.tvMessage.run {
//                        text = if (binding.patternLockView.stageState == PatternViewStageState.FIRST) {
//                            getString(R.string.initial_message_first_stage)
//                        } else {
//                            getString(R.string.initial_message_second_stage)
//                        }
//                        setTextColor(
//                            ContextCompat.getColor(
//                                context,
//                                R.color.message_text_default_color
//                            )
//                        )
//                    }

                    binding.textViewPrompt.run {
                        text =
                            if (binding.patternLockView.stageState == PatternViewStageState.FIRST) {
                                getString(R.string.draw_pattern_title)
                            } else {
                                getString(R.string.redraw_pattern_title)
                            }
                    }
                }

                is PatternViewState.Started -> {
//                    binding.tvMessage.run {
//                        text = getString(R.string.started_message)
//                        setTextColor(
//                            ContextCompat.getColor(
//                                context,
//                                R.color.message_text_default_color
//                            )
//                        )
//                    }
                }

                is PatternViewState.Success -> {
//                    binding.tvMessage.run {
//                        text = if (binding.patternLockView.stageState == PatternViewStageState.FIRST) {
//                            getString(R.string.success_message_first_stage)
//                        } else {
//                            getString(R.string.success_message_second_stage)
//                        }
//                        setTextColor(
//                            ContextCompat.getColor(
//                                context,
//                                R.color.message_text_default_color
//                            )
//                        )
//                    }

                    if (binding.patternLockView.stageState == PatternViewStageState.SECOND) {
                        binding.textViewPrompt.setText(R.string.create_pattern_successful)
                    }

                    if (binding.patternLockView.stageState == PatternViewStageState.FIRST) {
                        viewModel.setFirstStagePassword(
                            binding.patternLockView.getPassword(
                                PatternViewStageState.FIRST
                            )
                        )
                    } else {
                        viewModel.setSecondStagePassword(
                            binding.patternLockView.getPassword(
                                PatternViewStageState.SECOND
                            )
                        )
                    }

                    if (binding.patternLockView.stageState == PatternViewStageState.FIRST) {
                        binding.patternLockView.stageState = PatternViewStageState.SECOND
                        viewModel.updateViewState(PatternViewState.Initial)
                    }
                }

                is PatternViewState.Error -> {
//                    binding.tvMessage.run {
//                        text = if (binding.patternLockView.stageState == PatternViewStageState.FIRST) {
//                            getString(R.string.error_message_first_stage)
//                        } else {
//                            getString(R.string.error_message_second_stage)
//                        }
//                        setTextColor(
//                            ContextCompat.getColor(
//                                context,
//                                R.color.message_text_error_color
//                            )
//                        )
//                    }

                    binding.textViewPrompt.run {
                        text =
                            if (binding.patternLockView.stageState == PatternViewStageState.FIRST) {
                                getString(R.string.error_message_first_stage)
                            } else {
                                getString(R.string.recreate_pattern_error)
                            }
                    }
                }

                is PatternViewState.NavigateMainScreen -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            }
        }
    }
}