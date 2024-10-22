package com.chinchin.ratingdialog

import android.content.Context
import android.graphics.drawable.ClipDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import android.widget.ImageView
import android.widget.RelativeLayout


class PartialView : RelativeLayout {
    private var mFilledView: ImageView? = null
    private var mEmptyView: ImageView? = null

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        mFilledView = ImageView(context)
        mFilledView!!.scaleType = ImageView.ScaleType.CENTER_CROP
        mEmptyView = ImageView(context)
        mEmptyView!!.scaleType = ImageView.ScaleType.CENTER_CROP
        addView(mFilledView)
        addView(mEmptyView)
    }

    fun setFilledDrawable(drawable: Drawable?) {
        val clipDrawable = ClipDrawable(drawable, Gravity.START, ClipDrawable.HORIZONTAL)
        mFilledView!!.setImageDrawable(clipDrawable)
    }

    fun setEmptyDrawable(drawable: Drawable?) {
        val clipDrawable = ClipDrawable(drawable, Gravity.END, ClipDrawable.HORIZONTAL)
        mEmptyView!!.setImageDrawable(clipDrawable)
    }

    fun setFilled() {
        mFilledView!!.setImageLevel(10000)
        mEmptyView!!.setImageLevel(0)
    }

    fun setPartialFilled(rating: Float) {
        val percentage = rating % 1
        var level = (10000 * percentage).toInt()
        level = if (level == 0) 10000 else level
        mFilledView!!.setImageLevel(level)
        mEmptyView!!.setImageLevel(10000 - level)
    }

    fun setEmpty() {
        mFilledView!!.setImageLevel(0)
        mEmptyView!!.setImageLevel(10000)
    }
}
