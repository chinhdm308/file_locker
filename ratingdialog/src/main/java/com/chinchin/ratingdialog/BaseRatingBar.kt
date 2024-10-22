package com.chinchin.ratingdialog

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import kotlin.math.abs
import kotlin.math.ceil


/**
 * @param context      context
 * @param attrs        attributes from XML => app:mainText="mainText"
 * @param defStyleAttr attributes from default style (Application theme or activity theme)
 */
open class BaseRatingBar @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    LinearLayout(context, attrs, defStyleAttr), SimpleRatingBar {
    interface OnRatingChangeListener {
        fun onRatingChange(ratingBar: BaseRatingBar, rating: Float)
    }

    private var mNumStars: Int = 5
    private var mPadding = 0
    private val mStarWidth: Int
    private val mStarHeight: Int
    private var mRating = -1f
    private var mPreviousRating = 0f
    private var isTouchable = true
    private var isClearRatingEnabled = true
    private var mStartX = 0f
    private var mStartY = 0f
    private var mEmptyDrawable: Drawable?
    private var mFilledDrawable: Drawable?
    private var mOnRatingChangeListener: OnRatingChangeListener? = null
    protected var mPartialViews: MutableList<PartialView> = mutableListOf()

    /* Call by xml layout */
    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.RatingBarAttributes)
        val rating = typedArray.getFloat(R.styleable.RatingBarAttributes_rating, mRating)
        mNumStars = typedArray.getInt(R.styleable.RatingBarAttributes_numStars, mNumStars)
        mPadding = typedArray.getInt(R.styleable.RatingBarAttributes_starPadding, mPadding)
        mStarWidth = typedArray.getDimensionPixelSize(R.styleable.RatingBarAttributes_starWidth, 0)
        mStarHeight = typedArray.getDimensionPixelSize(R.styleable.RatingBarAttributes_starHeight, 0)
        mEmptyDrawable = typedArray.getDrawable(R.styleable.RatingBarAttributes_drawableEmpty)
        mFilledDrawable = typedArray.getDrawable(R.styleable.RatingBarAttributes_drawableFilled)
        isTouchable = typedArray.getBoolean(R.styleable.RatingBarAttributes_touchable, isTouchable)
        isClearRatingEnabled = typedArray.getBoolean(R.styleable.RatingBarAttributes_clearRatingEnabled, isClearRatingEnabled)
        typedArray.recycle()
        verifyParamsValue()
        initRatingView()
        setRating(rating)
    }

    private fun verifyParamsValue() {
        if (mNumStars <= 0) {
            mNumStars = 5
        }
        if (mPadding < 0) {
            mPadding = 0
        }
        if (mEmptyDrawable == null) {
            mEmptyDrawable = ContextCompat.getDrawable(context, R.drawable.empty)
        }
        if (mFilledDrawable == null) {
            mFilledDrawable = ContextCompat.getDrawable(context, R.drawable.filled)
        }
    }

    private fun initRatingView() {
        mPartialViews = mutableListOf()
        val params = ViewGroup.LayoutParams(
            if (mStarWidth == 0) LayoutParams.WRAP_CONTENT else mStarWidth, if (mStarHeight == 0) ViewGroup.LayoutParams.WRAP_CONTENT else mStarHeight
        )
        for (i in 1..mNumStars) {
            val partialView = getPartialView(i, mFilledDrawable, mEmptyDrawable)
            mPartialViews.add(partialView)
            addView(partialView, params)
        }
    }

    private fun getPartialView(ratingViewId: Int, filledDrawable: Drawable?, emptyDrawable: Drawable?): PartialView {
        val partialView = PartialView(context)
        partialView.id = ratingViewId
        partialView.setPadding(mPadding, mPadding, mPadding, mPadding)
        partialView.setFilledDrawable(filledDrawable)
        partialView.setEmptyDrawable(emptyDrawable)
        return partialView
    }

    /**
     * Retain this method to let other RatingBar can custom their decrease animation.
     */
    protected open fun emptyRatingBar() {
        fillRatingBar(0f)
    }

    /**
     * Use {maxIntOfRating} because if the rating is 3.5
     * the view which id is 3 also need to be filled.
     */
    protected open fun fillRatingBar(rating: Float) {
        for (partialView in mPartialViews) {
            val ratingViewId = partialView.id
            val maxIntOfRating = ceil(rating.toDouble())
            if (ratingViewId > maxIntOfRating) {
                partialView.setEmpty()
                continue
            }
            if (ratingViewId.toDouble() == maxIntOfRating) {
                partialView.setPartialFilled(rating)
            } else {
                partialView.setFilled()
            }
        }
    }

    override fun setNumStars(numStars: Int) {
        if (numStars <= 0) {
            return
        }
        mPartialViews.clear()
        removeAllViews()
        mNumStars = numStars
        initRatingView()
    }

    override fun getNumStars(): Int = mNumStars

    final override fun setRating(rating: Float) {
        var _rating = rating
        if (_rating > mNumStars) {
            _rating = mNumStars.toFloat()
        }
        if (rating < 0) {
            _rating = 0f
        }
        if (mRating == _rating) {
            return
        }
        mRating = _rating
        if (mOnRatingChangeListener != null) {
            mOnRatingChangeListener!!.onRatingChange(this, mRating)
        }
        fillRatingBar(_rating)
    }

    override fun getRating(): Float = mRating

    override fun setStarPadding(ratingPadding: Int) {
        if (ratingPadding < 0) {
            return
        }
        mPadding = ratingPadding
        for (partialView in mPartialViews) {
            partialView.setPadding(mPadding, mPadding, mPadding, mPadding)
        }
    }

    override fun getStarPadding(): Int = mPadding

    override fun setEmptyDrawableRes(@DrawableRes res: Int) {
        setEmptyDrawable(ContextCompat.getDrawable(context, res))
    }

    override fun setFilledDrawableRes(@DrawableRes res: Int) {
        setFilledDrawable(ContextCompat.getDrawable(context, res))
    }

    override fun setEmptyDrawable(drawable: Drawable?) {
        mEmptyDrawable = drawable
        for (partialView in mPartialViews) {
            partialView.setEmptyDrawable(drawable)
        }
    }

    override fun setFilledDrawable(drawable: Drawable?) {
        mFilledDrawable = drawable
        for (partialView in mPartialViews) {
            partialView.setFilledDrawable(drawable)
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return true
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!isTouchable) {
            return false
        }
        val eventX = event.x
        val eventY = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mStartX = eventX
                mStartY = eventY
                mPreviousRating = mRating
                handleMoveEvent(eventX)
            }

            MotionEvent.ACTION_MOVE -> handleMoveEvent(eventX)
            MotionEvent.ACTION_UP -> {
                if (!isClickEvent(mStartX, mStartY, event)) {
                    return false
                }
                handleClickEvent(eventX)
            }
        }
        return true
    }

    private fun handleMoveEvent(eventX: Float) {
        for (partialView in mPartialViews) {
            if (eventX < partialView.width / 2f) {
                setRating(0F)
                return
            }
            if (!isPositionInRatingView(eventX, partialView)) {
                continue
            }
            val rating = partialView.id
            if (mRating != rating.toFloat()) {
                setRating(rating.toFloat())
            }
        }
    }

    private fun handleClickEvent(eventX: Float) {
        for (partialView in mPartialViews) {
            if (!isPositionInRatingView(eventX, partialView)) {
                continue
            }
            val rating = partialView.id
            if (mPreviousRating == rating.toFloat() && isClearRatingEnabled) {
                setRating(0F)
            } else {
                setRating(rating.toFloat())
            }
            break
        }
    }

    private fun isPositionInRatingView(eventX: Float, ratingView: View): Boolean {
        return eventX > ratingView.left && eventX < ratingView.right
    }

    private fun isClickEvent(startX: Float, startY: Float, event: MotionEvent): Boolean {
        val duration = (event.eventTime - event.downTime).toFloat()
        if (duration > MAX_CLICK_DURATION) {
            return false
        }
        val differenceX = abs(startX - event.x)
        val differenceY = abs(startY - event.y)
        return !(differenceX > MAX_CLICK_DISTANCE || differenceY > MAX_CLICK_DISTANCE)
    }

    fun setOnRatingChangeListener(onRatingChangeListener: OnRatingChangeListener?) {
        mOnRatingChangeListener = onRatingChangeListener
    }

    companion object {
        const val TAG = "SimpleRatingBar"
        private const val MAX_CLICK_DISTANCE = 5
        private const val MAX_CLICK_DURATION = 200
    }
}
