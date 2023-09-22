package com.base.basemvvmcleanarchitecture.baseadapter

import android.app.Service
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.base.basemvvmcleanarchitecture.databinding.ItemFileHideBinding
import com.base.basemvvmcleanarchitecture.databinding.ItemFileHidePicBinding
import com.base.basemvvmcleanarchitecture.presentation.viewholder.FileHideViewHolder
import com.base.basemvvmcleanarchitecture.presentation.viewholder.PicHolder
import com.base.basemvvmcleanarchitecture.utils.helper.file.FileType

abstract class BaseHideAdapter(
    var context: Context,
    onListener: OnListener,
    private val type: FileType = FileType.FILE,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        /**
         * Default ID value of root directory folder
         */
        const val ROOT_FOLDER = -1
    }

    protected var mOnListener: OnListener = onListener
    private var mInflater: LayoutInflater = LayoutInflater.from(this.context)

    /**
     * Parent container id
     */
    private var groupInfo: GroupInfo? = null

    protected var mListGroup: List<Any>? = null
    protected var mListHideFile: List<Any>? = null

    protected var edit = false

    init {
        clear()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (type) {
            FileType.FILE -> {
                val binding = ItemFileHideBinding.inflate(mInflater, parent, false)
                return FileHideViewHolder(binding)
            }

            FileType.PIC -> {
                val binding = ItemFileHidePicBinding.inflate(mInflater, parent, false)
                PicHolder(binding)
            }

            else -> {
                val binding = ItemFileHideBinding.inflate(mInflater, parent, false)
                return FileHideViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        initView(holder, item, position)
    }

    override fun getItemCount(): Int {
        var count = 0
        if (mListGroup != null) count += mListGroup!!.size
        if (mListHideFile != null) count += mListHideFile!!.size
        return count
    }

    open fun selectAll(enable: Boolean) {
        if (mListHideFile != null) for (item in mListHideFile!!) {
            (item as IEnable).setEnable(enable)
        }
        mOnListener.setSelect(enable)
        notifyDataSetChanged()
    }

    protected open fun updateSelect() {
        if (mListHideFile != null) for (item in mListHideFile!!) {
            if ((item as IEnable).isEnable()) {
                mOnListener.setSelect(true)
                return
            }
        }
        mOnListener.setSelect(false)
    }

    /**
     * Set default selected items
     *
     * @param iEnable
     */
    open fun setSelect(iEnable: IEnable?) {
        iEnable?.setEnable(true)
        notifyDataSetChanged()
    }

    fun clear() {
        mListGroup = null
        mListHideFile = null
        groupInfo = null
        edit = false
    }

    /**
     * 是否在编辑状态
     *
     * @return
     */
    open fun isEdit(): Boolean {
        return edit
    }

    open fun setEditState(edit: Boolean) {
        this.edit = edit
        selectAll(false)
        notifyDataSetChanged()
    }

    /**
     * Get the parent group ID of the current group (currently supports two levels)
     *
     * @return
     */
    open fun getGroupParentID(): Int? {
        return if (groupInfo != null) groupInfo!!.parentId else ROOT_FOLDER
    }

    /**
     * Determine whether it is the root directory
     *
     * @return
     */
    open fun isRoot(): Boolean {
        return !(groupInfo != null && groupInfo!!.groupId != ROOT_FOLDER)
    }

    /**
     * Get the current group ID
     *
     * @return
     */
    open fun getGroupID(): Int? {
        return if (groupInfo != null) groupInfo!!.groupId else ROOT_FOLDER
    }

    fun getItem(position: Int): Any? {
        if (mListGroup != null && mListGroup!!.size > position) {
            return mListGroup!![position]
        }

        if (mListHideFile != null)
            return mListHideFile!![position - (mListGroup?.size ?: 0)]
        return null
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    /**
     * vibration service
     *
     * @param context
     */
    protected open fun doVibrator(context: Context) {
        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager =
                context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Service.VIBRATOR_SERVICE) as Vibrator
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(100)
        }
    }

    abstract fun initView(viewHolder: RecyclerView.ViewHolder, data: Any?, position: Int)

    /**
     * Set group data
     *
     * @param groupImageViews
     * @param fileList
     * @param groupID         Current group ID
     */
    abstract fun setHitFiles(groupImageViews: List<Any>?, fileList: List<Any>?, groupID: Int, )


    /**
     * 新加入的组
     *
     * @param groupID
     */
    protected open fun setGroup(groupID: Int) {
        if (groupInfo == null) {
            groupInfo = GroupInfo()
            groupInfo!!.parentId = ROOT_FOLDER
        }
        groupInfo!!.groupId = groupID
    }

    /**
     * Get the selected privacy file
     *
     * @return
     */
    open fun getHitFiles(): List<Any> {
        val list: MutableList<Any> = mutableListOf()
        if (mListHideFile != null) for (item in mListHideFile!!) {
            if ((item as IEnable).isEnable()) list.add(item)
        }
        return list
    }

    /**
     * Get the selected number
     *
     * @return
     */
    fun getHitFilesCount(): Int {
        var count = 0
        if (mListHideFile != null) for (item in mListHideFile!!) {
            if ((item as IEnable).isEnable()) count++
        }
        return count
    }

    /**
     * Get selected folder
     *
     * @return
     */
    open fun getGroupFiles(): List<Any>? {
        val list: MutableList<Any> = mutableListOf()
        if (mListGroup != null) for (iGroup in mListGroup!!) {
            if ((iGroup as IEnable).isEnable()) list.add(iGroup)
        }
        return list
    }

    class GroupInfo {
        var parentId: Int? = null
        var groupId: Int? = null
    }

    interface OnListener {
        /**
         * Open all contents in the current folder
         *
         * @param groupImage （组相关数据包）
         */
        fun openHolder(groupImage: Any?)

        /**
         * 设置选中内容状态切换
         *
         * @param selected
         */
        fun setSelect(selected: Boolean)

        fun onLongClick(iEnable: IEnable?)
    }

    interface IGroup {
        fun getId(): Long?

        fun getParentId(): Int?
    }

    interface IEnable {
        fun isEnable(): Boolean

        fun setEnable(enable: Boolean)
    }
}