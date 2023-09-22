package com.base.basemvvmcleanarchitecture.utils.helper

import android.content.Context

import android.media.AudioManager

import android.media.SoundPool
import android.os.Build
import com.base.basemvvmcleanarchitecture.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


@Suppress("DEPRECATION")
class PlayWarringSoundHelper @Inject constructor(
    @ApplicationContext context: Context
) {
    private var soundPool: SoundPool? = null

    init {
        soundPool = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            SoundPool.Builder()
                .setMaxStreams(10)
                .build()
        } else {
            SoundPool(10, AudioManager.STREAM_SYSTEM, 5)
        }
        soundPool!!.load(context, R.raw.warring01, 1)
    }

    fun playSound() {
        soundPool!!.play(1, 1f, 1f, 0, 0, 1f)
    }
}