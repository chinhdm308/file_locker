package com.base.basemvvmcleanarchitecture.base

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import com.base.basemvvmcleanarchitecture.R
import com.base.basemvvmcleanarchitecture.baseadapter.BaseHideAdapter
import com.base.basemvvmcleanarchitecture.databinding.ActivityFileHideBinding
import com.base.basemvvmcleanarchitecture.presentation.ui.main.MainActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder


abstract class BaseHideActivity :
    BaseActivity<ActivityFileHideBinding>(ActivityFileHideBinding::inflate),
    BaseHideAdapter.OnListener {

    companion object {
        const val ALPHA_DISABLE = 0.3f
        const val ALPHA_ENABLE = 1.0f
    }

    @StringRes
    private var mRidTitleTxt = 0

    @StringRes
    private var mRidTitleTxtEdit = 0

    /**
     * 是否编辑状态
     */
    private var mIsEdit = false

    @StringRes
    private var ridStringType = 0

    protected var mBaseHideAdapter: BaseHideAdapter? = null

    override fun initView(savedInstanceState: Bundle?) {
        window.statusBarColor = getColor(R.color.color_toolbar_hide_file)
        super.initView(savedInstanceState)

        initUI()
        initAdapter()
    }

    override fun setOnClick() {
        super.setOnClick()

        binding.hideBtnAdd.setOnClickListener {
            addFile()
        }

        binding.itemFileCheckbox.setOnClickListener {
            selectAll(binding.itemFileCheckbox.isChecked)
        }

        binding.btnBack.setOnClickListener {
            if (!onBack()) {
                onHome()
                finish()
            }
        }

        binding.picHideImgRecovery.setOnClickListener {
            if (it.alpha == ALPHA_ENABLE) {
                recoveryDialog()
            }
        }

        binding.picHideImgDel.setOnClickListener {
            if (it.alpha == ALPHA_ENABLE) {
                delDialog()
            }
        }

        binding.picHideBtnPreview.setOnClickListener {
            if (it.alpha == ALPHA_ENABLE) {
                setEditState(true)
            }
        }

        binding.picHideBtnEdit.setOnClickListener {
            setEditState(false)
        }
    }

    override fun onResume() {
        super.onResume()
        setEditState(false)
        binding.itemFileCheckbox.isChecked = false
        setSelect(false)
        openHolder()
    }

    override fun onDestroy() {
        if (mBaseHideAdapter != null) {
            mBaseHideAdapter!!.clear()
        }
        super.onDestroy()
    }

    abstract fun initAdapter()

    fun setRidStringType(@StringRes id: Int) {
        ridStringType = id
    }

    /**
     * 初始化UI设置
     */
    protected open fun initUI() {
        setUI()
    }

    private fun setUI() {
        binding.itemFileCheckbox.setOnCheckedChangeListener { _, isChecked ->
            selectAll(isChecked)
        }
    }

    /**
     * 设置资源id
     *
     * @param rid
     */
    protected open fun setTitleRID(@StringRes rid: Int, @StringRes ridEdit: Int) {
        mRidTitleTxt = rid
        mRidTitleTxtEdit = ridEdit
    }

    private fun delDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle(getString(R.string.file_dialog_del) + getString(ridStringType))
            .setMessage(getString(ridStringType) + getString(R.string.file_dialog_del_missage))
            .setPositiveButton(R.string.lock_ok) { _, _ ->
                delFiles()
                setEditState(false)
                openHolder()
            }
            .setNegativeButton(R.string.lock_cancel) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    /**
     * 恢复选中内容
     */
    private fun recoveryDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle(getString(R.string.file_dialog_recovery) + getString(ridStringType))
            .setMessage(getString(ridStringType) + getString(R.string.file_dialog_recovery_missage))
            .setPositiveButton(R.string.lock_ok) { _, _ ->
                recoveryFiles()
                setEditState(false)
                openHolder()
            }
            .setNegativeButton(R.string.lock_cancel) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    /**
     * 彻底删除选中的文件
     */
    protected abstract fun delFiles()

    /**
     * 全选切换
     *
     * @param enable
     */
    protected open fun selectAll(enable: Boolean) {
        mBaseHideAdapter?.selectAll(enable)
    }

    /**
     * 返回主页面
     */
    private fun onHome() {
        startActivity(Intent(this, MainActivity::class.java))
    }

    /**
     * 添加文件夹
     */
    abstract fun addFolder()

    /**
     * 删除文件夹（已选中的）同时恢复文件夹内的所有文件
     *
     * @return
     */
    abstract fun delFolder(): Boolean

    /**
     * 恢复隐藏的文件
     */
    abstract fun recoveryFiles()

    /**
     * 添加新内容
     */
    abstract fun addFile()

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (onBack()) return true
        }
        onHome()
        return true
    }

    /**
     * 返回/退出页面
     *
     * @return 是否停留
     */
    protected open fun onBack(): Boolean {
        if (mBaseHideAdapter != null && !mBaseHideAdapter!!.isRoot()) {
            openHolder(mBaseHideAdapter!!.getGroupParentID())
            return true
        }

        // 切换为返回状态
//        if (binding.btnBack.getAction() is CloseAction) {
//            setEditState(false)
//            return true
//        }
        return false
    }

    /**
     * 重新初始化列表数据
     */
    protected open fun openHolder() {
        openHolder(mBaseHideAdapter?.getGroupID())
    }

    /**
     * 打开指定分组内容
     *
     * @param groupID
     */
    abstract fun openHolder(groupID: Int)

    /**
     * 设置当前状态
     *
     * @param isEdit （编辑/预览）
     */
    private fun setEditState(isEdit: Boolean) {
        mIsEdit = isEdit
        mBaseHideAdapter?.setEditState(isEdit)
        if (isEdit) {
            binding.picHideBtnPreview.visibility = View.GONE
            binding.picHideBtnEdit.visibility = View.VISIBLE
//            mBtn_back.setAction(CloseAction(), ActionView.ROTATE_COUNTER_CLOCKWISE)
            binding.hideBtnAdd.visibility = View.GONE
            binding.fileHideTxtTitle.text = ""
        } else {
            binding.picHideBtnPreview.visibility = View.VISIBLE
            binding.picHideBtnEdit.visibility = View.GONE
//            mBtn_back.setAction(BackAction(), ActionView.ROTATE_CLOCKWISE)
            binding.hideBtnAdd.visibility = View.VISIBLE
            binding.fileHideTxtTitle.setText(mRidTitleTxt)
        }
    }

    open fun isEditState(): Boolean {
        return mIsEdit
    }

    override fun onLongClick(iEnable: BaseHideAdapter.IEnable?) {
        setEditState(true)
        mBaseHideAdapter?.setSelect(iEnable)
        setSelect(true)
    }

    override fun setSelect(selected: Boolean) {
        if (selected) {
            binding.picHideImgRecovery.alpha = ALPHA_ENABLE
            binding.picHideImgDel.alpha = ALPHA_ENABLE
        } else {
            binding.picHideImgRecovery.alpha = ALPHA_DISABLE
            binding.picHideImgDel.alpha = ALPHA_DISABLE
        }
    }

    /**
     * 设置是否有数据
     *
     * @param groupList
     * @param list
     */
    protected fun setHasData(groupList: List<Any>, list: List<Any>) {
        var hasData = false
        if (groupList.isNotEmpty() || list.isNotEmpty()) hasData = true

        if (hasData) {
            binding.picHideBtnPreview.alpha = ALPHA_ENABLE
            binding.fileBottomLayoutTips.visibility = View.GONE
        } else {
            binding.picHideBtnPreview.alpha = ALPHA_DISABLE
            if (mIsEdit) {
                binding.fileBottomLayoutTips.visibility = View.GONE
            } else {
                binding.fileBottomLayoutTips.visibility = View.VISIBLE
            }
        }
    }
}