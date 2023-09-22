package com.base.basemvvmcleanarchitecture.models

import com.base.basemvvmcleanarchitecture.baseadapter.BaseHideAdapter
import com.base.domain.models.image.GroupImage


class GroupImageExt(
    id: Long?,
    parentId: Int?,
    name: String?,
    createDate: Long?,
) : GroupImage(id, parentId, name, createDate), BaseHideAdapter.IEnable, BaseHideAdapter.IGroup {

    private var enable = false

    override fun isEnable(): Boolean {
        return enable
    }

    override fun setEnable(enable: Boolean) {
        this.enable = enable
    }

    companion object {
        private fun copyVal(groupImage: GroupImage): GroupImageExt {
            return GroupImageExt(
                id = groupImage.getId(),
                parentId = groupImage.getParentId(),
                name = groupImage.getName(),
                createDate = groupImage.getCreateDate()
            )
        }

        fun transList(list: List<Any>?): List<GroupImageExt> {
            return list?.map { copyVal(it as GroupImage) } ?: emptyList()
        }
    }
}