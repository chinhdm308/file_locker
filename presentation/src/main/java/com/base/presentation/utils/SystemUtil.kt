package com.base.presentation.utils

import android.content.Context
import android.content.res.Configuration
import java.util.Locale

object SystemUtil {

    // Lưu ngôn ngữ đã cài đặt
    @JvmStatic
    fun saveLocale(context: Context, lang: String?) {
        setPreLanguage(context, lang)
    }

    // Load lại ngôn ngữ đã lưu và thay đổi chúng
    @Suppress("DEPRECATION")
    @JvmStatic
    fun setLocale(context: Context) {
        val language: String = getPreLanguage(context)
        if (language == "") {
            val config = Configuration()
            val locale = Locale.getDefault()
            Locale.setDefault(locale)
            config.locale = locale
            context.resources.updateConfiguration(config, context.resources.displayMetrics)
        } else {
            changeLanguage(language, context)
        }
    }

    // method phục vụ cho việc thay đổi ngôn ngữ.
    @Suppress("DEPRECATION")
    @JvmStatic
    private fun changeLanguage(lang: String, context: Context) {
        if (lang.equals("", ignoreCase = true)) return
        val myLocale = Locale(lang)
        saveLocale(context, lang)
        Locale.setDefault(myLocale)
        val config = Configuration()
        config.locale = myLocale
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
    }

    @JvmStatic
    fun getPreLanguage(mContext: Context): String {
        val preferences = mContext.getSharedPreferences("data", Context.MODE_PRIVATE)
        return preferences.getString("KEY_LANGUAGE", "") ?: ""
    }

    private fun setPreLanguage(context: Context, language: String?) {
        if (language == null || language == "") return

        val preferences = context.getSharedPreferences("data", Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString("KEY_LANGUAGE", language)
        editor.apply()
    }
}
