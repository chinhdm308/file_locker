package com.base.presentation.base

import android.os.Bundle
import com.base.presentation.R
import com.base.presentation.baseadapter.BaseHideAdapter
import com.base.presentation.baseadapter.BasePreViewAdapter
import com.base.presentation.databinding.ActivityFilePreviewBinding


abstract class BasePreViewActivity : BaseActivity<ActivityFilePreviewBinding>(ActivityFilePreviewBinding::inflate),
    BasePreViewAdapter.OnListener {

    /**
     * 当前选中的文件夹ID标识
     */
    protected var mBeyondGroupId: Long = 0

    override fun initView(savedInstanceState: Bundle?) {
        window.statusBarColor = getColor(R.color.color_toolbar_hide_file)
        super.initView(savedInstanceState)

        initUI()
        initAdapter()
        initListener()
    }

    protected open fun initListener() {
        binding.previewBtnHide.setOnClickListener {
            if (it.alpha == BaseHideActivity.ALPHA_ENABLE) {
                hideFiles()
                finish()
            }
        }

        binding.btnBack.setOnClickListener {
            onBack()
        }
    }

    abstract fun initAdapter()

    abstract fun initUI()

    override fun onStart() {
        super.onStart()
        mBeyondGroupId = intent.getIntExtra("beyondGroupId", BaseHideAdapter.ROOT_FOLDER).toLong()
        setSelect(false)
    }

    /**
     * 按返回键
     */
    protected open fun onBack(): Boolean {
        finish()
        return true
    }

    /**
     * 隐藏当前选中项
     */
    abstract fun hideFiles()

    protected open fun setTitleRID(titleRID: Int) {
        binding.fileHideTxtTitle.setText(titleRID)
    }

    override fun setSelect(selected: Boolean) {
        if (selected) {
            binding.previewBtnHide.alpha = BaseHideActivity.ALPHA_ENABLE
        } else {
            binding.previewBtnHide.alpha = BaseHideActivity.ALPHA_DISABLE
        }
    }
}