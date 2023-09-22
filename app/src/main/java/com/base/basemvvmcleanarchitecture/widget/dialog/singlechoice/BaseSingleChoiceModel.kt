package com.base.basemvvmcleanarchitecture.widget.dialog.singlechoice

import java.util.UUID

abstract class BaseSingleChoiceModel {
    val id: String = UUID.randomUUID().toString()
    val singleChoiceType: SingleChoiceType = SingleChoiceType.CAMOUFLAGE_ICON
}