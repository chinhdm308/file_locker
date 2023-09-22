package com.base.basemvvmcleanarchitecture.widget

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent

class LayoutRipple : CustomView {

    var background = 0
    var rippleSpeed = 10f
    var rippleSize = 3

    @JvmField
    var onClickListener: OnClickListener? = null

    @JvmField
    var backgroundColor = Color.parseColor("#FFFFFF")

    var rippleColor: Int? = null
    var xRippleOrigin: Float? = null
    var yRippleOrigin: Float? = null

    // Set atributtes of XML to View
    protected fun setAttributes(attrs: AttributeSet) {

        // Set background Color
        // Color by resource
        val bacgroundColor = attrs.getAttributeResourceValue(
            ANDROID_XML,
            "background", -1
        )
        if (bacgroundColor != -1) {
            setBackgroundColor(resources.getColor(bacgroundColor))
        } else {
            // Color by hexadecimal
            background = attrs.getAttributeIntValue(ANDROID_XML, "background", -1)
            if (background != -1) setBackgroundColor(background) else setBackgroundColor(backgroundColor)
        }
        // Set Ripple Color
        // Color by resource
        val rippleColor = attrs.getAttributeResourceValue(
            MATERIAL_DESIGN_XML,
            "rippleColor", -1
        )
        if (rippleColor != -1) {
            setRippleColor(resources.getColor(rippleColor))
        } else {
            // Color by hexadecimal
            val background = attrs.getAttributeIntValue(MATERIAL_DESIGN_XML, "rippleColor", -1)
            if (background != -1) setRippleColor(background) else setRippleColor(makePressColor())
        }
        rippleSpeed = attrs.getAttributeFloatValue(
            MATERIAL_DESIGN_XML,
            "rippleSpeed", 20f
        )
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        setAttributes(attrs)
    }

    // Set color of background
    override fun setBackgroundColor(color: Int) {
        backgroundColor = color
        if (isEnabled) beforeBackground = backgroundColor
        super.setBackgroundColor(color)
    }

    fun setRippleSpeed(rippleSpeed: Int) {
        this.rippleSpeed = rippleSpeed.toFloat()
    }

    // ### RIPPLE EFFECT ###
    @JvmField
    var x = -1f

    @JvmField
    var y: Float = -1f

    @JvmField
    var radius = -1f

    override fun onTouchEvent(event: MotionEvent): Boolean {
        invalidate()
        if (isEnabled) {
            isLastTouch = true
            if (event.action == MotionEvent.ACTION_DOWN) {
                radius = (height / rippleSize).toFloat()
                x = event.x
                y = event.y
            } else if (event.action == MotionEvent.ACTION_MOVE) {
                radius = (height / rippleSize).toFloat()
                x = event.x
                y = event.y
                if (!(event.x <= width && event.x >= 0 && event
                        .y <= height && event.y >= 0)
                ) {
                    isLastTouch = false
                    x = -1f
                    y = -1f
                }
            } else if (event.action == MotionEvent.ACTION_UP) {
                if (event.x <= width && event.x >= 0 && event.y <= height && event.y >= 0) {
                    radius++
                } else {
                    isLastTouch = false
                    x = -1f
                    y = -1f
                }
            }
            if (event.action == MotionEvent.ACTION_CANCEL) {
                isLastTouch = false
                x = -1f
                y = -1f
            }
        }
        return true
    }

    override fun onFocusChanged(
        gainFocus: Boolean, direction: Int,
        previouslyFocusedRect: Rect?,
    ) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect)
        if (!gainFocus) {
            x = -1f
            y = -1f
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        // super.onInterceptTouchEvent(ev);
        return true
    }

    fun makeCircle(): Bitmap? {
        val output = Bitmap.createBitmap(
            width, height,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(output)
        canvas.drawARGB(0, 0, 0, 0)
        val paint = Paint()
        paint.isAntiAlias = true
        if (rippleColor == null) rippleColor = makePressColor()
        paint.color = rippleColor!!
        x = if (xRippleOrigin == null) x else xRippleOrigin!!
        y = if (yRippleOrigin == null) y else yRippleOrigin!!
        canvas.drawCircle(x, y, radius, paint)
        if (radius > height / rippleSize) radius += rippleSpeed
        if (radius >= width) {
            x = -1f
            y = -1f
            radius = (height / rippleSize).toFloat()
            if (onClickListener != null) onClickListener!!.onClick(this)
        }
        return output
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (x != -1f) {
            val src = Rect(0, 0, width, height)
            val dst = Rect(0, 0, width, height)
            canvas?.drawBitmap(makeCircle()!!, src, dst, null)
            invalidate()
        }
    }

    /**
     * Make a dark color to ripple effect
     *
     * @return
     */
    protected fun makePressColor(): Int {
        var r = backgroundColor shr 16 and 0xFF
        var g = backgroundColor shr 8 and 0xFF
        var b = backgroundColor shr 0 and 0xFF
        r = if (r - 30 < 0) 0 else r - 30
        g = if (g - 30 < 0) 0 else g - 30
        b = if (b - 30 < 0) 0 else b - 30
        return Color.rgb(r, g, b)
    }

    override fun setOnClickListener(l: OnClickListener?) {
        super.setOnClickListener(l)
        onClickListener = l
    }

    fun setRippleColor(rippleColor: Int) {
        this.rippleColor = rippleColor
    }

    fun setxRippleOrigin(xRippleOrigin: Float?) {
        this.xRippleOrigin = xRippleOrigin
    }

    fun setyRippleOrigin(yRippleOrigin: Float?) {
        this.yRippleOrigin = yRippleOrigin
    }
}