package com.base.admob.new_nativeads

import android.app.Activity
import android.view.ViewGroup
import com.base.admob.new_nativeads.InflateAds
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError

class NativeHelp {
    fun showNativeAds(activity: Activity, viewGroup: ViewGroup, myCallback: () -> Unit) {
        val builder = AdLoader.Builder(activity, "ca-app-pub-3940256099942544/2247696110")
        builder.forNativeAd { nativeAd -> InflateAds(activity).inflateAdmobNative(nativeAd, viewGroup) }

        builder.withAdListener(object : AdListener() {
            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                val builderFetch = AdLoader.Builder(activity, "ca-app-pub-3940256099942544/2247696110")
                builderFetch.forNativeAd { nativeAd -> InflateAds(activity).inflateAdmobNative(nativeAd, viewGroup) }
                builderFetch.withAdListener(object : AdListener() {
                    override fun onAdFailedToLoad(loadAdError: LoadAdError) {}
                }).build().loadAd(AdRequest.Builder().build())
            }

            override fun onAdLoaded() {
                super.onAdLoaded()
                myCallback.invoke()
            }
        }).build().loadAd(AdRequest.Builder().build())
    }

    fun showNativeMedium(activity: Activity, viewGroup: ViewGroup) {
        val builder = AdLoader.Builder(activity, "ca-app-pub-3940256099942544/2247696110")
        builder.forNativeAd { nativeAd -> InflateAds(activity).inflateAdmobNativeMedium(nativeAd, viewGroup) }
        builder.withAdListener(object : AdListener() {
            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                val builderFetch = AdLoader.Builder(activity, "ca-app-pub-3940256099942544/2247696110")
                builderFetch.forNativeAd { nativeAd -> InflateAds(activity).inflateAdmobNativeMedium(nativeAd, viewGroup) }
                builderFetch.withAdListener(object : AdListener() {
                    override fun onAdFailedToLoad(loadAdError: LoadAdError) {}
                }).build().loadAd(AdRequest.Builder().build())
            }
        }).build().loadAd(AdRequest.Builder().build())
    }
}