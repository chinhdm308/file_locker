package com.base.presentation.ui.videopreview

import com.base.presentation.R
import com.base.presentation.base.BasePreViewActivity
import com.base.presentation.models.VideoModelExt
import com.base.presentation.service.VideoService
import com.base.domain.models.video.VideoModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class VideoPreViewActivity : BasePreViewActivity() {

    @Inject
    lateinit var videoService: VideoService

    private lateinit var mVideoPreViewAdapter: VideoPreViewAdapter

    override fun initAdapter() {
        mVideoPreViewAdapter = VideoPreViewAdapter(this, this, listOf())
        binding.hideViewList.apply {
            setHasFixedSize(true)
            adapter = mVideoPreViewAdapter
        }

        @Suppress("UNCHECKED_CAST")
        mVideoPreViewAdapter.setPreViewFiles(
            VideoModelExt.transList(videoService.getList() as List<VideoModel>)
        )
    }

    override fun initUI() {

    }

    override fun initListener() {
        super.initListener()

        setTitleRID(R.string.video_preview_title_add)

        binding.itemFileCheckboxAll.setOnCheckedChangeListener { buttonView, isChecked ->
            mVideoPreViewAdapter.selectAll(isChecked)
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun hideFiles() {
        runBlocking {
            // 隐藏图片
            val list = mVideoPreViewAdapter.getEnablePreViewFiles() as List<VideoModelExt>
            for (imageModelView in list) {
                videoService.hideVideo(imageModelView, mBeyondGroupId.toInt())
            }
        }
    }


}