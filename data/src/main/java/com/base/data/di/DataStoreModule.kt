package com.base.data.di

import com.base.data.local.datastore.DataStoreManager
import com.base.data.local.datastore.DataStoreRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataStoreModule {
    @Binds
    abstract fun provideDataStoreRepository(dataStoreManager: DataStoreManager): DataStoreRepository
}
