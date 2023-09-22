package com.base.domain.models.video

open class VideoModel(
    val id: Long,
    val title: String,
    val album: String?,
    val artist: String?,
    val path: String,
    val displayName: String,
    val mimeType: String,
    val duration: Long,
    val size: Long,
)