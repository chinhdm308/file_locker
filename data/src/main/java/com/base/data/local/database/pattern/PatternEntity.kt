package com.base.data.local.database.pattern

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.base.domain.models.pattern.PatternDotMetadata

@Entity(tableName = "pattern")
@TypeConverters(PatternTypeConverter::class)
data class PatternEntity(
    @PrimaryKey
    @ColumnInfo(name = "pattern_metadata")
    val patternMetadata: PatternDotMetadata,
)