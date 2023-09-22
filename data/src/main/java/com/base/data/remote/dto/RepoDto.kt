package com.base.data.remote.dto

import com.squareup.moshi.Json

data class RepoDto(
    @Json(name = "id") val id: Long,
    @Json(name = "name") val name: String,
    @Json(name = "description") val description: String,
    @Json(name = "html_url") val url: String,
)