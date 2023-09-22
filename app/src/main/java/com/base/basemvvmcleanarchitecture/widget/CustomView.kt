package com.base.basemvvmcleanarchitecture.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import android.widget.RelativeLayout


open class CustomView : RelativeLayout {
    companion object {
        const val MATERIAL_DESIGN_XML = "http://schemas.android.com/apk/res-auto"
        const val ANDROID_XML = "http://schemas.android.com/apk/res/android"
    }

    val disabledBackgroundColor = Color.parseColor("#E2E2E2")
    var beforeBackground = 0

    // Indicate if user touched this view the last time
    var isLastTouch = false

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        if (enabled) setBackgroundColor(beforeBackground) else setBackgroundColor(disabledBackgroundColor)
        invalidate()
    }

    var animation = false

    override fun onAnimationStart() {
        super.onAnimationStart()
        animation = true
    }

    override fun onAnimationEnd() {
        super.onAnimationEnd()
        animation = false
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (animation) invalidate()
    }
}