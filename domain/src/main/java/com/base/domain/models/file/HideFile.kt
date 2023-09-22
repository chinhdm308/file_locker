package com.base.domain.models.file

open class HideFile(
    var id: Long?,
    val beyondGroupId: Int,
    val name: String,
    val oldPathUrl: String,
    val newPathUrl: String,
    val moveDate: Long,
)