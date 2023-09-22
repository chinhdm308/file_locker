package com.base.basemvvmcleanarchitecture.presentation.ui.imagehide

import android.content.Intent
import androidx.recyclerview.widget.GridLayoutManager
import com.base.basemvvmcleanarchitecture.R
import com.base.basemvvmcleanarchitecture.base.BaseHideActivity
import com.base.basemvvmcleanarchitecture.baseadapter.BaseHideAdapter
import com.base.basemvvmcleanarchitecture.models.HideImageExt
import com.base.basemvvmcleanarchitecture.presentation.ui.picpreview.PicPreViewActivity
import com.base.basemvvmcleanarchitecture.service.GroupImageService
import com.base.basemvvmcleanarchitecture.service.ImageService
import com.base.basemvvmcleanarchitecture.utils.AdaptiveSpacingItemDecoration
import com.base.domain.models.image.GroupImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import javax.inject.Inject


@AndroidEntryPoint
class PicHideActivity : BaseHideActivity(), BaseHideAdapter.OnListener {

    @Inject
    lateinit var imageService: ImageService

    @Inject
    lateinit var groupImageService: GroupImageService

    override fun initUI() {
        setTitleRID(
            R.string.pic_preview_title,
            R.string.pic_preview_title_edit
        )
        binding.fileBottomTxtTips.setText(R.string.file_hide_txt_add_pic)

        setRidStringType(R.string.pic_preview)
    }

    override fun initAdapter() {
        mBaseHideAdapter = PicHideAdapter(this, this)

        val spacing = 25

        binding.hideViewList.apply {
            setHasFixedSize(true)
            clipToPadding = false
            clipChildren = false
            layoutManager = GridLayoutManager(this@PicHideActivity, 3)
            addItemDecoration(AdaptiveSpacingItemDecoration(spacing, true))
            adapter = mBaseHideAdapter
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun delFiles() = runBlocking {
        val list: List<HideImageExt> = mBaseHideAdapter!!.getHitFiles() as List<HideImageExt>
        list.forEach {
            imageService.deleteImageByPath(it)
        }
    }

    override fun addFolder() {

    }

    override fun delFolder(): Boolean {
        return false
    }

    @Suppress("UNCHECKED_CAST")
    override fun recoveryFiles() = runBlocking {
        val list: List<HideImageExt> = mBaseHideAdapter!!.getHitFiles() as List<HideImageExt>
        list.forEach {
            imageService.unHideImage(it)
        }
    }

    override fun addFile() {
        val intent = Intent(this@PicHideActivity, PicPreViewActivity::class.java)
        intent.putExtra("beyondGroupId", mBaseHideAdapter?.getGroupID())
        startActivity(intent)
    }

    override fun openHolder(groupID: Int) = runBlocking {
        val groupList = groupImageService.getGroupFiles(groupID)
        val list = imageService.getHideImages(groupID)

        mBaseHideAdapter?.setHitFiles(groupList, list, groupID)
        setHasData(groupList, list)
    }

    override fun openHolder(groupImage: Any?) {
        val data = groupImage as? GroupImage
        var groupId = BaseHideAdapter.ROOT_FOLDER
        if (data != null) {
            data.getId()?.let {
                groupId = it.toInt()
            }
        }
        openHolder(groupId)
    }

}