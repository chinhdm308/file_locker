package com.base.data.di

import com.base.data.repositories.GroupAudioRepoImpl
import com.base.data.repositories.GroupFileRepoImpl
import com.base.data.repositories.GroupImageRepoImpl
import com.base.data.repositories.GroupVideoRepoImpl
import com.base.data.repositories.HideAudioRepoImpl
import com.base.data.repositories.HideFileRepoImpl
import com.base.data.repositories.HideImageRepoImpl
import com.base.data.repositories.HideVideoRepoImpl
import com.base.data.repositories.PatternRepoImpl
import com.base.data.repositories.SetLastAppEnterPwdStateRepoImpl
import com.base.domain.repositories.GroupAudioRepository
import com.base.domain.repositories.GroupFileRepository
import com.base.domain.repositories.GroupImageRepository
import com.base.domain.repositories.GroupVideoRepository
import com.base.domain.repositories.HideAudioRepository
import com.base.domain.repositories.HideFileRepository
import com.base.domain.repositories.HideImageRepository
import com.base.domain.repositories.HideVideoRepository
import com.base.domain.repositories.PatternRepository
import com.base.domain.repositories.SetLastAppEnterPwdStateRepo
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun bindHideAudioRepository(hideAudioRepoImpl: HideAudioRepoImpl): HideAudioRepository

    @Binds
    fun bindGroupAudioRepository(groupAudioRepoImpl: GroupAudioRepoImpl): GroupAudioRepository

    @Binds
    fun bindHideFileRepository(hideFileRepoImpl: HideFileRepoImpl): HideFileRepository

    @Binds
    fun bindGroupFileRepository(groupFileRepoImpl: GroupFileRepoImpl): GroupFileRepository

    @Binds
    fun bindHideImageRepository(hideImageRepoImpl: HideImageRepoImpl): HideImageRepository

    @Binds
    fun bindGroupImageRepository(groupImageRepoImpl: GroupImageRepoImpl): GroupImageRepository

    @Binds
    fun bindHideVideoRepository(hideVideoRepoImpl: HideVideoRepoImpl): HideVideoRepository

    @Binds
    fun bindGroupVideoRepository(groupVideoRepoImpl: GroupVideoRepoImpl): GroupVideoRepository

    @Binds
    fun bindSetLastAppEnterPwdStateRepo(setLastAppEnterPwdStateRepoImpl: SetLastAppEnterPwdStateRepoImpl): SetLastAppEnterPwdStateRepo

    @Binds
    fun bindPatternRepository(patternRepositoryImpl: PatternRepoImpl): PatternRepository

}