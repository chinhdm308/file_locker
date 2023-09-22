package com.base.basemvvmcleanarchitecture.presentation.ui.patterncheck

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import com.base.basemvvmcleanarchitecture.R
import com.base.basemvvmcleanarchitecture.base.BaseActivity
import com.base.basemvvmcleanarchitecture.component.patternlockview.LockPatternUtils
import com.base.basemvvmcleanarchitecture.component.patternlockview.PatternViewStageState
import com.base.basemvvmcleanarchitecture.component.patternlockview.PatternViewState
import com.base.basemvvmcleanarchitecture.databinding.ActivityPatternLockBinding
import com.base.basemvvmcleanarchitecture.presentation.ui.main.MainActivity
import com.base.basemvvmcleanarchitecture.utils.AppConstants
import com.base.basemvvmcleanarchitecture.utils.collectLifecycleFlow
import com.base.basemvvmcleanarchitecture.utils.fingerprint.FingerPrintHelper
import com.base.basemvvmcleanarchitecture.utils.frontpicture.APictureCapturingService
import com.base.basemvvmcleanarchitecture.utils.frontpicture.FrontPictureHelper
import com.base.basemvvmcleanarchitecture.utils.frontpicture.PictureCapturingListener
import com.base.basemvvmcleanarchitecture.utils.frontpicture.PictureCapturingServiceImpl
import com.base.basemvvmcleanarchitecture.utils.helper.PlayWarringSoundHelper
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.Date
import java.util.TreeMap
import javax.inject.Inject


@AndroidEntryPoint
class PatternCheckActivity :
    BaseActivity<ActivityPatternLockBinding>(ActivityPatternLockBinding::inflate),
    PictureCapturingListener {

    @Inject
    lateinit var fingerPrintHelper: FingerPrintHelper

    @Inject
    lateinit var playWarringSoundHelper: PlayWarringSoundHelper

    private val viewModel by viewModels<PatternCheckViewModel>()

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
    private var pictureService: APictureCapturingService? = null

    override fun initView(savedInstanceState: Bundle?) {
        window.setBackgroundDrawableResource(R.drawable.bg_gradient_main)
        super.initView(savedInstanceState)

        binding.patternLockView.isEnabled = false

        if (viewModel.isFingerPrintEnabled()) {
            binding.textViewFingerprint.isVisible = true
            fingerPrintHelper.init()
        } else {
            binding.textViewFingerprint.isVisible = false
        }

        pictureService = PictureCapturingServiceImpl.getInstance(this)

        bPwdIsCorrect = viewModel.getLastAppEnterCorrectPwd()
        errorCount = viewModel.getLastAppEnterPwdErrorCount()
        Timber.e(
            "The status is:$bPwdIsCorrect\n" +
                    "The last unlock password was wrong, the last time was:${viewModel.getLastAppEnterPwdDelayTime()}\n" +
                    "The number of errors is:$errorCount"
        )
        if (!bPwdIsCorrect) {
            bIsFalseStart = true
            val defaultTime: Long =
                Date().time - viewModel.getLastAppEnterPwdLeaverDateMilliseconds()
            Timber.e(
                "The last unlock password was wrong. The time until now is:$defaultTime\n" +
                        "The last time was:${viewModel.getLastAppEnterPwdDelayTime()}"
            )
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

    override fun setOnClick() {
        super.setOnClick()

        onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {}
        })

        binding.patternLockView.setOnChangeStateListener { state ->
            viewModel.updateViewState(state)
        }
    }

    override fun bindingStateView() {
        super.bindingStateView()

        collectLifecycleFlow(viewModel.uiState) { patternViewState ->
            when (patternViewState) {
                is PatternViewState.Initial -> {
                    binding.patternLockView.reset()
                    binding.textViewPrompt.setText(R.string.overlay_prompt_pattern_title)
                }

                is PatternViewState.Started -> {}

                is PatternViewState.Success -> {
                    if (binding.patternLockView.getPassword(PatternViewStageState.FIRST) == viewModel.readNumPassword()) {
                        binding.textViewPrompt.setText(R.string.overlay_prompt_pattern_title_correct)
                        if (intent.getBooleanExtra(AppConstants.EXTRA_TO_APP, false)
                                .not() || isTaskRoot
                        ) {
                            startActivity(Intent(this, MainActivity::class.java))
                        }
                        finish()
                    } else {
                        mFailedPatternAttemptsSinceLastTimeout++
                        val retry =
                            LockPatternUtils.FAILED_ATTEMPTS_BEFORE_TIMEOUT - mFailedPatternAttemptsSinceLastTimeout
                        if (retry >= 0) {
                            if (retry == 0) {
                                Toast.makeText(
                                    this,
                                    getString(
                                        R.string.password_error_wait,
                                        delayTime[errorCount] / 1000 / 60
                                    ),
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                            val str = getString(R.string.password_error_count, retry)
                            binding.textHead.text = str
                            binding.textHead.setTextColor(Color.RED)
                            binding.textHead.startAnimation(mShakeAnim)
                        }

                        if (mFailedPatternAttemptsSinceLastTimeout >= viewModel.getNumOfTimesEnterIncorrectPwd()) {
                            // Catch intruder
                            if (viewModel.isIntrudersCatcherEnable()) {
                                pictureService?.startCapturing(this)
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

//                        setErrorMessage(R.string.overlay_prompt_pattern_title_wrong)
//                        binding.patternLockView.reset()
                    }
                }

                is PatternViewState.Error -> {
                    Toast.makeText(
                        this,
                        getString(R.string.password_short),
                        Toast.LENGTH_LONG
                    ).show()
//                    setErrorMessage(R.string.error_message_first_stage)
                }

                else -> {}
            }
        }

        collectLifecycleFlow(fingerPrintHelper.fingerPrintState) {
            if (it.isSuccess()) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
    }

    override fun onStop() {
        fingerPrintHelper.onInactive()
        viewModel.setLastAppEnterPwdState(bPwdIsCorrect, Date().time, errorCount, lastDelayTime)
        viewModel.updateViewState(PatternViewState.Initial)
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()

        viewModel.setLastAppEnterPwdState(bPwdIsCorrect, Date().time, errorCount, lastDelayTime)
        mCountdownTimer?.cancel()
    }

    override fun onCaptureDone(pictureUrl: String?, pictureData: ByteArray?) {

    }

    override fun onDoneCapturingAllPhotos(picturesTaken: TreeMap<String, ByteArray>?) {

    }

    private fun setErrorMessage(@StringRes res: Int) {
        binding.textViewPrompt.setText(res)
        binding.textViewPrompt.setTextColor(Color.RED)
        binding.textViewPrompt.startAnimation(mShakeAnim)
    }

    private var attemptLockout = Runnable {
        Timber.e("errorCounter:$errorCount")
        binding.patternLockView.reset()
        binding.patternLockView.isEnabled = false

        var millsInFuture: Long = 0
        if (bIsFalseStart) {
            bIsFalseStart = false
            val defaultTime: Long =
                Date().time - viewModel.getLastAppEnterPwdLeaverDateMilliseconds()
            if (defaultTime < viewModel.getLastAppEnterPwdDelayTime() * 1000
            ) {
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
                    val format = resources.getString(
                        R.string.password_time
                    )
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
        }.start()
    }

    private val mClearPatternRunnable = Runnable { binding.patternLockView.reset() }
}