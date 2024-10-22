package com.chinchin.ratingdialog

import android.animation.Animator
import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.Window
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView


class RatingDialog(private val context: Context) {
    private val dialog: Dialog = Dialog(context)
    private val main: RelativeLayout
    private val btnCancel: ImageView
    private val ratingFace: ImageView
    private val rotationRatingbarMain: RotationRatingBar
    private val btnSubmit: TextView
    private var pre: SharedPreferences = context.getSharedPreferences("rateData", Context.MODE_PRIVATE)
    private var edit: SharedPreferences.Editor = pre.edit()
    private var isEnable = true
    private var defRating = 0
    private var mRatingDialogListener: RatingDialogInterFace? = null

    init {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_rating)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        btnCancel = dialog.findViewById<View>(R.id.btnCancel) as ImageView
        ratingFace = dialog.findViewById<View>(R.id.ratingFace) as ImageView
        main = dialog.findViewById<View>(R.id.main) as RelativeLayout
        rotationRatingbarMain = dialog.findViewById<View>(R.id.rotationRatingbar) as RotationRatingBar
        btnSubmit = dialog.findViewById<View>(R.id.btnSubmit) as TextView
        main.alpha = 0f
        main.scaleY = 0f
        main.scaleX = 0f
        dialog.setOnDismissListener {
            main.rotation = 0f
            main.alpha = 0f
            main.scaleY = 0f
            main.scaleX = 0f
            main.clearAnimation()
            rotationRatingbarMain.visibility = View.INVISIBLE
            mRatingDialogListener?.onDismiss()
        }
        btnCancel.setOnClickListener { closeDialog() }

        rotationRatingbarMain.setOnRatingChangeListener(object : BaseRatingBar.OnRatingChangeListener {
            override fun onRatingChange(ratingBar: BaseRatingBar, rating: Float) {
//                if (ratingBar.getRating() < 4.0f) {
//                    setRatingFace(false)
//                } else {
//                    setRatingFace(true)
//                }
                when (rating) {
                    0F -> ratingFace.setImageResource(R.drawable.rating_0)
                    1F -> ratingFace.setImageResource(R.drawable.rating_1)
                    2F -> ratingFace.setImageResource(R.drawable.rating_2)
                    3F -> ratingFace.setImageResource(R.drawable.rating_3)
                    4F -> ratingFace.setImageResource(R.drawable.rating_4)
                    5F -> ratingFace.setImageResource(R.drawable.rating_5)
                }
                mRatingDialogListener?.onRatingChanged(rotationRatingbarMain.getRating())
            }
        })

        btnSubmit.setOnClickListener {
            main.animate()
                .scaleY(0f)
                .scaleX(0f)
                .alpha(0f)
                .setDuration(600)
                .setListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(animator: Animator) {}
                    override fun onAnimationEnd(animator: Animator) {
                        dialog.dismiss()
                        main.clearAnimation()
                        rotationRatingbarMain.visibility = View.INVISIBLE
                        mRatingDialogListener?.onSubmit(rotationRatingbarMain.getRating())
                    }

                    override fun onAnimationCancel(animator: Animator) {}
                    override fun onAnimationRepeat(animator: Animator) {}
                }).start()
        }
    }

    fun showDialog() {
        isEnable = pre.getBoolean("enb", true)
        if (isEnable) {
            dialog.show()
            rotationRatingbarMain.clearAnimation()
            rotationRatingbarMain.setRating(defRating.toFloat())
            ratingFace.setImageResource(R.drawable.rating_0)
            main.animate()
                .scaleY(1f)
                .scaleX(1f)
                .alpha(1f)
                .setDuration(600)
                .setListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(animator: Animator) {}
                    override fun onAnimationEnd(animator: Animator) {
                        main.clearAnimation()
                        rotationRatingbarMain.visibility = View.VISIBLE
                        rotationRatingbarMain.startAnimation(AnimationUtils.loadAnimation(context, R.anim.bounce_amn))
                    }

                    override fun onAnimationCancel(animator: Animator) {}
                    override fun onAnimationRepeat(animator: Animator) {}
                }).start()
        }
    }

    fun setEnable(isEnable: Boolean) {
        edit.putBoolean("enb", isEnable)
        edit.commit()
    }

    fun getEnable(): Boolean {
        return pre.getBoolean("enb", true)
    }

    private fun setRatingFace(isTrue: Boolean) {
        if (isTrue) {
            ratingFace.setImageResource(R.drawable.favorite)
        } else {
            ratingFace.setImageResource(R.drawable.favorite2)
        }
    }

    private fun closeDialog() {
        main.animate()
            .scaleY(0f)
            .scaleX(0f)
            .alpha(0f)
            .setDuration(600)
            .setListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animator: Animator) {}
                override fun onAnimationEnd(animator: Animator) {
                    dialog.dismiss()
                    main.clearAnimation()
                    rotationRatingbarMain.visibility = View.INVISIBLE
                    mRatingDialogListener?.onDismiss()
                }

                override fun onAnimationCancel(animator: Animator) {}
                override fun onAnimationRepeat(animator: Animator) {}
            }).start()
    }

    fun setDefaultRating(defaultRating: Int) {
        defRating = defaultRating
    }

    fun setRatingDialogListener(mRatingDialogListener: RatingDialogInterFace?) {
        this.mRatingDialogListener = mRatingDialogListener
    }

    interface RatingDialogInterFace {
        fun onDismiss()
        fun onSubmit(rating: Float)
        fun onRatingChanged(rating: Float)
    }
}
