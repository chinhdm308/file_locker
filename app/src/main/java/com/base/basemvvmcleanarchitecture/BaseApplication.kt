package com.base.basemvvmcleanarchitecture

import android.app.Activity
import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.base.basemvvmcleanarchitecture.presentation.ui.patterncheck.PatternCheckActivity
import com.base.basemvvmcleanarchitecture.utils.AppConstants
import com.base.basemvvmcleanarchitecture.utils.helper.file.FileManager
import com.base.data.local.datastore.DataStoreRepository
import com.base.domain.usecases.SetLastAppEnterPwdStateUseCase
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import com.nostra13.universalimageloader.core.assist.QueueProcessingType
import com.nostra13.universalimageloader.utils.StorageUtils
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import java.io.File
import javax.inject.Inject


/**
 * extending android application class in order to use Hilt dependency injection
 */
@HiltAndroidApp
class BaseApplication : Application(), LifecycleEventObserver, Application.ActivityLifecycleCallbacks {

    @Inject
    lateinit var dataStoreRepository: DataStoreRepository

    @Inject
    lateinit var setLastAppEnterPwdStateUseCase: SetLastAppEnterPwdStateUseCase

    @Inject
    lateinit var fileManager: FileManager

    private var appFirstSettingInstanceDone = false

    private var screenOnOffReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                Intent.ACTION_SCREEN_ON -> {
                    Timber.d("ACTION_SCREEN_ON")
                }

                Intent.ACTION_SCREEN_OFF -> {
                    Timber.d("ACTION_SCREEN_OFF")
                }
            }
        }
    }

    private var isAppBackgrounded = false

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        registerActivityLifecycleCallbacks(this)

        initLoadImage()
        registerScreenReceiver()

        runBlocking {
            appFirstSettingInstanceDone = dataStoreRepository.getAppFirstSettingInstanceDone()
            if (!appFirstSettingInstanceDone) {
                setLastAppEnterPwdStateUseCase.execute(
                    lastAppEnterCorrectPwd = true,
                    lastAppEnterPwdLeaverDateMilliseconds = 0,
                    lastAppEnterPwdErrorCount = 0,
                    lastAppEnterPwdDelayTime = 0
                )
                dataStoreRepository.setNumOfTimesEnterIncorrectPwd(3)
                dataStoreRepository.setFingerPrintEnable(false)
                dataStoreRepository.setIntrudersCatcherEnable(false)
                dataStoreRepository.setPlayWarringSoundState(true)
            }
        }
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_START -> onAppForegrounded()
            Lifecycle.Event.ON_STOP -> onAppBackgrounded()
            else -> {}
        }
    }

    override fun onTerminate() {
        Timber.d("onTerminate")
        unregisterScreenReceiver()
        super.onTerminate()
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}

    override fun onActivityStarted(activity: Activity) {}

    override fun onActivityResumed(activity: Activity) {}

    override fun onActivityPaused(activity: Activity) {}

    override fun onActivityStopped(activity: Activity) {
//        if (activity !is PatternLockActivity) {
//            activity.finish()
//        }
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

    override fun onActivityDestroyed(activity: Activity) {}

    private fun registerScreenReceiver() {
        val screenFilter = IntentFilter()
        screenFilter.addAction(Intent.ACTION_SCREEN_ON)
        screenFilter.addAction(Intent.ACTION_SCREEN_OFF)
        registerReceiver(screenOnOffReceiver, screenFilter)
    }

    private fun unregisterScreenReceiver() {
        unregisterReceiver(screenOnOffReceiver)
    }

    /**
     * Initialize the tool for reading images
     */
    private fun initLoadImage() {
//        var cacheDir: File? = fileManager.getCacheFile(this)
        var cacheDir: File? = fileManager.getCacheDir()
        if (cacheDir == null) {
            cacheDir = StorageUtils.getCacheDirectory(this)
        }
        val cacheSize = 1024 * 1024 * 50
        val count = 100
        val maxImageMemory = (Runtime.getRuntime().maxMemory() / 3).toInt()
        val builder = ImageLoaderConfiguration.Builder(this)
            .threadPriority(Thread.NORM_PRIORITY - 1)
            .threadPoolSize(3)
            .denyCacheImageMultipleSizesInMemory() // 解码图像的大尺寸将在内存中缓存先前解码图像的小尺寸。
            .memoryCache(LruMemoryCache(maxImageMemory))
            .memoryCacheSize(maxImageMemory) // 设置内存缓存的最大大小 默认为一个当前应用可用内存的1/8
            .memoryCacheSizePercentage(13) // 设置内存缓存最大大小占当前应用可用内存的百分比 默认为一个当前应用可用内存的1/8
            .memoryCacheExtraOptions(480, 800) // 内存缓存的设置选项 (最大图片宽度,最大图片高度) 默认当前屏幕分辨率
            .diskCache(UnlimitedDiskCache(cacheDir))
            .diskCacheFileNameGenerator(Md5FileNameGenerator()) // 设置磁盘缓存文件名称
            .diskCacheSize(cacheSize).diskCacheFileCount(count)
//            .imageDownloader(ThumbnailImageDownloader(this))
            .tasksProcessingOrder(QueueProcessingType.FIFO) // 设置加载显示图片队列进程 // FIFO 先入先出 // LIFO 后入先出
        ImageLoader.getInstance().init(builder.build())
    }

    private fun onAppForegrounded() = runBlocking {
        Timber.d("onAppForegrounded")
        if (dataStoreRepository.getAppFirstSettingInstanceDone().not()) {
            isAppBackgrounded = false
            return@runBlocking
        }
        if (isAppBackgrounded) {
            isAppBackgrounded = false
            val intent = Intent(this@BaseApplication, PatternCheckActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra(AppConstants.EXTRA_TO_APP, true)
            startActivity(intent)
        }
    }

    private fun onAppBackgrounded() {
        Timber.d("onAppBackgrounded")
        isAppBackgrounded = true
    }
}