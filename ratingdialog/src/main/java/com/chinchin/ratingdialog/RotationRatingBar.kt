package com.chinchin.ratingdialog

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.animation.AnimationUtils


class RotationRatingBar : BaseRatingBar {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun emptyRatingBar() {
        // Need to remove all previous runnable to prevent emptyRatingBar and fillRatingBar out of sync
        sUiHandler.removeCallbacksAndMessages(null)
        var delay = 0
        for (partialView: PartialView in mPartialViews) {
            Handler(Looper.getMainLooper()).postDelayed({ partialView.setEmpty() }, 5.let { delay += it; delay }.toLong())
        }
    }

    override fun fillRatingBar(rating: Float) {
        // Need to remove all previous runnable to prevent emptyRatingBar and fillRatingBar out of sync
        sUiHandler.removeCallbacksAndMessages(null)
        var delay = 0
        for (partialView: PartialView in mPartialViews) {
            val ratingViewId = partialView.id
            val maxIntOfRating = Math.ceil(rating.toDouble())
            if (ratingViewId > maxIntOfRating) {
                partialView.setEmpty()
                continue
            }
            sUiHandler.postDelayed({
                if (ratingViewId.toDouble() == maxIntOfRating) {
                    partialView.setPartialFilled(rating)
                } else {
                    partialView.setFilled()
                }
                if (ratingViewId.toFloat() == rating) {
                    val rotation = AnimationUtils.loadAnimation(context, R.anim.rotation)
                    partialView.startAnimation(rotation)
                }
            }, 15.let { delay += it; delay }.toLong())
        }
    }

    companion object {
        private val sUiHandler = Handler(Looper.getMainLooper())
    }
}
