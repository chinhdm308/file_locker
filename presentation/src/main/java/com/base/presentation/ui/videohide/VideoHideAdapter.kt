package com.base.presentation.ui.videohide

import android.content.Context
import android.content.Intent
import android.provider.MediaStore
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.base.presentation.R
import com.base.presentation.baseadapter.BaseHideAdapter
import com.base.presentation.models.GroupVideoExt
import com.base.presentation.models.HideVideoExt
import com.base.presentation.viewholder.FileHideViewHolder
import com.base.presentation.utils.BitmapUtil
import com.base.presentation.utils.getVideoThumbnail

class VideoHideAdapter(context: Context, onListener: OnListener) : BaseHideAdapter(context, onListener) {

    override fun initView(viewHolder: RecyclerView.ViewHolder, data: Any?, position: Int) {
        if (viewHolder is FileHideViewHolder) {
            viewHolder.mImgPreview.setImageBitmap(null)
            viewHolder.mTvTitle.text = ""

            if (data is HideVideoExt) {
                @Suppress("DEPRECATION")
                var bitmap = getVideoThumbnail(data.newPathUrl, 96, 96, MediaStore.Images.Thumbnails.MICRO_KIND)

                if (bitmap != null) {
                    bitmap = BitmapUtil.toRoundBitmap(bitmap)
                    viewHolder.mImgPreview.setImageBitmap(bitmap)
                } else {
                    viewHolder.mImgPreview.setImageResource(R.drawable.avi_1)
                }

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
//                        OpenMIMEUtil.getInstance().openFile(context, data.newPathUrl)
                        context.startActivity(Intent(context, PlayVideoPreViewActivity::class.java).apply {
                            putExtra("id", position)
                            putStringArrayListExtra("list", (mListHideFile as List<HideVideoExt>).map{ it.newPathUrl } as ArrayList<String>)
                        })
                    }

                    // 长按监听
                    viewHolder.mFileHideItem.setOnLongClickListener {
                        doVibrator(context)
                        mOnListener.onLongClick(data)
                        false
                    }
                }
            } else if (data is GroupVideoExt) {
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
        this.mListGroup = GroupVideoExt.transList(groupImageViews)
        this.mListHideFile = HideVideoExt.transList(fileList)

        setGroup(groupID)
        notifyDataSetChanged()
    }

}