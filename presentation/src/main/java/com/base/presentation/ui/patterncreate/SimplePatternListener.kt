package com.base.presentation.ui.patterncreate

import com.chinchin.patternlockview.PatternLockView
import com.chinchin.patternlockview.listener.PatternLockViewListener

open class SimplePatternListener : PatternLockViewListener {
    override fun onComplete(pattern: MutableList<PatternLockView.Dot>?) {}

    override fun onCleared() {}

    override fun onStarted() {}

    override fun onProgress(progressPattern: MutableList<PatternLockView.Dot>?) {}
}