package com.base.admob

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.base.admob.utils.AdmobConstants
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import java.util.Date


class AppOpenAdManager() : LifecycleEventObserver, Application.ActivityLifecycleCallbacks {

    companion object {
        private const val LOG_TAG = "AppOpenManager"
        private const val AD_UNIT_ID = "ca-app-pub-3940256099942544/3419835294"
    }

    private var appOpenAd: AppOpenAd? = null
    private var isLoadingAd = false
    private var isShowingAd = false

    /** Keep track of the time an app open ad is loaded to ensure you don't show an expired ad. */
    private var loadTime: Long = 0

    private var currentActivity: Activity? = null

    constructor(myApplication: Application) : this() {
        myApplication.registerActivityLifecycleCallbacks(this)
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {

    }

    override fun onActivityStarted(activity: Activity) {
        // Updating the currentActivity only when an ad is not showing.
        if (!isShowingAd) {
            currentActivity = activity
        }
    }

    override fun onActivityResumed(activity: Activity) {
        if (!isShowingAd) {
            currentActivity = activity
        }
    }

    override fun onActivityPaused(activity: Activity) {

    }

    override fun onActivityStopped(activity: Activity) {

    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

    }

    override fun onActivityDestroyed(activity: Activity) {
        currentActivity = null
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_START -> {
                Log.d(LOG_TAG, "onStart")
                if (!AdmobConstants.IS_SHOW_APP_OPEN_ADMOB) return
                onAppForegrounded()
                onMoveToForeground()
            }
            Lifecycle.Event.ON_STOP -> {
                //your code here
                onAppBackgrounded()
            }
            else -> {}
        }
    }

    /** Show the ad if one isn't already showing. */
    private fun showAdIfAvailable(activity: Activity) {
        if (isShowingAd || !isAdAvailable()) {
            Log.d(LOG_TAG, "Can not show ad.")
            fetchAd(activity)
            return
        }
        Log.d(LOG_TAG, "Will show ad.")
        appOpenAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                // Called when full screen content is dismissed.
                // Set the reference to null so isAdAvailable() returns false.
                Log.d(LOG_TAG, "Ad dismissed fullscreen content.")
                this@AppOpenAdManager.appOpenAd = null
                this@AppOpenAdManager.isShowingAd = false
                val unused: Boolean = this@AppOpenAdManager.isShowingAd
                this@AppOpenAdManager.fetchAd(activity)
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                // Called when fullscreen content failed to show.
                // Set the reference to null so isAdAvailable() returns false.
                Log.d(
                    LOG_TAG,
                    "onAdFailedToShowFullScreenContent: " + adError.message
                )
                this@AppOpenAdManager.appOpenAd = null
                this@AppOpenAdManager.isShowingAd = false
                val unused: Boolean = this@AppOpenAdManager.isShowingAd
                fetchAd(activity)
            }

            override fun onAdShowedFullScreenContent() {
                // Called when fullscreen content is shown.
                Log.d(LOG_TAG, "Ad showed fullscreen content.")
                this@AppOpenAdManager.isShowingAd = true
                val unused: Boolean = this@AppOpenAdManager.isShowingAd
            }
        }
        isShowingAd = true
        appOpenAd?.show(activity)
    }

    private fun fetchAd(context: Context) {
        // Do not load ad if there is an unused ad or one is already loading.
        if (isLoadingAd || isAdAvailable()) {
            return
        }
        isLoadingAd = true
        val request = AdRequest.Builder().build()
        AppOpenAd.load(
            context,
            AD_UNIT_ID,
            request,
            object : AppOpenAd.AppOpenAdLoadCallback() {
                override fun onAdLoaded(appOpenAd: AppOpenAd) {
                    this@AppOpenAdManager.appOpenAd = appOpenAd
                    isLoadingAd = false
                    this@AppOpenAdManager.loadTime = Date().time
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    Log.e(LOG_TAG, loadAdError.message)
                    isLoadingAd = false
                }
            }
        )
    }

    private fun wasLoadTimeLessThanNHoursAgo(numHours: Long): Boolean {
        val dateDifference: Long = Date().time - loadTime
        val numMilliSecondsPerHour: Long = 3600000
        return dateDifference < numMilliSecondsPerHour * numHours
    }

    /** Check if ad exists and can be shown. */
    private fun isAdAvailable(): Boolean {
        return appOpenAd != null && wasLoadTimeLessThanNHoursAgo(4)
    }

    private fun onMoveToForeground() {
        // Show the ad (if available) when the app moves to foreground.
        currentActivity?.let {
            showAdIfAvailable(it)
        }
    }

    private fun onAppBackgrounded() {
        Log.d("YourApplication", "YourApplication is in background")
    }

    private fun onAppForegrounded() {
        Log.d("YourApplication", "YourApplication is in foreground")
    }
}