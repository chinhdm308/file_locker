package com.example.photoview

import android.annotation.TargetApi
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.view.View


class Compat {
    companion object {
        private const val SIXTY_FPS_INTERVAL = 1000 / 60

        @JvmStatic
        fun postOnAnimation(view: View?, runnable: Runnable) {
            if (VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN) {
                postOnAnimationJellyBean(view, runnable)
            } else {
                view?.postDelayed(runnable, SIXTY_FPS_INTERVAL.toLong())
            }
        }

        @JvmStatic
        private fun postOnAnimationJellyBean(view: View?, runnable: Runnable) {
            view?.postOnAnimation(runnable)
        }
    }
}