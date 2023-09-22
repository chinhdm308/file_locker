package com.base.basemvvmcleanarchitecture.models

import com.base.basemvvmcleanarchitecture.baseadapter.BaseHideAdapter
import com.base.domain.models.file.FileModel
import com.base.domain.models.file.HideFile


class HideFileExt(
    id: Long?,
    beyondGroupId: Int,
    name: String,
    oldPathUrl: String,
    newPathUrl: String,
    moveDate: Long,
) : HideFile(id, beyondGroupId, name, oldPathUrl, newPathUrl, moveDate), BaseHideAdapter.IEnable {

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

    fun transientToModel(): FileModel? {
        return null
    }

    companion object {
        private fun copyVal(hideFile: HideFile): HideFileExt {
            return HideFileExt(
                hideFile.id,
                hideFile.beyondGroupId,
                hideFile.name,
                hideFile.oldPathUrl,
                hideFile.newPathUrl,
                hideFile.moveDate
            )
        }

        fun transList(list: List<Any>?): List<HideFileExt> {
            return list?.map { copyVal(it as HideFile) } ?: emptyList()
        }
    }
}