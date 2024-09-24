package com.base.presentation.models

import com.base.presentation.baseadapter.BaseHideAdapter
import com.base.domain.models.audio.GroupAudio


class GroupAudioExt(id: Long?, parentId: Int?, name: String?, createDate: Long?) : GroupAudio(id, parentId, name, createDate),
    BaseHideAdapter.IEnable, BaseHideAdapter.IGroup {

    /**
     * 是否选中
     */
    private var enable = false

    override fun isEnable(): Boolean {
        return enable
    }

    override fun setEnable(enable: Boolean) {
        this.enable = enable
    }

    companion object {
        private fun copyVal(groupAudio: GroupAudio): GroupAudioExt {
            return GroupAudioExt(
                groupAudio.getId(),
                groupAudio.getParentId(),
                groupAudio.getName(),
                groupAudio.getCreateDate()
            )
        }

        fun transList(list: List<Any>?): List<GroupAudioExt> {
            return list?.map { copyVal(it as GroupAudio) } ?: emptyList()
        }
    }
}