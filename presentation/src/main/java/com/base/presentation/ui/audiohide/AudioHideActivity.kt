package com.base.presentation.ui.audiohide

import android.content.Intent
import com.base.presentation.R
import com.base.presentation.base.BaseHideActivity
import com.base.presentation.baseadapter.BaseHideAdapter
import com.base.presentation.models.HideAudioExt
import com.base.presentation.ui.audiopreview.AudioPreViewActivity
import com.base.presentation.service.AudioService
import com.base.presentation.service.GroupAudioService
import com.base.domain.models.audio.GroupAudio
import com.base.domain.models.audio.HideAudio
import com.base.presentation.utils.AdaptiveSpacingItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import javax.inject.Inject


@AndroidEntryPoint
class AudioHideActivity : BaseHideActivity(), BaseHideAdapter.OnListener {

    @Inject
    lateinit var audioService: AudioService

    @Inject
    lateinit var groupAudioService: GroupAudioService

    override fun initUI() {
        super.initUI()

        setTitleRID(
            R.string.audio_preview_title,
            R.string.audio_preview_title_edit,
        )

        binding.fileBottomTxtTips.setText(R.string.file_hide_txt_add_audio)
        setRidStringType(R.string.audio_preview)
    }

    override fun initAdapter() {
        mBaseHideAdapter = AudioHideAdapter(this, this)
        binding.hideViewList.adapter = mBaseHideAdapter
        binding.hideViewList.addItemDecoration(AdaptiveSpacingItemDecoration(25))
    }

    @Suppress("UNCHECKED_CAST")
    override fun delFiles() {
        runBlocking {
            val list: List<HideAudioExt> = mBaseHideAdapter!!.getHitFiles() as List<HideAudioExt>
            list.forEach {
                audioService.deleteAudioByPath(it)
            }
        }
    }

    /**
     * 添加文件夹
     */
    override fun addFolder() {

    }

    override fun delFolder(): Boolean {
        return false
    }

    @Suppress("UNCHECKED_CAST")
    override fun recoveryFiles() {
        runBlocking {
            val list: List<HideAudioExt> = mBaseHideAdapter!!.getHitFiles() as List<HideAudioExt>
            list.forEach {
                audioService.unHideAudio(it)
            }
        }
    }

    override fun addFile() {
        // 隐藏图片
        val intent = Intent(this@AudioHideActivity, AudioPreViewActivity::class.java)
        intent.putExtra("beyondGroupId", mBaseHideAdapter!!.getGroupID())
        startActivity(intent)
    }

    override fun openHolder(groupID: Int) {
        runBlocking {
            val groupList: List<GroupAudio> = groupAudioService.getGroupFiles(groupID)
            val list: List<HideAudio> = audioService.getHideAudios(groupID)

            mBaseHideAdapter?.setHitFiles(groupList, list, groupID)
            setHasData(groupList, list)
        }
    }

    override fun openHolder(groupImage: Any?) {
        val data: GroupAudio? = groupImage as? GroupAudio
        var groupID = BaseHideAdapter.ROOT_FOLDER
        if (data != null) {
            groupID = data.getId()!!.toInt()
        }
        openHolder(groupID)
    }
}