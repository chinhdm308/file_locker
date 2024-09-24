package com.base.presentation.service

import com.base.domain.models.file.GroupFile
import com.base.domain.usecases.groupfile.DeleteGroupFileUseCase
import com.base.domain.usecases.groupfile.GetGroupFilesUseCase
import com.base.domain.usecases.groupfile.InsertGroupFileUseCase
import javax.inject.Inject


class GroupFileService @Inject constructor(
    private val deleteGroupFileUseCase: DeleteGroupFileUseCase,
    private val insertGroupFileUseCase: InsertGroupFileUseCase,
    private val getGroupFilesUseCase: GetGroupFilesUseCase
) {
    /**
     * 增加一个分组
     */
    suspend fun addGroup(groupFile: GroupFile): Boolean {
        return insertGroupFileUseCase.execute(groupFile, false) >= 0
    }

    /**
     * 修改一个分组
     */
    suspend fun modifyGroup(groupFile: GroupFile): Boolean {
        return insertGroupFileUseCase.execute(groupFile, true) >= 0
    }

    /**
     * 删除一个分组
     */
    suspend

    fun deleteGroup(groupFile: GroupFile): Boolean {
        return deleteGroupFileUseCase.execute(groupFile) > 0
    }

    /**
     * 获取所有分组
     */
    suspend fun getGroupFiles(beyondGroupId: Int): List<GroupFile> {
        return getGroupFilesUseCase.execute(beyondGroupId)
    }
}