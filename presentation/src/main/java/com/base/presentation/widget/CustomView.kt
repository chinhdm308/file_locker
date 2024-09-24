package com.base.presentation.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import android.widget.RelativeLayout


open class CustomView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : RelativeLayout(context, attrs, defStyleAttr) {

    companion object {
        const val MATERIAL_DESIGN_XML = "http://schemas.android.com/apk/res-auto"
        const val ANDROID_XML = "http://schemas.android.com/apk/res/android"
    }

    private val disabledBackgroundColor = Color.parseColor("#E2E2E2")
    var beforeBackground = 0

    // Indicate if user touched this view the last time
    var isLastTouch = false

    private var animation = false


    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        setBackgroundColor(if (enabled) beforeBackground else disabledBackgroundColor)
        invalidate()
    }

    override fun onAnimationStart() {
        super.onAnimationStart()
        animation = true
    }

    override fun onAnimationEnd() {
        super.onAnimationEnd()
        animation = false
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (animation) invalidate()
    }
}