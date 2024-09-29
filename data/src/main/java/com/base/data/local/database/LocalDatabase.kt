package com.base.data.local.database

import android.content.Context
import android.os.Environment
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.base.data.local.database.hideaudio.HideAudioDao
import com.base.data.local.database.groupaudio.GroupAudioDao
import com.base.data.local.database.groupfile.GroupFileDao
import com.base.data.local.database.groupimage.GroupImageDao
import com.base.data.local.database.groupvideo.GroupVideoDao
import com.base.data.local.database.hidefile.HideFileDao
import com.base.data.local.database.hideimage.HideImageDao
import com.base.data.local.database.hidevideo.HideVideoDao
import com.base.data.local.database.pattern.PatternDao
import com.base.data.local.database.groupaudio.GroupAudioEntity
import com.base.data.local.database.groupfile.GroupFileEntity
import com.base.data.local.database.groupimage.GroupImageEntity
import com.base.data.local.database.groupvideo.GroupVideoEntity
import com.base.data.local.database.hideaudio.HideAudioEntity
import com.base.data.local.database.hidefile.HideFileEntity
import com.base.data.local.database.hideimage.HideImageEntity
import com.base.data.local.database.hidevideo.HideVideoEntity
import com.base.data.local.database.pattern.PatternEntity
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
        GroupVideoEntity::class,
        PatternEntity::class
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
    abstract fun patternDao(): PatternDao

    companion object {
        @Volatile
        private var INSTANCE: LocalDatabase? = null

        fun getInstance(applicationContext: Context): LocalDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(applicationContext).also { INSTANCE = it }
            }

        private val DB_PATH = Environment.getExternalStorageDirectory().path + "/.chinchin_file_hide/.db/"

        private fun buildDatabase(applicationContext: Context) = Room.databaseBuilder(
            applicationContext,
            LocalDatabase::class.java,
            DB_PATH + CacheConstants.DB_NAME
        ).fallbackToDestructiveMigration()
//            .allowMainThreadQueries()
            .build()
    }
}