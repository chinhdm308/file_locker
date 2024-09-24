package com.base.presentation.ui.filehide

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.base.presentation.R
import com.base.presentation.baseadapter.BaseHideAdapter
import com.base.presentation.models.GroupFileExt
import com.base.presentation.models.HideFileExt
import com.base.presentation.viewholder.FileHideViewHolder
import com.base.presentation.utils.OpenMIMEUtil


class FileHideAdapter(context: Context, onListener: OnListener) :
    BaseHideAdapter(context, onListener) {
    override fun initView(viewHolder: RecyclerView.ViewHolder, data: Any?, position: Int) {
        if (viewHolder is FileHideViewHolder) {

            viewHolder.mImgPreview.setImageBitmap(null)
            viewHolder.mTvTitle.text = ""

            if (data is HideFileExt) {
                viewHolder.mImgPreview.setImageResource(R.drawable.file_1)
                viewHolder.mTvTitle.text = data.name

                if (edit) {
                    viewHolder.mCheckBox.visibility = View.VISIBLE
                    viewHolder.mCheckBox.isChecked = data.isEnable()

                    viewHolder.mFileHideItem.setOnClickListener {
                        data.setEnable(!data.isEnable())
                        viewHolder.mCheckBox.isChecked = data.isEnable()
                        updateSelect()
                    }

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
            } else if (data is GroupFileExt) {
                viewHolder.mImgPreview.setImageResource(R.drawable.folder)
                viewHolder.mTvTitle.text = data.getName()

                viewHolder.mFileHideItem.setOnClickListener {
                    if (edit) {
                        // 编辑模式
                        viewHolder.mCheckBox.visibility = View.VISIBLE
                        val enable: Boolean = data.isEnable()
                        val fileHolder = it.tag as FileHideViewHolder
                        data.setEnable(!enable)
                    } else {
                        // 正常打开模式
                        viewHolder.mCheckBox.visibility = View.GONE
                        mOnListener.openHolder(data)
                    }
                }

            }
        }
    }

    override fun setHitFiles(groupImageViews: List<Any>?, fileList: List<Any>?, groupID: Int) {
        this.mListGroup = GroupFileExt.transList(groupImageViews)
        this.mListHideFile = HideFileExt.transList(fileList)

        setGroup(groupID)
        notifyDataSetChanged()
    }
}