package com.base.admob

import android.app.Activity
import android.widget.FrameLayout
import com.base.admob.utils.currentWindowMetricsPointCompat
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

class AdBanner {
    companion object {
        private var mInstance: AdBanner? = null

        @JvmStatic
        fun getInstance(): AdBanner {
            if (mInstance == null) {
                mInstance = AdBanner()
            }
            return mInstance!!
        }
    }

    fun showBanner(activity: Activity, adContainerView: FrameLayout) {
        val adUnitId = "ca-app-pub-3940256099942544/6300978111"
        val adView = AdView(activity).apply {
            setAdSize(getAdSize(activity, adContainerView))
            setAdUnitId(adUnitId)
            loadAd(AdRequest.Builder().build())
            adListener = object : AdListener() {}
        }

        adContainerView.addView(adView)
    }

    // Determine the screen width (less decorations) to use for the ad width.
    private fun getAdSize(activity: Activity, adContainerView: FrameLayout): AdSize {
        var adWidthPixels = adContainerView.width.toFloat()

        // If the ad hasn't been laid out, default to the full screen width.
        if (adWidthPixels == 0f) {
            adWidthPixels = activity.windowManager.currentWindowMetricsPointCompat().x.toFloat()
        }

        val density = activity.resources.displayMetrics.density
        val adWidth = (adWidthPixels / density).toInt()

        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, adWidth)
    }
}