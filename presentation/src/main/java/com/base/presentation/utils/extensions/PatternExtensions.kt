package com.base.presentation.utils.extensions

import com.base.domain.models.pattern.PatternDot
import com.chinchin.patternlockview.PatternLockView

fun List<PatternLockView.Dot>.convertToPatternDot(): List<PatternDot> {
    val patternDotList: ArrayList<PatternDot> = arrayListOf()
    forEach {
        patternDotList.add(PatternDot(column = it.column, row = it.row))
    }
    return patternDotList
}