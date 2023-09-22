package com.base.domain.models.image

open class ImageModel(
    val id: Long,
    val title: String,
    val path: String,
    val displayName: String,
    val mimeType: String,
    val size: Long,
)