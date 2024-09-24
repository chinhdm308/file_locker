package com.base.presentation.baseadapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.base.presentation.baseadapter.BaseHideAdapter.IEnable
import com.base.presentation.databinding.ItemFileHideBinding
import com.base.presentation.databinding.ItemFileHidePicBinding
import com.base.presentation.viewholder.FilePreViewHolder
import com.base.presentation.viewholder.PicHolder
import com.base.presentation.utils.helper.file.FileType


abstract class BasePreViewAdapter(
    context: Context,
    onListener: OnListener,
    fileList: List<Any>,
    private val type: FileType = FileType.FILE,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var mInflater: LayoutInflater
    protected var mOnListener: OnListener = onListener
    var mFileList: List<Any> = fileList

    init {
        mInflater = LayoutInflater.from(context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (type) {
            FileType.FILE -> {
                val binding = ItemFileHideBinding.inflate(mInflater, parent, false)
                FilePreViewHolder(binding)
            }

            FileType.PIC -> {
                val binding = ItemFileHidePicBinding.inflate(mInflater, parent, false)
                PicHolder(binding)
            }

            else -> {
                val binding = ItemFileHideBinding.inflate(mInflater, parent, false)
                FilePreViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val data = getItem(position)
        initView(holder, data)
    }

    override fun getItemCount(): Int {
        return mFileList.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    open fun clear() {}

    protected open fun updateSelect() {
        for (item in mFileList) {
            if ((item as IEnable).isEnable()) {
                mOnListener.setSelect(true)
                return
            }
        }
        mOnListener.setSelect(false)
    }

    /**
     * 获取选中的项
     *
     * @return
     */
    open fun getEnablePreViewFiles(): List<Any>? {
        val list: MutableList<IEnable> = mutableListOf()
        for (item in mFileList) {
            if ((item as IEnable).isEnable()) list.add(item)
        }
        return list
    }

    /**
     * 重新设置数据
     *
     * @param list
     */
    open fun setPreViewFiles(list: List<Any>) {
        mFileList = list
        notifyDataSetChanged()
    }

    fun getItem(position: Int): Any {
        return mFileList[position]
    }

    /**
     * 设置具体UI
     *
     * @param viewHolder
     * @param data
     */
    protected abstract fun initView(viewHolder: RecyclerView.ViewHolder, data: Any?)

    /**
     * 切换全选状态
     *
     * @param selected
     */
    open fun selectAll(selected: Boolean) {
        for (`object` in mFileList) {
            (`object` as IEnable).setEnable(selected)
        }
        mOnListener.setSelect(selected)
        notifyDataSetChanged()
    }

    interface OnListener {
        /**
         * 设置选中内容状态切换
         *
         * @param selected
         */
        fun setSelect(selected: Boolean)
    }
}