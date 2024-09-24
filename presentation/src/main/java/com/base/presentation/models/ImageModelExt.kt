package com.base.presentation.models

import com.base.presentation.baseadapter.BaseHideAdapter
import com.base.domain.models.image.ImageModel


class ImageModelExt(
    id: Long,
    title: String,
    path: String,
    displayName: String,
    mimeType: String,
    size: Long,
) : ImageModel(id, title, path, displayName, mimeType, size), BaseHideAdapter.IEnable {

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
        private fun copyVal(imageModel: ImageModel): ImageModelExt {
            return ImageModelExt(
                id = imageModel.id,
                title = imageModel.title,
                displayName = imageModel.displayName,
                mimeType = imageModel.mimeType,
                path = imageModel.path,
                size = imageModel.size
            )
        }

        fun transList(list: List<ImageModel>?): List<ImageModelExt> {
            return list?.map { copyVal(it) } ?: emptyList()
        }
    }
}