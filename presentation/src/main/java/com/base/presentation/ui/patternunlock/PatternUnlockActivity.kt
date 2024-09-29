package com.base.presentation.ui.patternunlock

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import com.base.presentation.R
import com.base.presentation.base.BaseActivity
import com.base.presentation.component.patternlockview.LockPatternUtils
import com.base.presentation.component.patternlockview.PatternViewStageState
import com.base.presentation.component.patternlockview.PatternViewState
import com.base.presentation.databinding.ActivityPatternLockBinding
import com.base.presentation.ui.main.MainActivity
import com.base.presentation.ui.patterncreate.SimplePatternListener
import com.base.presentation.utils.AppConstants
import com.base.presentation.utils.collectLifecycleFlow
import com.base.presentation.utils.extensions.convertToPatternDot
import com.base.presentation.utils.fingerprint.FingerPrintHelper
import com.base.presentation.utils.frontpicture.APictureCapturingService
import com.base.presentation.utils.frontpicture.PictureCapturingListener
import com.base.presentation.utils.frontpicture.PictureCapturingServiceImpl
import com.base.presentation.utils.helper.PlayWarringSoundHelper
import com.chinchin.patternlockview.PatternLockView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.Date
import java.util.TreeMap
import javax.inject.Inject


@AndroidEntryPoint
class PatternUnlockActivity : BaseActivity<ActivityPatternLockBinding>(ActivityPatternLockBinding::inflate) {

    @Inject
    lateinit var playWarringSoundHelper: PlayWarringSoundHelper

    private val viewModel by viewModels<PatternUnlockViewModel>()

    private val mShakeAnim: Animation by lazy {
        AnimationUtils.loadAnimation(this, R.anim.shake_x)
    }

    private val mHandler: Handler = Handler(Looper.getMainLooper())
    private val mHandlerClearPattern: Handler = Handler(Looper.getMainLooper())

    private var mFailedPatternAttemptsSinceLastTimeout = 0
    private var mCountdownTimer: CountDownTimer? = null
    private val delayTime = intArrayOf(60000, 120000, 180000, 600000, 1800000)
    private var errorCount = 0
    private var bPwdIsCorrect = true
    private var bIsFalseStart = false
    private var lastDelayTime = 0

    //The capture service
    private val pictureService: APictureCapturingService by lazy {
        PictureCapturingServiceImpl.getInstance(this)
    }

