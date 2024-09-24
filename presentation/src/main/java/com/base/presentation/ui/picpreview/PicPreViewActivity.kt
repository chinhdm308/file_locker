package com.base.presentation.ui.picpreview

import androidx.recyclerview.widget.GridLayoutManager
import com.base.presentation.R
import com.base.presentation.base.BasePreViewActivity
import com.base.presentation.models.ImageModelExt
import com.base.presentation.service.ImageService
import com.base.presentation.utils.AdaptiveSpacingItemDecoration
import com.base.domain.models.image.ImageModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class PicPreViewActivity : BasePreViewActivity() {

    @Inject
    lateinit var imageService: ImageService

    private lateinit var mPicPreViewAdapter: PicPreViewAdapter

    override fun initAdapter() {
        mPicPreViewAdapter = PicPreViewAdapter(this, this, listOf())

        val spacing = 25

        binding.hideViewList.apply {
//            setPadding(spacing, spacing, spacing, spacing)
            setHasFixedSize(true)
            clipToPadding = false
            clipChildren = false
            layoutManager = GridLayoutManager(this@PicPreViewActivity, 3)
            addItemDecoration(AdaptiveSpacingItemDecoration(spacing, true))

            adapter = mPicPreViewAdapter
        }


        @Suppress("UNCHECKED_CAST")
        mPicPreViewAdapter.setPreViewFiles(
            ImageModelExt.transList(imageService.getList() as List<ImageModel>)
        )
    }

    override fun initUI() {
        setTitleRID(R.string.pic_preview_title_add)
    }

    @Suppress("UNCHECKED_CAST")
    override fun hideFiles() = runBlocking {
        val list = mPicPreViewAdapter.getEnablePreViewFiles() as? List<ImageModelExt>
        if (list != null) for (imageModelView in list) {
            imageService.hideImage(imageModelView, mBeyondGroupId.toInt())
        }
    }

    override fun initListener() {
        super.initListener()

        binding.itemFileCheckboxAll.setOnCheckedChangeListener { buttonView, isChecked ->
            mPicPreViewAdapter.selectAll(isChecked)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mPicPreViewAdapter.clear()
    }
}