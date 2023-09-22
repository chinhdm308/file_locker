package com.base.data.di

import android.app.Application
import com.base.data.local.database.LocalDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(
        app: Application,
    ): LocalDatabase = LocalDatabase.getInstance(app)

    @Provides
    @Singleton
    fun provideHideAudioDao(database: LocalDatabase) = database.hideAudioDao()

    @Provides
    @Singleton
    fun provideGroupAudioDao(database: LocalDatabase) = database.groupAudioDao()

    @Provides
    @Singleton
    fun provideHideFileDao(database: LocalDatabase) = database.hideFileDao()

    @Provides
    @Singleton
    fun provideGroupFileDao(database: LocalDatabase) = database.groupFileDao()

    @Provides
    @Singleton
    fun provideHideImageDao(database: LocalDatabase) = database.hideImageDao()

    @Provides
    @Singleton
    fun provideGroupImageDao(database: LocalDatabase) = database.groupImageDao()

    @Provides
    @Singleton
    fun provideHideVideoDao(database: LocalDatabase) = database.hideVideoDao()

    @Provides
    @Singleton
    fun provideGroupVideoDao(database: LocalDatabase) = database.groupVideoDao()
}