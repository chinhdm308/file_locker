package com.base.basemvvmcleanarchitecture.presentation.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.base.basemvvmcleanarchitecture.base.BaseActivity
import com.base.basemvvmcleanarchitecture.databinding.ActivityMainBinding
import com.base.basemvvmcleanarchitecture.presentation.ui.audiohide.AudioHideActivity
import com.base.basemvvmcleanarchitecture.presentation.ui.filehide.FileHideActivity
import com.base.basemvvmcleanarchitecture.presentation.ui.imagehide.PicHideActivity
import com.base.basemvvmcleanarchitecture.presentation.ui.videohide.VideoHideActivity
import com.base.basemvvmcleanarchitecture.service.AudioService
import com.base.basemvvmcleanarchitecture.service.FileService
import com.base.basemvvmcleanarchitecture.service.ImageService
import com.base.basemvvmcleanarchitecture.service.VideoService
import com.base.basemvvmcleanarchitecture.utils.collectLifecycleFlow
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

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

    override fun bindingStateView() {
        super.bindingStateView()

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