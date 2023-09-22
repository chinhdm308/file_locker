package com.base.basemvvmcleanarchitecture.presentation.viewholder

import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.base.basemvvmcleanarchitecture.databinding.ItemFileHideBinding

class FilePreViewHolder(val binding: ItemFileHideBinding) :
    RecyclerView.ViewHolder(binding.root) {
    var mImgPreview: ImageView
    var mTvTitle: TextView
    var mTvDetail: TextView // 辅助内容（可能没有）
    var mCheckBox: CheckBox //选中按钮
    var mItemFileLinear: View
    var mObject: Any? = null

    init {
        binding.apply {
            mImgPreview = imgPrePreview
            mTvTitle = prePreViewTxt
            mTvDetail = tvDetail
            mItemFileLinear = fileHideLayoutItem
            mCheckBox = itemFileCheckbox
        }
    }
}