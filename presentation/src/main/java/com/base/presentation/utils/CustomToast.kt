package com.base.presentation.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.DrawableRes
import com.base.presentation.R


class CustomToast(context: Context) : Toast(context) {

    companion object {
        const val SUCCESS = 1
        const val WARNING = 2
        const val ERROR = 3
        const val CONFUSING = 4

        private const val SHORT = 4000L
        private const val LONG = 7000L

        fun makeText(context: Context, message: String?, duration: Int, type: Int, androidIcon: Boolean): Toast {
            val toast = Toast(context)
            toast.duration = duration
            val layout: View = LayoutInflater.from(context).inflate(R.layout.customtoast_layout, null, false)
            val l1 = layout.findViewById<View>(R.id.toast_text) as TextView
            val linearLayout = layout.findViewById<View>(R.id.toast_type) as LinearLayout
            val img = layout.findViewById<View>(R.id.toast_icon) as ImageView
            val img1 = layout.findViewById<View>(R.id.imageView4) as ImageView
            l1.text = message
            if (androidIcon) img1.visibility = View.VISIBLE
            else img1.visibility = View.GONE
            if (type == 1) {
                linearLayout.setBackgroundResource(R.drawable.success_shape)
                img.setImageResource(R.drawable.ic_check_black_24dp)
            } else if (type == 2) {
                linearLayout.setBackgroundResource(R.drawable.warning_shape)
                img.setImageResource(R.drawable.ic_pan_tool_black_24dp)
            } else if (type == 3) {
                linearLayout.setBackgroundResource(R.drawable.error_shape)
                img.setImageResource(R.drawable.ic_clear_black_24dp)
            } else if (type == 4) {
                linearLayout.setBackgroundResource(R.drawable.confusing_shape)
                img.setImageResource(R.drawable.ic_refresh_black_24dp)
            }
            toast.setView(layout)
            return toast
        }

        fun makeText(context: Context, message: String?, duration: Int, type: Int, @DrawableRes resId: Int): Toast {
            val toast = Toast(context)
            val layout: View = LayoutInflater.from(context).inflate(R.layout.customtoast_layout, null, false)
            val l1 = layout.findViewById<View>(R.id.toast_text) as TextView
            val linearLayout = layout.findViewById<View>(R.id.toast_type) as LinearLayout
            val img = layout.findViewById<View>(R.id.toast_icon) as ImageView
            l1.text = message
            if (type == 1) {
                linearLayout.setBackgroundResource(R.drawable.success_shape)
                img.setImageResource(resId)
            } else if (type == 2) {
                linearLayout.setBackgroundResource(R.drawable.warning_shape)
                img.setImageResource(resId)
            } else if (type == 3) {
                linearLayout.setBackgroundResource(R.drawable.error_shape)
                img.setImageResource(resId)
            } else if (type == 4) {
                linearLayout.setBackgroundResource(R.drawable.confusing_shape)
                img.setImageResource(resId)
            }
            toast.setView(layout)
            return toast
        }
    }
}