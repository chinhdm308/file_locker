package com.base.presentation.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.base.presentation.base.BaseActivity
import com.base.presentation.databinding.ActivityMainBinding
import com.base.presentation.ui.audiohide.AudioHideActivity
import com.base.presentation.ui.filehide.FileHideActivity
import com.base.presentation.ui.imagehide.PicHideActivity
import com.base.presentation.ui.videohide.VideoHideActivity
import com.base.presentation.utils.collectLifecycleFlow
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    private val viewModel by viewModels<MainViewModel>()

    private var boxId: Int = 0

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)

        binding.recyclerViewBoxes.layoutManager = LinearLayoutManager(this)
    }

    override fun onAccessStoragePermission() {
        when (boxId) {
            SafeBoxMgr.BOX_ID_PIC -> {
                startActivity(Intent(this, PicHideActivity::class.java))
            }

            SafeBoxMgr.BOX_ID_VIDEO -> {
                startActivity(Intent(this, VideoHideActivity::class.java))
            }

            SafeBoxMgr.BOX_ID_AUDIO -> {
                startActivity(Intent(this, AudioHideActivity::class.java))
            }

            SafeBoxMgr.BOX_ID_FILE -> {
                startActivity(Intent(this, FileHideActivity::class.java))
            }
        }
    }

    override fun dataObservable() {
        super.dataObservable()

        collectLifecycleFlow(viewModel.uiState) { it ->
            val boxAdapter = BoxAdapter(
                context = this,
                hideImageCount = it[0],
                hideVideoCount = it[1],
                hideAudioCount = it[2],
                hideFileCount = it[3],
            ) {
                boxId = it
                requestStoragePermission()
            }
            boxAdapter.submitList(SafeBoxMgr().getSafeBoxList())
            binding.recyclerViewBoxes.adapter = boxAdapter
        }
    }

    override fun onResume() {
        super.onResume()

        viewModel.init()
    }

}