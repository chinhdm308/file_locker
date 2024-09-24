package com.base.presentation.ui.filehide

import android.content.Intent
import com.base.presentation.R
import com.base.presentation.base.BaseHideActivity
import com.base.presentation.baseadapter.BaseHideAdapter
import com.base.presentation.models.HideFileExt
import com.base.presentation.ui.filepreview.FilePreViewActivity
import com.base.presentation.service.FileService
import com.base.presentation.service.GroupFileService
import com.base.domain.models.file.GroupFile
import com.base.domain.models.file.HideFile
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import javax.inject.Inject


@AndroidEntryPoint
class FileHideActivity : BaseHideActivity(), BaseHideAdapter.OnListener {

    @Inject
    lateinit var fileService: FileService

    @Inject
    lateinit var groupFileService: GroupFileService

    override fun initUI() {
        super.initUI()

        setTitleRID(
            R.string.file_preview_title,
            R.string.file_preview_title_edit
        )

        binding.fileBottomTxtTips.setText(R.string.file_hide_txt_add_file)

        setRidStringType(R.string.file_preview)
    }

    override fun initAdapter() {
        mBaseHideAdapter = FileHideAdapter(this, this)
        binding.hideViewList.adapter = mBaseHideAdapter
    }

    /**
     * 删除文件
     *
     * @return
     */
    @Suppress("UNCHECKED_CAST")
    override fun delFiles() = runBlocking {
        val list = mBaseHideAdapter?.getHitFiles() as? List<HideFileExt>
        if (list != null) for (imageModelView in list) {
            fileService.unHideFile(imageModelView)
            fileService.deleteFileByPath(imageModelView.oldPathUrl)
        }
    }

    /**
     * 添加文件夹
     */
    override fun addFolder() {

    }

    /**
     * 删除文件
     *
     * @return
     */
    @Suppress("UNCHECKED_CAST")
    override fun delFolder(): Boolean = runBlocking {
        val list = mBaseHideAdapter?.getHitFiles() as? List<HideFileExt>
        if (list != null) for (imageModelView in list) {
            fileService.unHideFile(imageModelView)
            fileService.deleteFileByPath(imageModelView.oldPathUrl)
        }
        return@runBlocking false
    }

    @Suppress("UNCHECKED_CAST")
    override fun recoveryFiles() = runBlocking {
        val list = mBaseHideAdapter?.getHitFiles() as? List<HideFileExt>
        if (list != null) for (imageModelView in list) {
            fileService.unHideFile(imageModelView)
        }
    }

    /**
     * 添加新内容
     */
    override fun addFile() {
        // 隐藏图片
        val intent = Intent(this@FileHideActivity, FilePreViewActivity::class.java)
        intent.putExtra("beyondGroupId", mBaseHideAdapter?.getGroupID())
        startActivity(intent)
    }

    @Suppress("UNCHECKED_CAST")
    override fun openHolder(groupID: Int) = runBlocking {
        val groupList = groupFileService.getGroupFiles(groupID)
        val list = fileService.getHideFiles(groupID) as List<HideFile>

        mBaseHideAdapter?.setHitFiles(groupList, list, groupID)
        setHasData(groupList, list)
    }

    override fun openHolder(groupImage: Any?) {
        val data = groupImage as? GroupFile

        var groupId: Int = BaseHideAdapter.ROOT_FOLDER
        if (data != null) {
            groupId = data.getId()!!.toInt()
        }
        openHolder(groupId)
    }
}