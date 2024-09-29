package com.base.presentation.ui.patterncreate

import android.content.Context
import com.base.presentation.R

data class CreateNewPatternViewState(val patternEvent: PatternEvent) {

    fun getPromptText(context: Context): String =
        when (patternEvent) {
            PatternEvent.INITIALIZE -> context.getString(R.string.draw_pattern_title)
            PatternEvent.FIRST_COMPLETED -> context.getString(R.string.redraw_pattern_title)
            PatternEvent.SECOND_COMPLETED -> context.getString(R.string.create_pattern_successful)
            PatternEvent.ERROR -> context.getString(R.string.recreate_pattern_error)
        }

    fun isCreatedNewPattern(): Boolean = patternEvent == PatternEvent.SECOND_COMPLETED
}