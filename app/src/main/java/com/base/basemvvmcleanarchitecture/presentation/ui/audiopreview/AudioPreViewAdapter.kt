package com.base.basemvvmcleanarchitecture.presentation.ui.audiopreview

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.base.basemvvmcleanarchitecture.R
import com.base.basemvvmcleanarchitecture.baseadapter.BasePreViewAdapter
import com.base.basemvvmcleanarchitecture.models.AudioModelExt
import com.base.basemvvmcleanarchitecture.presentation.viewholder.FilePreViewHolder


class AudioPreViewAdapter(context: Context, onListener: OnListener, fileList: List<Any>) :
    BasePreViewAdapter(context, onListener, fileList) {
    override fun initView(viewHolder: RecyclerView.ViewHolder, data: Any?) {
        if (viewHolder is FilePreViewHolder) {
            if (data is AudioModelExt) {
                viewHolder.mObject = data
                viewHolder.mImgPreview.setImageBitmap(null)

                viewHolder.mImgPreview.setImageResource(R.drawable.audio_1)
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