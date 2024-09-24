package com.base.presentation.viewholder

import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.base.presentation.databinding.ItemFileHideBinding

class FileHideViewHolder(val binding: ItemFileHideBinding) :
    RecyclerView.ViewHolder(binding.root) {

    var mFileHideItem: View
    var mImgPreview: ImageView
    var mTvTitle: TextView // 主内容
    var mTvDetail: TextView // 辅助内容（可能没有）
    var mCheckBox: CheckBox //选中

    init {
        binding.apply {
            mImgPreview = imgPrePreview
            mTvTitle = prePreViewTxt
            mTvDetail = tvDetail
            mCheckBox = itemFileCheckbox
            mFileHideItem = fileHideLayoutItem
        }
    }

    fun bind() {}
}