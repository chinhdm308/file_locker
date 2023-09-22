package com.base.domain.models.audio

open class HideAudio(
    var id: Long?,
    val beyondGroupId: Int,
    val title: String,
    val album: String?,
    val artist: String?,
    val oldPathUrl: String,
    val displayName: String,
    val mimeType: String,
    val duration: String,
    val newPathUrl: String,
    val size: Long,
    val moveDate: Long
)