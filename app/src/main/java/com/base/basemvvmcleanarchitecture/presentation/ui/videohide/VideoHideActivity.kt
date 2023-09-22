package com.base.basemvvmcleanarchitecture.presentation.ui.videohide

import android.content.Intent
import com.base.basemvvmcleanarchitecture.R
import com.base.basemvvmcleanarchitecture.base.BaseHideActivity
import com.base.basemvvmcleanarchitecture.baseadapter.BaseHideAdapter
import com.base.basemvvmcleanarchitecture.models.HideVideoExt
import com.base.basemvvmcleanarchitecture.presentation.ui.videopreview.VideoPreViewActivity
import com.base.basemvvmcleanarchitecture.service.GroupVideoService
import com.base.basemvvmcleanarchitecture.service.VideoService
import com.base.domain.models.video.GroupVideo
import com.base.domain.models.video.HideVideo
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class VideoHideActivity : BaseHideActivity(), BaseHideAdapter.OnListener {

    @Inject
    lateinit var videoService: VideoService

    @Inject
    lateinit var groupVideoService: GroupVideoService

    override fun initUI() {
        super.initUI()

        setTitleRID(
            R.string.video_preview_title,
            R.string.video_preview_title_edit
        )

        binding.fileBottomTxtTips.setText(R.string.file_hide_txt_add_video)

        setRidStringType(R.string.video_preview)
    }

    override fun initAdapter() {
        mBaseHideAdapter = VideoHideAdapter(this, this)
        binding.hideViewList.adapter = mBaseHideAdapter
    }

    @Suppress("UNCHECKED_CAST")
    override fun delFiles() {
        runBlocking {
            val list: List<HideVideoExt> = mBaseHideAdapter!!.getHitFiles() as List<HideVideoExt>
            list.forEach {
                videoService.deleteVideoByPath(it)
            }
        }
    }

    override fun addFolder() {

    }

    override fun delFolder(): Boolean {
        return false
    }

    @Suppress("UNCHECKED_CAST")
    override fun recoveryFiles() {
        runBlocking {
            val list: List<HideVideoExt> = mBaseHideAdapter!!.getHitFiles() as List<HideVideoExt>
            list.forEach {
                videoService.unHideVideo(it)
            }
        }
    }

    override fun addFile() {
        val intent = Intent(this@VideoHideActivity, VideoPreViewActivity::class.java)
        intent.putExtra("beyondGroupId", mBaseHideAdapter!!.getGroupID())
        startActivity(intent)
    }

    override fun openHolder(groupID: Int) = runBlocking {
        val groupList: List<GroupVideo> = groupVideoService.getGroupFiles(groupID)
        val list: List<HideVideo> = videoService.getHideVideos(groupID)

        mBaseHideAdapter?.setHitFiles(groupList, list, groupID)
        setHasData(groupList, list)
    }

    override fun openHolder(groupImage: Any?) {
        val data: GroupVideo? = groupImage as? GroupVideo
        var groupID = BaseHideAdapter.ROOT_FOLDER
        if (data != null) {
            groupID = data.getId()!!.toInt()
        }
        openHolder(groupID)
    }
}