package com.base.basemvvmcleanarchitecture.service

import com.base.domain.models.video.GroupVideo
import com.base.domain.usecases.groupvideo.DeleteGroupVideoUseCase
import com.base.domain.usecases.groupvideo.GetGroupVideosByBeyondGroupIdUseCase
import com.base.domain.usecases.groupvideo.InsertGroupVideoUseCase
import javax.inject.Inject


class GroupVideoService @Inject constructor(
    private val getGroupVideosByBeyondGroupIdUseCase: GetGroupVideosByBeyondGroupIdUseCase,
    private val deleteGroupVideoUseCase: DeleteGroupVideoUseCase,
    private val insertGroupVideoUseCase: InsertGroupVideoUseCase,
) {
    /**
     * 增加一个分组
     */
    suspend fun addGroup(groupVideo: GroupVideo): Boolean {
        return insertGroupVideoUseCase.execute(groupVideo, false) >= 0
    }

    /**
     * 修改一个分组
     */
    suspend fun modifyGroup(groupVideo: GroupVideo): Boolean {
        return insertGroupVideoUseCase.execute(groupVideo, true) >= 0
    }

    /**
     * 删除一个分组
     */
    suspend fun deleteGroup(groupVideo: GroupVideo): Boolean {
        return deleteGroupVideoUseCase.execute(groupVideo) >= 0
    }

    /**
     * 获取所有分组
     */
    suspend fun getGroupFiles(beyondGroupId: Int): List<GroupVideo> {
        return getGroupVideosByBeyondGroupIdUseCase.execute(beyondGroupId)
    }
}