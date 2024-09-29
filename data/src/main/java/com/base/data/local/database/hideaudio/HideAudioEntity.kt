package com.base.data.local.database.hideaudio

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("hide_audio_table")
data class HideAudioEntity(
    @PrimaryKey val id: Int?,
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