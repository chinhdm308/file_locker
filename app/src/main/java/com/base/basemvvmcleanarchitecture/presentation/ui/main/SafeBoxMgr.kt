package com.base.basemvvmcleanarchitecture.presentation.ui.main

import com.base.basemvvmcleanarchitecture.R

class SafeBoxMgr {
    companion object {
        const val BOX_ID_PIC = 1
        const val BOX_ID_VIDEO = 2
        const val BOX_ID_AUDIO = 3
        const val BOX_ID_FILE = 4
        const val BOX_ID_SETTING = 5
        const val BOX_ID_INFO = 6
    }

    private val safeBoxList: MutableList<SafeBox> = mutableListOf()

    init {
        init()
    }

    fun getSafeBoxList(): List<SafeBox> = safeBoxList.toList()


    private fun init() {
        safeBoxList.clear()
        safeBoxList.add(
            SafeBox(
                BOX_ID_PIC,
                R.drawable.box_image,
                R.string.box_title_pic,
                R.string.box_detail_pic
            )
        )
        safeBoxList.add(
            SafeBox(
                BOX_ID_VIDEO,
                R.drawable.box_avi,
                R.string.box_title_video,
                R.string.box_detail_file
            )
        )
        safeBoxList.add(
            SafeBox(
                BOX_ID_AUDIO,
                R.drawable.box_audio,
                R.string.box_title_audio,
                R.string.box_detail_file
            )
        )
        safeBoxList.add(
            SafeBox(
                BOX_ID_FILE,
                R.drawable.box_file,
                R.string.box_title_file,
                R.string.box_detail_file
            )
        )
        safeBoxList.add(
            SafeBox(
                BOX_ID_SETTING,
                R.drawable.box_setting,
                R.string.box_title_setting,
                0
            )
        )
    }

    data class SafeBox(
        val id: Int,
        val iconId: Int,
        val titleId: Int,
        val detailId: Int,
    )
}