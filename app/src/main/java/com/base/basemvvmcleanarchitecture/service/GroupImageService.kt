package com.base.basemvvmcleanarchitecture.service

import com.base.domain.models.image.GroupImage
import com.base.domain.usecases.groupimage.DeleteGroupImageUseCase
import com.base.domain.usecases.groupimage.GetGroupImagesByBeyondGroupIdUseCase
import com.base.domain.usecases.groupimage.InsertGroupImageUseCase
import javax.inject.Inject

class GroupImageService @Inject constructor(
    private val insertGroupImageUseCase: InsertGroupImageUseCase,
    private val deleteGroupImageUseCase: DeleteGroupImageUseCase,
    private val getGroupImagesByBeyondGroupIdUseCase: GetGroupImagesByBeyondGroupIdUseCase
) {
    /**
     * 增加一个分组
     */
    suspend fun addGroup(groupImage: GroupImage): Boolean {
        return insertGroupImageUseCase.execute(groupImage, false) >= 0
    }

    /**
     * 修改一个分组
     */
    suspend fun modifyGroup(groupImage: GroupImage): Boolean {
        return insertGroupImageUseCase.execute(groupImage, true) >= 0
    }

    /**
     * 删除一个分组
     */
    suspend fun deleteGroup(groupImage: GroupImage): Boolean {
        return deleteGroupImageUseCase.execute(groupImage) >= 0
    }

    /**
     * 获取所有分组
     */
    suspend fun getGroupFiles(beyondGroupId: Int): List<GroupImage> {
        return getGroupImagesByBeyondGroupIdUseCase.execute(beyondGroupId)
    }
}