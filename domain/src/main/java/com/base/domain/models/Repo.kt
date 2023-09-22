package com.base.domain.models

data class Repo(
    val id: Long,
    val name: String,
    val description: String,
    val url: String,
    val isFavorite: Boolean = false,
    val userName: String? = null
)