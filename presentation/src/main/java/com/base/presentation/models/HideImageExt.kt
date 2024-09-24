package com.base.presentation.models

import android.os.Parcelable
import com.base.presentation.baseadapter.BaseHideAdapter
import com.base.presentation.utils.transMoney
import com.base.domain.models.image.HideImage
import com.base.domain.models.image.ImageModel
import kotlinx.parcelize.Parcelize
import timber.log.Timber

@Parcelize
class HideImageExt(
    @JvmField
    var enable: Boolean = false,
    override var id: Long? = null,
    override var beyondGroupId: Int,
    override var title: String,
    override var oldPathUrl: String,
    override var displayName: String,
    override var mimeType: String,
    override var newPathUrl: String,
    override var size: Long,
    override var moveDate: Long
) : HideImage(
    id,
    beyondGroupId,
    title,
    oldPathUrl,
    displayName,
    mimeType,
    newPathUrl,
    size,
    moveDate
), BaseHideAdapter.IEnable, Parcelable {

    override fun isEnable(): Boolean {
        return enable
    }

    override fun setEnable(enable: Boolean) {
        this.enable = enable
    }

    companion object {
        private fun copyVal(hideImage: HideImage): HideImageExt {
            return HideImageExt(
                id = hideImage.id,
                beyondGroupId = hideImage.beyondGroupId,
                title = hideImage.title,
                oldPathUrl = hideImage.oldPathUrl,
                displayName = hideImage.displayName,
                mimeType = hideImage.mimeType,
                newPathUrl = hideImage.newPathUrl,
                size = hideImage.size,
                moveDate = hideImage.moveDate
            )
        }

        fun transList(list: List<Any>?): List<HideImageExt> {
            return list?.map { copyVal(it as HideImage) } ?: emptyList()
        }
    }

    fun getSizeStr(): String {
        val size = size.toFloat() / 1024 / 1024
        return transMoney(size) + "MB"
    }

    fun transientToModel(): ImageModel? {
        return try {
            ImageModel(
                id = id?.toInt()?.toLong() ?: 0L,
                title = title,
                path = newPathUrl,
                displayName = displayName,
                mimeType = mimeType,
                size = size
            )
        } catch (e: NumberFormatException) {
            Timber.tag("HideImageExt").e(e.toString())
            null
        }
    }
}