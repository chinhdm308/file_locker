package com.base.data.local.database.pattern

import androidx.room.TypeConverter
import com.base.domain.models.pattern.PatternDotMetadata
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class PatternTypeConverter {

    @TypeConverter
    fun patternToString(patternMetadata: PatternDotMetadata): String {
        val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
        return moshi.adapter(PatternDotMetadata::class.java).toJson(patternMetadata)
    }

    @TypeConverter
    fun stringToPattern(patternJson: String): PatternDotMetadata {
        val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
        return moshi.adapter(PatternDotMetadata::class.java).fromJson(patternJson)!!
    }
}