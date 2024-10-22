package com.chinchin.ratingdialog

import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes


internal interface SimpleRatingBar {
    fun setNumStars(numStars: Int)

    fun getNumStars(): Int

    fun setRating(rating: Float)

    fun getRating(): Float

    fun setStarPadding(ratingPadding: Int)

    fun getStarPadding(): Int

    fun setEmptyDrawable(drawable: Drawable?)
    fun setEmptyDrawableRes(@DrawableRes res: Int)
    fun setFilledDrawable(drawable: Drawable?)
    fun setFilledDrawableRes(@DrawableRes res: Int)
}
