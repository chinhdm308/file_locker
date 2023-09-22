package com.base.data.local.database

import android.content.Context
import android.os.Environment
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.base.data.local.dao.HideAudioDao
import com.base.data.local.dao.GroupAudioDao
import com.base.data.local.dao.GroupFileDao
import com.base.data.local.dao.GroupImageDao
import com.base.data.local.dao.GroupVideoDao
import com.base.data.local.dao.HideFileDao
import com.base.data.local.dao.HideImageDao
import com.base.data.local.dao.HideVideoDao
import com.base.data.local.entities.GroupAudioEntity
import com.base.data.local.entities.GroupFileEntity
import com.base.data.local.entities.GroupImageEntity
import com.base.data.local.entities.GroupVideoEntity
import com.base.data.local.entities.HideAudioEntity
import com.base.data.local.entities.HideFileEntity
import com.base.data.local.entities.HideImageEntity
import com.base.data.local.entities.HideVideoEntity
import com.base.data.utils.CacheConstants
import com.base.data.utils.Migrations

@Database(
    entities = [
        HideAudioEntity::class,
        GroupAudioEntity::class,
        HideFileEntity::class,
        GroupFileEntity::class,
        HideImageEntity::class,
        GroupImageEntity::class,
        HideVideoEntity::class,
        GroupVideoEntity::class
    ],
    version = Migrations.DB_VERSION,
    exportSchema = false
)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun hideAudioDao(): HideAudioDao
    abstract fun groupAudioDao(): GroupAudioDao
    abstract fun hideFileDao(): HideFileDao
    abstract fun groupFileDao(): GroupFileDao
    abstract fun hideImageDao(): HideImageDao
    abstract fun groupImageDao(): GroupImageDao
    abstract fun hideVideoDao(): HideVideoDao
    abstract fun groupVideoDao(): GroupVideoDao

    companion object {
        @Volatile
        private var INSTANCE: LocalDatabase? = null

        fun getInstance(applicationContext: Context): LocalDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(applicationContext).also { INSTANCE = it }
            }

        private val DB_PATH = Environment.getExternalStorageDirectory().path + "/.byte2e_file_hide/.db/"

        private fun buildDatabase(applicationContext: Context) = Room.databaseBuilder(
            applicationContext,
            LocalDatabase::class.java,
            DB_PATH + CacheConstants.DB_NAME
        ).fallbackToDestructiveMigration()
//            .allowMainThreadQueries()
            .build()
    }
}