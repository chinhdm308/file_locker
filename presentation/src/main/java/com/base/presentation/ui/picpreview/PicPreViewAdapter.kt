package com.base.presentation.ui.picpreview

import android.content.Context
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.base.presentation.R
import com.base.presentation.baseadapter.BasePreViewAdapter
import com.base.presentation.models.ImageModelExt
import com.base.presentation.viewholder.PicHolder
import com.base.presentation.utils.helper.file.FileType
import com.bumptech.glide.Glide

class PicPreViewAdapter(val context: Context, onListener: OnListener, fileList: List<Any>) :
    BasePreViewAdapter(context, onListener, fileList, type = FileType.PIC) {

    override fun initView(viewHolder: RecyclerView.ViewHolder, data: Any?) {
        if (viewHolder is PicHolder) {
            if (data is ImageModelExt) {
                viewHolder.mImgPrePreview.setImageBitmap(null)
                viewHolder.mData = data
                viewHolder.mItemFileOk.isVisible = data.isEnable()

                Glide.with(context)
                    .load(data.path)
                    .error(R.drawable.default_picture)
                    .placeholder(R.drawable.default_picture)
                    .into(viewHolder.mImgPrePreview)

                viewHolder.mItemFilePic.setOnClickListener {
                    data.setEnable(!data.isEnable())
                    viewHolder.mItemFileOk.isVisible = data.isEnable()
                    updateSelect()
                }
            }
        }
    }
}