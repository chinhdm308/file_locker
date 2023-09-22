package com.base.basemvvmcleanarchitecture.models

import com.base.basemvvmcleanarchitecture.baseadapter.BaseHideAdapter
import com.base.domain.models.file.GroupFile


class GroupFileExt(
    id: Long,
    parentId: Int,
    name: String,
    createDate: Long,
) : GroupFile(id, parentId, name, createDate), BaseHideAdapter.IEnable, BaseHideAdapter.IGroup {

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
        private fun copyVal(groupFile: GroupFile): GroupFileExt {
            return GroupFileExt(
                groupFile.getId()!!,
                groupFile.getParentId()!!,
                groupFile.getName()!!,
                groupFile.getCreateDate()!!
            )
        }

        fun transList(list: List<Any>?): List<GroupFileExt> {
            val listExt: MutableList<GroupFileExt> = mutableListOf()
            if (list != null) for (model in list) {
                listExt.add(copyVal(model as GroupFile))
            }
            return listExt
        }
    }
}