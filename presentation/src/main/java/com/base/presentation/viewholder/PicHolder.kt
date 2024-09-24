package com.base.presentation.viewholder

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.base.presentation.databinding.ItemFileHidePicBinding

class PicHolder(val binding: ItemFileHidePicBinding) : RecyclerView.ViewHolder(binding.root) {
    var mItemFilePic: View
    var mItemFileOk: View //选中样式
    var mImgPrePreview: ImageView //图片容器
    var mData: Any? = null

    init {
        binding.apply {
            mImgPrePreview = imgPrePreview
            mItemFilePic = itemFilePic
            mItemFileOk = itemFileOk
        }
    }

}