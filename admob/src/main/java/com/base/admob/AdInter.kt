package com.base.admob

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.base.admob.utils.AdmobConstants
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class AdInter {
    companion object {
        private const val LOG_TAG = "AdInter"
        private var gclick: Int = 1
        private var mInstance: AdInter? = null

        @JvmStatic
        fun getInstance(): AdInter {
            if (mInstance == null) {
                mInstance = AdInter()
            }
            return mInstance!!
        }
    }

    interface MyCallbackAdInter {
        fun invoke()
    }

    private var interstitialOne: InterstitialAd? = null
    private var interstitialTwo: InterstitialAd? = null
    private var myCallback: MyCallbackAdInter? = null

    fun loadInterOne(context: Context) {
        MobileAds.initialize(context) {}
        InterstitialAd.load(
            context,
            "ca-app-pub-3940256099942544/1033173712",
            AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    this@AdInter.interstitialOne = interstitialAd
                    Log.i(LOG_TAG, "onAdLoaded 1")
                    this@AdInter.interstitialOne?.fullScreenContentCallback = object : FullScreenContentCallback() {
                        override fun onAdDismissedFullScreenContent() {
                            this@AdInter.interstitialOne = null
                            Log.d(LOG_TAG, "The ad 1 was dismissed.")
                            this@AdInter.loadInterOne(context)
                            if (this@AdInter.myCallback != null) {
                                this@AdInter.myCallback?.invoke()
                                this@AdInter.myCallback = null
                            }
                        }

                        override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                            this@AdInter.interstitialOne = null
                            Log.d(LOG_TAG, "The ad 1 failed to show.")
                            this@AdInter.loadInterOne(context)
                        }
                    }
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    Log.d(LOG_TAG, "The ad 1 Load Error.")
                    this@AdInter.interstitialOne = null
                }
            }
        )
    }

    fun loadInterTwo(context: Context) {
        MobileAds.initialize(context) {}
        InterstitialAd.load(
            context,
            "ca-app-pub-3940256099942544/1033173712",
            AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    this@AdInter.interstitialTwo = interstitialAd
                    Log.i(LOG_TAG, "onAdLoaded 2")
                    this@AdInter.interstitialTwo?.fullScreenContentCallback = object : FullScreenContentCallback() {
                        override fun onAdDismissedFullScreenContent() {
                            this@AdInter.interstitialTwo = null
                            Log.d(LOG_TAG, "The ad 2 was dismissed.")
                            this@AdInter.loadInterTwo(context)
                            if (this@AdInter.myCallback != null) {
                                this@AdInter.myCallback?.invoke()
                                this@AdInter.myCallback = null
                            }
                        }

                        override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                            this@AdInter.interstitialTwo = null
                            Log.d(LOG_TAG, "The ad 2 failed to show.")
                            this@AdInter.loadInterTwo(context)
                        }
                    }
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    Log.d(LOG_TAG, "The ad 1 Load Error.")
                    this@AdInter.interstitialOne = null
                }
            }
        )
    }

    fun showInter(activity: Activity, myCallback: MyCallbackAdInter?) {
        if (!AdmobConstants.IS_SHOW_INTER_ADMOB) {
            myCallback?.invoke()
            return
        }
        this.myCallback = myCallback
        val i = gclick
        if (i == 1) {
            gclick = 1
            val interstitialAd: InterstitialAd? = this.interstitialOne
            if (interstitialAd != null) {
                interstitialAd.show(activity)
                return
            }
            val interstitialAd2: InterstitialAd? = this.interstitialTwo
            if (interstitialAd2 != null) {
                interstitialAd2.show(activity)
            } else if (myCallback != null) {
                myCallback.invoke()
                this.myCallback = null
            }

        } else {
            gclick = i + 1
            if (myCallback != null) {
                myCallback.invoke()
                this.myCallback = null
            }
        }
    }

    fun showInterIntent(activity: Activity, intent: Intent, myCallback: MyCallbackAdInter?) {
        this.myCallback = myCallback
        activity.startActivity(intent)
        val i = gclick
        if (i == 1) {
            gclick = 1
            val interstitialAd: InterstitialAd? = interstitialOne
            if (interstitialAd != null) {
                interstitialAd.show(activity)
                return
            }
            val interstitialAd2: InterstitialAd? = this.interstitialTwo
            if (interstitialAd2 != null) {
                interstitialAd2.show(activity)
                return
            }
            val myCallback2 = this.myCallback
            if (myCallback2 != null) {
                myCallback2.invoke()
                this.myCallback = null
                return
            }
            return
        }
        gclick = i + 1
        val myCallback3 = this.myCallback
        if (myCallback3 != null) {
            myCallback3.invoke()
            this.myCallback = null
        }
    }

    fun showInterBack(activity: Activity, myCallback: MyCallbackAdInter?) {
        this.myCallback = myCallback
        val i = gclick
        if (i == 1) {
            gclick = 1
            val interstitialAd: InterstitialAd? = interstitialOne
            if (interstitialAd != null) {
                interstitialAd.show(activity)
                return
            }
            val interstitialAd2: InterstitialAd? = this.interstitialTwo
            if (interstitialAd2 != null) {
                interstitialAd2.show(activity)
            } else if (myCallback != null) {
                myCallback.invoke();
                this.myCallback = null;
            }
        } else {
            gclick = i + 1
            if (myCallback != null) {
                myCallback.invoke();
                this.myCallback = null;
            }
        }
    }

    fun alert(context: Context) {
        val builder = MaterialAlertDialogBuilder(context)
        builder.setTitle("Internet Alert")
        builder.setMessage("You need to internet connection")
        builder.setPositiveButton("OK") { dialog: DialogInterface?, which: Int ->
            dialog?.cancel()
        }
        builder.show()
    }
}