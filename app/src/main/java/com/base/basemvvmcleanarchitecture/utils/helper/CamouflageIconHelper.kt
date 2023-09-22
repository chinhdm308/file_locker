package com.base.basemvvmcleanarchitecture.utils.helper

import androidx.annotation.DrawableRes
import com.base.basemvvmcleanarchitecture.R
import com.base.basemvvmcleanarchitecture.widget.dialog.singlechoice.BaseSingleChoiceModel

class CamouflageIconHelper {

    private val camouflageIconList: MutableList<CamouflageIconModel> = mutableListOf()

    init {
        camouflageIconList.add(
            CamouflageIconModel(
                key = CamouflageIconType.DEFAULT.key,
                name = CamouflageIconType.DEFAULT.appName,
                icon = R.mipmap.ic_launcher_round,
                type = CamouflageIconType.DEFAULT
            )
        )
        camouflageIconList.add(
            CamouflageIconModel(
                key = CamouflageIconType.CALCULATE.key,
                name = CamouflageIconType.CALCULATE.appName,
                icon = R.drawable.icons8_calculate_app_96,
                type = CamouflageIconType.CALCULATE
            )
        )
        camouflageIconList.add(
            CamouflageIconModel(
                key = CamouflageIconType.MUSIC.key,
                name = CamouflageIconType.MUSIC.appName,
                icon = R.drawable.icons8_music_app_65,
                type = CamouflageIconType.MUSIC
            )
        )
        camouflageIconList.add(
            CamouflageIconModel(
                key = CamouflageIconType.WEATHER.key,
                name = CamouflageIconType.WEATHER.appName,
                icon = R.drawable.icons8_weather_app_65,
                type = CamouflageIconType.WEATHER
            )
        )
        camouflageIconList.add(
            CamouflageIconModel(
                key = CamouflageIconType.ALARM.key,
                name = CamouflageIconType.ALARM.appName,
                icon = R.drawable.icons8_alarm_clock_app_96,
                type = CamouflageIconType.ALARM
            )
        )
    }

    fun getCamouflageIconList() = camouflageIconList.toList()

    data class CamouflageIconModel(
        val key: String,
        val name: String,
        @DrawableRes val icon: Int,
        val type: CamouflageIconType,

        var isChecked: Boolean = false
    ) : BaseSingleChoiceModel()

    enum class CamouflageIconType(val key: String, val appName: String) {
        DEFAULT(".SplashActivityDefault", "Default"),
        CALCULATE(".CalculateLauncher", "Calculate"),
        MUSIC(".MusicLauncher", "Music"),
        WEATHER(".WeatherLauncher", "Weather"),
        ALARM(".AlarmLauncher", "Alarm")
    }
}