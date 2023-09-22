package com.base.basemvvmcleanarchitecture.presentation.ui.videohide

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.viewpager.widget.PagerAdapter
import com.base.basemvvmcleanarchitecture.R
import com.base.basemvvmcleanarchitecture.base.BaseActivity
import com.base.basemvvmcleanarchitecture.databinding.ActivityPlayVideoPreViewBinding
import com.base.basemvvmcleanarchitecture.models.HideImageExt
import com.base.basemvvmcleanarchitecture.presentation.ui.photopreview.PhotoPreViewActivity


class PlayVideoPreViewActivity : BaseActivity<ActivityPlayVideoPreViewBinding>(ActivityPlayVideoPreViewBinding::inflate) {
    private lateinit var mSamplePagerAdapter: SamplePagerAdapter

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)

        mSamplePagerAdapter = SamplePagerAdapter(listOf())

        val index: Int = intent.getIntExtra("id", -1)
        val list: List<String> = intent.getStringArrayListExtra("list")?.toList() ?: emptyList()

        mSamplePagerAdapter.setList(list)
        binding.filePreviewViewpager.adapter = mSamplePagerAdapter

        if (index != -1) {
            binding.filePreviewViewpager.setCurrentItem(index, true)
        }

        binding.viewpageTitle.isVisible = true
    }

    override fun setOnClick() {
        super.setOnClick()

        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    inner class SamplePagerAdapter(listPath: List<String>) : PagerAdapter() {
        private var list: List<String> = listPath

        override fun getCount(): Int {
            return list.size
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view == `object`
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View?)
        }

        override fun getItemPosition(`object`: Any): Int {
            return POSITION_NONE
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val videoView = VideoView(container.context)
            videoView.setVideoPath(list[position])

            val mediaController = MediaController(container.context)
            mediaController.setAnchorView(videoView)
            mediaController.setMediaPlayer(videoView)

            videoView.setMediaController(mediaController)

            // Now just add PhotoView to ViewPager and return it
            container.addView(
                videoView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )

            return videoView
        }

        fun setList(list: List<String>) {
            this.list = list
            notifyDataSetChanged()
        }

        /**
         * 获取当前列表
         *
         * @return
         */
        fun getList(): List<String> {
            return list
        }

    }
}