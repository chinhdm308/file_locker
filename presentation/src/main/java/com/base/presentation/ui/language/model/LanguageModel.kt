package com.base.presentation.ui.language.model;

import androidx.annotation.DrawableRes

data class LanguageModel(
    val name: String,
    val code: String,
    @DrawableRes val flag: Int = 0,
    var active: Boolean = false,
)