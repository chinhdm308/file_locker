package com.base.domain.models.image

open class HideImage(
    open var id: Long?,
    open val beyondGroupId: Int,
    open val title: String,
    open val oldPathUrl: String,
    open val displayName: String,
    open val mimeType: String,
    open val newPathUrl: String,
    open val size: Long,
    open val moveDate: Long
)