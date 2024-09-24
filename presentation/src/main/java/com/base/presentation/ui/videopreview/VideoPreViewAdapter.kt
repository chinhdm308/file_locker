package com.base.presentation.ui.videopreview

import android.content.Context
import android.provider.MediaStore
import androidx.recyclerview.widget.RecyclerView
import com.base.presentation.R
import com.base.presentation.baseadapter.BasePreViewAdapter
import com.base.presentation.models.VideoModelExt
import com.base.presentation.viewholder.FilePreViewHolder
import com.base.presentation.utils.BitmapUtil
import com.base.presentation.utils.getVideoThumbnail


class VideoPreViewAdapter(context: Context, onListener: OnListener, fileList: List<Any>) :
    BasePreViewAdapter(context, onListener, fileList) {
    override fun initView(viewHolder: RecyclerView.ViewHolder, data: Any?) {
        if (viewHolder is FilePreViewHolder) {
            if (data is VideoModelExt) {
                viewHolder.mObject = data
                viewHolder.mImgPreview.setImageBitmap(null)

                @Suppress("DEPRECATION")
                var bitmap = getVideoThumbnail(data.path, 96, 96, MediaStore.Images.Thumbnails.MICRO_KIND)

                if (bitmap != null) {
                    bitmap = BitmapUtil.toRoundBitmap(bitmap)
                    viewHolder.mImgPreview.setImageBitmap(bitmap)
                } else {
                    viewHolder.mImgPreview.setImageResource(R.drawable.avi_1)
                }

                viewHolder.mTvTitle.text = data.displayName
                viewHolder.mTvDetail.text = data.getSizeStr()

                viewHolder.mCheckBox.isChecked = data.isEnable()
                viewHolder.mItemFileLinear.setOnClickListener {
                    data.setEnable(!data.isEnable())
                    viewHolder.mCheckBox.isChecked = data.isEnable()
                    updateSelect()
                }
            }
        }
    }
}