    private val attemptLockout = Runnable {
        Timber.e("errorCounter:$errorCount")
        binding.patternLockView.clearPattern()
        binding.patternLockView.isEnabled = false

        var millsInFuture: Long = 0
        if (bIsFalseStart) {
            bIsFalseStart = false
            val defaultTime: Long = Date().time - viewModel.getLastAppEnterPwdLeaverDateMilliseconds()
            if (defaultTime < viewModel.getLastAppEnterPwdDelayTime() * 1000) {
                millsInFuture = viewModel.getLastAppEnterPwdDelayTime() * 1000 - defaultTime
            }
        } else {
            millsInFuture = (delayTime[errorCount] + 1).toLong()
        }
        Timber.e("attemptLockout:$millsInFuture")

        mCountdownTimer = object : CountDownTimer(millsInFuture, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsRemaining = (millisUntilFinished / 1000).toInt() - 1
                lastDelayTime = secondsRemaining
                if (secondsRemaining > 0) {
                    val format = resources.getString(R.string.password_time)
                    val str = String.format(format, secondsRemaining)
                    binding.textHead.text = str
                } else {
                    binding.textHead.setText(R.string.password_gesture_tips)
                    binding.textHead.setTextColor(Color.WHITE)
                }
            }

            override fun onFinish() {
                binding.patternLockView.isEnabled = true
                mFailedPatternAttemptsSinceLastTimeout = 0
                errorCount += 1
                if (errorCount > 4) {
                    errorCount = 0
                }
            }
        }
        mCountdownTimer?.start()
    }

    private val mClearPatternRunnable = Runnable { binding.patternLockView.clearPattern() }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        window.setBackgroundDrawableResource(R.drawable.bg_gradient_main)

        binding.textViewFingerprint.isVisible = viewModel.getFingerPrintEnabled()

        binding.textViewPrompt.setText(R.string.overlay_prompt_pattern_title)

        bPwdIsCorrect = viewModel.getLastAppEnterCorrectPwd()
        errorCount = viewModel.getLastAppEnterPwdErrorCount()
        Timber.e("The status is:$bPwdIsCorrect\n" + "The last unlock password was wrong, the last time was:${viewModel.getLastAppEnterPwdDelayTime()}\n" + "The number of errors is:$errorCount")
        if (!bPwdIsCorrect) {
            bIsFalseStart = true
            val defaultTime: Long = Date().time - viewModel.getLastAppEnterPwdLeaverDateMilliseconds()
            Timber.e("The last unlock password was wrong. The time until now is:$defaultTime\n" + "The last time was:${viewModel.getLastAppEnterPwdDelayTime()}")
            if (defaultTime < viewModel.getLastAppEnterPwdDelayTime() * 1000) {
                Timber.e("The last unlock password was wrong")
                mHandler.postDelayed(attemptLockout, 100)
            } else {
                Timber.e("The last unlock password was wrong and the time has passed.")
                bIsFalseStart = false
                errorCount += 1
                if (errorCount > 4) {
                    errorCount = 0
                }
                viewModel.setLastAppEnterPwdErrorCount(errorCount)
            }
        }
    }

    override fun viewListener() {
        super.viewListener()

        onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {}
        })

        binding.patternLockView.addPatternLockListener(object : SimplePatternListener() {
            override fun onComplete(pattern: MutableList<PatternLockView.Dot>?) {
                super.onComplete(pattern)
                pattern?.let { viewModel.onPatternDrawn(it.convertToPatternDot()) }
            }
        })
    }

    override fun dataObservable() {
        super.dataObservable()

        collectLifecycleFlow(viewModel.patternValidationViewState) { state ->
            binding.patternLockView.clearPattern()

            if (state.isDrawnCorrect == true || state.fingerPrintResultData?.isSuccess() == true) {
                if (intent.getBooleanExtra(AppConstants.EXTRA_TO_APP, false).not() || isTaskRoot) {
                    startActivity(Intent(this, MainActivity::class.java))
                }
                finish()
            }

            if (state.isDrawnCorrect == false || state.fingerPrintResultData?.isNotSuccess() == true) {
                mFailedPatternAttemptsSinceLastTimeout++
                val retry = LockPatternUtils.FAILED_ATTEMPTS_BEFORE_TIMEOUT - mFailedPatternAttemptsSinceLastTimeout
                if (retry >= 0) {
                    if (retry == 0) {
                        Snackbar.make(binding.root, getPasswordErrorWait(), Snackbar.ANIMATION_MODE_SLIDE)
                            .setAnchorView(binding.view)
                            .show()
                    }
                    setErrorMessage(getString(R.string.password_error_count, retry))
                }

                if (mFailedPatternAttemptsSinceLastTimeout >= viewModel.getNumOfTimesEnterIncorrectPwd()) {
                    // Catch intruder
                    if (viewModel.getIntrudersCatcherEnabled()) {
                        pictureService.startCapturing(null)
                    }

                    //Play sound
                    if (viewModel.getPlayWarringSoundState()) {
                        playWarringSoundHelper.playSound()
                    }
                }

                if (mFailedPatternAttemptsSinceLastTimeout >= LockPatternUtils.FAILED_ATTEMPTS_BEFORE_TIMEOUT) {
                    mHandler.postDelayed(attemptLockout, 2000)
                } else {
                    mHandlerClearPattern.postDelayed(mClearPatternRunnable, 2000)
                }

                setErrorMessage(state.getPromptMessage(this))
            }
        }
    }

    override fun onStop() {
        super.onStop()
        viewModel.setLastAppEnterPwdState(bPwdIsCorrect, Date().time, errorCount, lastDelayTime)
    }

    override fun onDestroy() {
        viewModel.setLastAppEnterPwdState(bPwdIsCorrect, Date().time, errorCount, lastDelayTime)
        mCountdownTimer?.cancel()
        super.onDestroy()
    }

    private fun setErrorMessage(value: String) {
        binding.textViewPrompt.text = value
        binding.textViewPrompt.setTextColor(Color.RED)
        binding.textViewPrompt.startAnimation(mShakeAnim)
    }

    private fun getPasswordErrorWait(): String {
        return getString(R.string.password_error_wait, delayTime[errorCount] / 1000 / 60)
    }
}