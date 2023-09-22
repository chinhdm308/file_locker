package com.base.basemvvmcleanarchitecture.presentation.ui.audiopreview

import com.base.basemvvmcleanarchitecture.R
import com.base.basemvvmcleanarchitecture.base.BasePreViewActivity
import com.base.basemvvmcleanarchitecture.models.AudioModelExt
import com.base.basemvvmcleanarchitecture.service.AudioService
import com.base.domain.models.audio.AudioModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject


@AndroidEntryPoint
class AudioPreViewActivity : BasePreViewActivity() {

    @Inject
    lateinit var audioService: AudioService

    private lateinit var mAudioPreViewAdapter: AudioPreViewAdapter

    override fun initAdapter() {
        mAudioPreViewAdapter = AudioPreViewAdapter(this, this, listOf())
        binding.hideViewList.apply {
            setHasFixedSize(true)
            adapter = mAudioPreViewAdapter
        }

        @Suppress("UNCHECKED_CAST")
        mAudioPreViewAdapter.setPreViewFiles(
            AudioModelExt.transList(audioService.getList() as List<AudioModel>)
        )
    }

    override fun initUI() {

    }

    override fun initListener() {
        super.initListener()

        setTitleRID(R.string.audio_preview_title_add)

        binding.itemFileCheckboxAll.setOnCheckedChangeListener { buttonView, isChecked ->
            mAudioPreViewAdapter.selectAll(isChecked)
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun hideFiles() {
        runBlocking {
            // 隐藏图片
            val list = mAudioPreViewAdapter.getEnablePreViewFiles() as List<AudioModelExt>
            for (imageModelView in list) {
                audioService.hideAudio(imageModelView, mBeyondGroupId.toInt())
            }
        }
    }


}