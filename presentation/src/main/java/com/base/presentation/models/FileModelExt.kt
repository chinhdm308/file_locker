package com.base.presentation.models

import com.base.presentation.baseadapter.BaseHideAdapter
import com.base.domain.models.file.FileModel


class FileModelExt(name: String?, path: String?, fileType: Int?, numOfItems: Int) :
    FileModel(name, path, fileType, numOfItems),
    BaseHideAdapter.IEnable, Comparable<FileModelExt> {

    /**
     * 是否选中
     */
    private var enable = false

    override fun compareTo(other: FileModelExt): Int {
        return if (getFileType() == other.getFileType()) {
            getName()!!.compareTo(other.getName()!!, ignoreCase = true)
        } else if (getFileType()!! > other.getFileType()!!) {
            1
        } else -1
    }

    override fun isEnable(): Boolean {
        return enable
    }

    override fun setEnable(enable: Boolean) {
        this.enable = enable
    }

    companion object {
        private fun copyVal(fileModel: FileModel): FileModelExt {
            return FileModelExt(
                name = fileModel.getName(),
                path = fileModel.getPath(),
                fileType = fileModel.getFileType(),
                numOfItems = fileModel.getNumOfItems()
            )
        }

        /**
         * Transform data and filter out hidden files (those starting with '.')
         *
         * @param list List of FileModel objects
         * @return List of FileModelExt objects
         */
        fun transList(list: List<FileModel>): List<FileModelExt> {
            val dian = '.'
            return list
                .filter { !(it.getName() ?: "").startsWith(dian) }
                .map { copyVal(it) }
        }
    }

}