package com.base.presentation.models

import com.base.presentation.baseadapter.BaseHideAdapter
import com.base.domain.models.video.GroupVideo


class GroupVideoExt(id: Long?, parentId: Int?, name: String?, createDate: Long?) :
    GroupVideo(id, parentId, name, createDate),
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
        private fun copyVal(groupVideo: GroupVideo): GroupVideoExt {
            return GroupVideoExt(
                groupVideo.getId(),
                groupVideo.getParentId(),
                groupVideo.getName(),
                groupVideo.getCreateDate()
            )
        }

        fun transList(list: List<Any>?): List<GroupVideoExt> {
            return list?.map { copyVal(it as GroupVideo) } ?: emptyList()
        }
    }
}