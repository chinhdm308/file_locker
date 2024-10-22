package com.base.presentation.ui.filepreview

import android.view.KeyEvent
import com.base.presentation.R
import com.base.presentation.base.BasePreViewActivity
import com.base.presentation.models.FileModelExt
import com.base.presentation.service.FileService
import com.base.presentation.utils.helper.file.FileManager
import com.base.domain.models.file.FileModel
import com.base.presentation.utils.AdaptiveSpacingItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import java.io.File
import javax.inject.Inject


@AndroidEntryPoint
class FilePreViewActivity : BasePreViewActivity(), FilePreViewAdapter.OnFolder {

    @Inject
    lateinit var fileService: FileService

    @Inject
    lateinit var fileManager: FileManager

    private lateinit var mFilePreViewAdapter: FilePreViewAdapter

    /**
     * SD卡根路径
     */
    private val SD_URL by lazy {
        fileManager.getSDPath()
    }

    /**
     * 当前所在的文件夹信息（没人默认为空）
     */
    private var mFile: File? = null

    override fun initAdapter() {
        mFilePreViewAdapter = FilePreViewAdapter(this, this, listOf())
        binding.hideViewList.adapter = mFilePreViewAdapter
        binding.hideViewList.addItemDecoration(AdaptiveSpacingItemDecoration(25))

        openFolder()
    }

    override fun initUI() {

    }

    override fun initListener() {
        super.initListener()

        setTitleRID(R.string.file_preview_title_add)

        binding.itemFileCheckboxAll.setOnCheckedChangeListener { buttonView, isChecked ->
            mFilePreViewAdapter.selectAll(isChecked)
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun hideFiles() = runBlocking {
        // 隐藏图片
        val list = mFilePreViewAdapter.getEnablePreViewFiles() as? List<FileModelExt>
        if (list != null) {
            for (imageModelView in list) {
                fileService.hideFile(imageModelView, mBeyondGroupId.toInt())
            }
        }
    }

    override fun openFolder(fileModel: FileModel?) {
        if (fileModel != null) {
            fileModel.getPath()?.let { openFolder(it) }
        } else {
            SD_URL?.let { openFolder(it) }
        }
    }

    /**
     * 打开某个文件夹
     *
     * @param url
     */
    private fun openFolder(url: String) {
        Timber.i("url: $url")
        openFolder(File(url))
    }

    private fun openFolder(file: File) {
        mFile = file
        val list: List<FileModel>? = fileService.getFilesByDir(file.path)
        if (!list.isNullOrEmpty()) {
            val listTemp = FileModelExt.transList(list).sorted()
            if (listTemp.isNotEmpty()) mFilePreViewAdapter.setPreViewFiles(listTemp)
        }
    }

    private fun openFolder() {
        SD_URL?.let { openFolder(it) }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (onBack()) return true
        }
        return super.onKeyDown(keyCode, event)
    }

    /**
     * 返回/退出页面
     *
     * @return 是否停留
     */
    override fun onBack(): Boolean {
        if (mFile != null && mFile!!.path != SD_URL) {
            mFile!!.parent?.let { openFolder(it) }
            return true
        }
        finish()
        return false
    }
}