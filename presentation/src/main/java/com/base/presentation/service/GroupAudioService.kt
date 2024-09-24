package com.base.presentation.service

import com.base.domain.models.audio.GroupAudio
import com.base.domain.usecases.groupaudio.DeleteGroupAudioUseCase
import com.base.domain.usecases.groupaudio.GetGroupAudiosByBeyondGroupIdUseCase
import com.base.domain.usecases.groupaudio.InsertGroupAudioUseCase
import javax.inject.Inject


class GroupAudioService @Inject constructor(
    private val getGroupAudiosByBeyondGroupIdUseCase: GetGroupAudiosByBeyondGroupIdUseCase,
    private val deleteGroupAudioUseCase: DeleteGroupAudioUseCase,
    private val insertGroupAudioUseCase: InsertGroupAudioUseCase,
) {
    /**
     * 增加一个分组
     */
    suspend fun addGroup(groupAudio: GroupAudio): Boolean {
        return insertGroupAudioUseCase.execute(groupAudio, false) >= 0
    }

    /**
     * 修改一个分组
     */
    suspend fun modifyGroup(groupAudio: GroupAudio): Boolean {
        return insertGroupAudioUseCase.execute(groupAudio, true) >= 0
    }

    /**
     * 删除一个分组
     */
    suspend fun deleteGroup(groupAudio: GroupAudio): Boolean {
        return deleteGroupAudioUseCase.execute(groupAudio) >= 0
    }

    /**
     * 获取所有分组
     */
    suspend fun getGroupFiles(beyondGroupId: Int): List<GroupAudio> {
        return getGroupAudiosByBeyondGroupIdUseCase.execute(beyondGroupId)
    }
}