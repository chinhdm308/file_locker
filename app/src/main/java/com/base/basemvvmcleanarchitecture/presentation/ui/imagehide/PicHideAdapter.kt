package com.base.basemvvmcleanarchitecture.presentation.ui.imagehide

import android.content.Context
import android.content.Intent
import android.os.Parcelable
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.base.basemvvmcleanarchitecture.R
import com.base.basemvvmcleanarchitecture.baseadapter.BaseHideAdapter
import com.base.basemvvmcleanarchitecture.models.GroupImageExt
import com.base.basemvvmcleanarchitecture.models.HideImageExt
import com.base.basemvvmcleanarchitecture.presentation.ui.photopreview.PhotoPreViewActivity
import com.base.basemvvmcleanarchitecture.presentation.viewholder.PicHolder
import com.base.basemvvmcleanarchitecture.utils.helper.file.FileType
import com.bumptech.glide.Glide
import java.util.ArrayList

class PicHideAdapter(context: Context, onListener: OnListener) : BaseHideAdapter(context, onListener, FileType.PIC) {

    @Suppress("UNCHECKED_CAST")
    override fun initView(viewHolder: RecyclerView.ViewHolder, data: Any?, position: Int) {
        if (viewHolder is PicHolder) {
            viewHolder.mImgPrePreview.setImageBitmap(null)
            viewHolder.mData = data

            if (data is HideImageExt) {
                Glide.with(context)
                    .load(data.newPathUrl)
                    .error(R.drawable.default_picture)
                    .placeholder(R.drawable.default_picture)
                    .into(viewHolder.mImgPrePreview)

                if (edit) {
                    viewHolder.mItemFileOk.isVisible = data.isEnable()

                    viewHolder.binding.root.setOnClickListener {
                        data.setEnable(!data.isEnable())
                        viewHolder.mItemFileOk.isVisible = data.isEnable()
                        updateSelect()
                    }

                    viewHolder.binding.root.setOnLongClickListener(null)
                } else {
                    viewHolder.mItemFileOk.isVisible = false

                    viewHolder.binding.root.setOnClickListener {
                        context.startActivity(Intent(context, PhotoPreViewActivity::class.java).apply {
                            putExtra("id", position)
                            putParcelableArrayListExtra("list", mListHideFile as ArrayList<out Parcelable>?)
                        })
                    }

                    viewHolder.binding.root.setOnLongClickListener {
                        doVibrator(context)
                        mOnListener.onLongClick(data)
                        false
                    }
                }
            } else if (data is GroupImageExt) {
                viewHolder.mItemFileOk.isVisible = data.isEnable()
                viewHolder.mImgPrePreview.setImageResource(R.drawable.folder)

                viewHolder.binding.root.setOnClickListener {
                    if (edit) {
                        val enable = data.isEnable()
                        val fileHolder = viewHolder
                        data.setEnable(!enable)
                    } else {
                        mOnListener.openHolder(data)
                    }
                }
            }
        }
    }

    override fun setHitFiles(groupImageViews: List<Any>?, fileList: List<Any>?, groupID: Int) {
        this.mListGroup = GroupImageExt.transList(groupImageViews)
        this.mListHideFile = HideImageExt.transList(fileList)

        setGroup(groupID)
        notifyDataSetChanged()
    }
}