package com.base.presentation.ui.photopreview

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.viewpager.widget.PagerAdapter
import com.base.presentation.R
import com.base.presentation.base.BaseActivity
import com.base.presentation.databinding.ActivityFilePreviewViewpagerBinding
import com.base.presentation.models.HideImageExt
import com.base.presentation.service.ImageService
import com.base.domain.models.image.HideImage
import com.example.photoview.PhotoView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.nostra13.universalimageloader.core.DisplayImageOptions
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.assist.ImageScaleType
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer
import com.nostra13.universalimageloader.core.download.ImageDownloader
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import javax.inject.Inject


@AndroidEntryPoint
class PhotoPreViewActivity :
    BaseActivity<ActivityFilePreviewViewpagerBinding>(ActivityFilePreviewViewpagerBinding::inflate) {

    @Inject
    lateinit var imageService: ImageService

    private lateinit var mSamplePagerAdapter: SamplePagerAdapter

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        window.statusBarColor = getColor(R.color.color_appbar)

        mSamplePagerAdapter = SamplePagerAdapter(listOf())

        val hideImageExt: List<HideImageExt>? =
            intent.parcelableArrayList<HideImageExt>("list")?.toList()
        val index = intent.getIntExtra("id", -1)

        hideImageExt?.let { mSamplePagerAdapter.setList(it) }

        binding.filePreviewViewpager.adapter = mSamplePagerAdapter

        if (index != -1) {
            binding.filePreviewViewpager.setCurrentItem(index, true)
        }

        binding.viewpageTitle.isVisible = true
    }

    override fun viewListener() {
        super.viewListener()

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.picHideImgRecovery.setOnClickListener {
            MaterialAlertDialogBuilder(this)
                .setTitle(getString(R.string.file_dialog_recovery) + getString(R.string.pic_preview))
                .setMessage(getString(R.string.pic_preview) + getString(R.string.file_dialog_recovery_missage))
                .setPositiveButton(R.string.lock_ok) { _, _ ->
                    recoveryFiles()
                }
                .setNegativeButton(R.string.lock_cancel) { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }

        binding.picHideImgDel.setOnClickListener {
            MaterialAlertDialogBuilder(this)
                .setTitle(getString(R.string.file_dialog_del) + getString(R.string.pic_preview))
                .setMessage(getString(R.string.pic_preview) + getString(R.string.file_dialog_del_missage))
                .setPositiveButton(R.string.lock_ok) { _, _ ->
                    delFiles()
                }
                .setNegativeButton(R.string.lock_cancel) { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }

    private fun recoveryFiles() = runBlocking {
        val list = mSamplePagerAdapter.getList().toMutableList()
        if (list.isNotEmpty()) {
            val index = binding.filePreviewViewpager.currentItem
            val hideImageExt = list.removeAt(index) as HideImage
            mSamplePagerAdapter.setList(list)
            binding.filePreviewViewpager.currentItem = index
            imageService.unHideImage(hideImageExt)
        }
    }

    private fun delFiles() = runBlocking {
        val list = mSamplePagerAdapter.getList().toMutableList()
        if (list.isNotEmpty()) {
            val index = binding.filePreviewViewpager.currentItem
            val hideImageExt = list.removeAt(index) as HideImage
            mSamplePagerAdapter.setList(list)
            binding.filePreviewViewpager.currentItem = index
            imageService.deleteImageByPath(hideImageExt)
        }
    }

    inner class SamplePagerAdapter(listImages: List<HideImageExt>) : PagerAdapter() {
        private var list: List<HideImageExt> = listImages

        private val imageLoader = ImageLoader.getInstance()

        private val options: DisplayImageOptions by lazy {
            // Create DisplayImageOptions.Builder() use DisplayImageOptions
            DisplayImageOptions.Builder()
                .cacheInMemory(true) // Set whether downloaded images are cached in memory
                .cacheOnDisk(false) // Set whether downloaded images are cached in the SD card
                .resetViewBeforeLoading(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(FadeInBitmapDisplayer(300))
                .considerExifParams(true)
                .showImageOnLoading(R.drawable.default_picture)
                .showImageForEmptyUri(R.drawable.default_picture)
                .showImageOnFail(R.drawable.default_picture)
                .build() // Create a configured DisplayImageOption object
        } // Display image settings

        override fun getCount(): Int {
            return list.size
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view == `object`
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View?)
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val photoView = PhotoView(container.context)

            imageLoader.displayImage(
                ImageDownloader.Scheme.FILE.wrap(list[position].newPathUrl),
                photoView,
                options
            )

//            Glide.with(container.context).load(list[position].newPathUrl).load(photoView)

            // Now just add PhotoView to ViewPager and return it
            container.addView(
                photoView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )

            return photoView
        }

        override fun getItemPosition(`object`: Any): Int {
            return POSITION_NONE
        }

        fun setList(list: List<HideImageExt>) {
            this.list = list
            notifyDataSetChanged()
        }

        /**
         * 获取当前列表
         *
         * @return
         */
        fun getList(): List<HideImageExt> {
            return list
        }
    }
}

inline fun <reified T : Parcelable> Intent.parcelableArrayList(key: String): ArrayList<T>? = when {
    Build.VERSION.SDK_INT >= 33 -> getParcelableArrayListExtra(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelableArrayListExtra(key)
}

inline fun <reified T : Parcelable> Intent.parcelable(key: String): T? = when {
    Build.VERSION.SDK_INT >= 33 -> getParcelableExtra(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelableExtra(key) as? T
}

inline fun <reified T : Parcelable> Bundle.parcelable(key: String): T? = when {
    Build.VERSION.SDK_INT >= 33 -> getParcelable(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelable(key) as? T
}
