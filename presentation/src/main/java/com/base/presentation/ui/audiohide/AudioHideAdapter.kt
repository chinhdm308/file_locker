package com.base.presentation.ui.audiohide

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.base.presentation.R
import com.base.presentation.baseadapter.BaseHideAdapter
import com.base.presentation.models.GroupAudioExt
import com.base.presentation.models.HideAudioExt
import com.base.presentation.viewholder.FileHideViewHolder
import com.base.presentation.utils.OpenMIMEUtil

class AudioHideAdapter(context: Context, onListener: OnListener) : BaseHideAdapter(context, onListener) {

    override fun initView(viewHolder: RecyclerView.ViewHolder, data: Any?, position: Int) {
        if (viewHolder is FileHideViewHolder) {
            viewHolder.mImgPreview.setImageBitmap(null)
            viewHolder.mTvTitle.text = ""

            if (data is HideAudioExt) {
                viewHolder.mImgPreview.setImageResource(R.drawable.audio_1)
                viewHolder.mCheckBox.isChecked = data.isEnable()
                viewHolder.mTvTitle.text = data.displayName
                viewHolder.mTvDetail.text = data.getSizeStr()
                if (edit) {
                    // 编辑模式
                    viewHolder.mCheckBox.visibility = View.VISIBLE
                    viewHolder.mFileHideItem.setOnClickListener {
                        data.setEnable(!data.isEnable())
                        viewHolder.mCheckBox.isChecked = data.isEnable()
                        updateSelect()
                    }

                    // 长按监听
                    viewHolder.mFileHideItem.setOnLongClickListener(null)
                } else {
                    // 正常打开模式
                    viewHolder.mCheckBox.visibility = View.GONE
                    viewHolder.mCheckBox.isChecked = false

                    viewHolder.mFileHideItem.setOnClickListener {
                        OpenMIMEUtil.getInstance().openFile(context, data.newPathUrl)
                    }

                    // 长按监听
                    viewHolder.mFileHideItem.setOnLongClickListener {
                        doVibrator(context)
                        mOnListener.onLongClick(data)
                        false
                    }
                }
            } else if (data is GroupAudioExt) {
                viewHolder.mImgPreview.setImageResource(R.drawable.folder)
                viewHolder.mTvTitle.text = data.getName()
                viewHolder.mFileHideItem.setOnClickListener {
                    if (edit) {
                        // 编辑模式
                        data.setEnable(!data.isEnable())
                    } else {
                        // 正常打开模式
                        mOnListener.openHolder(data)
                    }
                }
            }
        }
    }

    override fun setHitFiles(groupImageViews: List<Any>?, fileList: List<Any>?, groupID: Int) {
        this.mListGroup = GroupAudioExt.transList(groupImageViews)
        this.mListHideFile = HideAudioExt.transList(fileList)

        setGroup(groupID)
        notifyDataSetChanged()
    }

}