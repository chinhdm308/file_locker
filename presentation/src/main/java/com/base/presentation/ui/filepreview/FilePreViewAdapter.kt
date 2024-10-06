package com.base.presentation.ui.filepreview

import android.content.Context
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.base.presentation.R
import com.base.presentation.baseadapter.BasePreViewAdapter
import com.base.presentation.models.FileModelExt
import com.base.presentation.viewholder.FilePreViewHolder
import com.base.domain.models.file.FileModel


class FilePreViewAdapter(
    val context: Context,
    private val onFolder: OnFolder,
    fileList: List<Any>
) : BasePreViewAdapter(context, onFolder, fileList) {
    override fun initView(viewHolder: RecyclerView.ViewHolder, data: Any?) {
        if (viewHolder is FilePreViewHolder) {
            viewHolder.mTvDetail.isVisible = false
            if (data is FileModelExt) {
                viewHolder.mObject = data
                val fileModel: FileModel = viewHolder.mObject as FileModel
                viewHolder.mObject = data

                if (fileModel.getFileType() == FileModel.FILE_FILE) {
                    viewHolder.mImgPreview.setImageResource(R.drawable.file)
                } else {
                    viewHolder.mImgPreview.setImageResource(R.drawable.folder)
                    viewHolder.mTvDetail.isVisible = true
                    viewHolder.mTvDetail.text = context.getString(
                        if (fileModel.getNumOfItems() > 0) {
                            R.string.num_of_items
                        } else {
                            R.string.num_of_item
                        },
                        fileModel.getNumOfItems()
                    )
                }

                viewHolder.mTvTitle.text = fileModel.getName()

                //Set directly without animation effect
                viewHolder.mCheckBox.isChecked = data.isEnable()

                if (data.getFileType() == FileModel.FILE_DIR) {
                    //Folder
                    viewHolder.mCheckBox.visibility = View.GONE
                    viewHolder.mItemFileLinear.setOnClickListener { // 打开文件夹
                        onFolder.openFolder(viewHolder.mObject as FileModel?)
                    }
                } else {
                    //Document
                    viewHolder.mCheckBox.visibility = View.VISIBLE
                    viewHolder.mItemFileLinear.setOnClickListener {
                        data.setEnable(!data.isEnable())
                        viewHolder.mCheckBox.isChecked = data.isEnable()
                        updateSelect()
                    }
                }
            }
        }
    }

    override fun selectAll(selected: Boolean) {
        for (item in mFileList) {
            if ((item as FileModelExt).getFileType() == FileModel.FILE_FILE) {
                item.setEnable(selected)
            }
        }
        mOnListener.setSelect(selected)
        notifyDataSetChanged()
    }

    interface OnFolder : OnListener {
        /**
         * Select the folder and open the folder
         *
         * @param fileModel
         */
        fun openFolder(fileModel: FileModel?)
    }
}