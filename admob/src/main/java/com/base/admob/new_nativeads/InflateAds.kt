package com.base.admob.new_nativeads

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import com.base.admob.databinding.GoogleNative1BigNotFixBinding
import com.base.admob.databinding.NativeSmallNewBinding
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView

class InflateAds(private val context: Context) {
    fun inflateAdmobNative(nativeAd: NativeAd, viewGroup: ViewGroup) {
        viewGroup.visibility = View.VISIBLE
//        val nativeAdView = (context as Activity).layoutInflater.inflate(
//            R.layout.google_native1_big_not_fix,
//            null as ViewGroup?
//        ) as NativeAdView

        val binding = GoogleNative1BigNotFixBinding.inflate(LayoutInflater.from(context), null, false)
        val nativeAdView: NativeAdView = binding.root


        nativeAdView.mediaView = binding.adMedia
        nativeAdView.headlineView = binding.adHeadline
        nativeAdView.bodyView = binding.adBody
        val textView = binding.adCallToAction as TextView
        nativeAdView.callToActionView = textView
        nativeAdView.iconView = binding.adAppIcon
        nativeAdView.priceView = binding.adPrice
        nativeAdView.starRatingView = binding.adStars
        nativeAdView.storeView = binding.adStore
        nativeAdView.advertiserView = binding.adAdvertiser
        (nativeAdView.headlineView as TextView?)?.text = nativeAd.headline
        if (nativeAd.body == null) {
            nativeAdView.bodyView?.visibility = View.INVISIBLE
        } else {
            nativeAdView.bodyView?.visibility = View.VISIBLE
            (nativeAdView.bodyView as TextView?)?.text = nativeAd.body
        }
        if (nativeAd.callToAction == null) {
            nativeAdView.callToActionView?.visibility = View.INVISIBLE
        } else {
            nativeAdView.callToActionView?.visibility = View.VISIBLE
            (nativeAdView.callToActionView as TextView?)?.text = nativeAd.callToAction
            textView.visibility = View.VISIBLE
        }
        if (nativeAd.icon == null) {
            nativeAdView.iconView?.visibility = View.GONE
        } else {
            (nativeAdView.iconView as ImageView?)?.setImageDrawable(nativeAd.icon?.drawable)
            nativeAdView.iconView?.visibility = View.VISIBLE
        }
        if (nativeAd.price == null) {
            nativeAdView.priceView?.visibility = View.GONE
        } else {
            nativeAdView.priceView?.visibility = View.VISIBLE
            (nativeAdView.priceView as TextView?)?.text = nativeAd.price
        }
        if (nativeAd.store == null) {
            nativeAdView.storeView?.visibility = View.GONE
        } else {
            nativeAdView.storeView?.visibility = View.VISIBLE
            (nativeAdView.storeView as TextView?)?.text = nativeAd.store
        }
        if (nativeAd.advertiser == null) {
            nativeAdView.advertiserView?.visibility = View.GONE
        } else {
            (nativeAdView.advertiserView as TextView?)?.text = nativeAd.advertiser
            nativeAdView.advertiserView?.visibility = View.VISIBLE
        }
        nativeAdView.setNativeAd(nativeAd)
        viewGroup.removeAllViews()
        viewGroup.addView(nativeAdView)
    }

    fun inflateAdmobNativeMedium(nativeAd: NativeAd, viewGroup: ViewGroup) {
        viewGroup.visibility = View.VISIBLE

        val binding = NativeSmallNewBinding.inflate(LayoutInflater.from(context), null, false)
        val nativeAdView: NativeAdView = binding.root
//        val nativeAdView =
//            (this.context as Activity).layoutInflater.inflate(R.layout.native_small_new, null as ViewGroup?) as NativeAdView
        nativeAdView.mediaView = binding.adMedia
        nativeAdView.headlineView = binding.adHeadline
        nativeAdView.bodyView = binding.adBody
        val textView = binding.adCallToAction as TextView
        nativeAdView.callToActionView = textView
        nativeAdView.iconView = binding.adAppIcon
        nativeAdView.priceView = binding.adPrice
        nativeAdView.starRatingView = binding.adStars
        nativeAdView.storeView = binding.adStore
        nativeAdView.advertiserView = binding.adAdvertiser
        (nativeAdView.headlineView as TextView?)?.text = nativeAd.headline
        if (nativeAd.body == null) {
            nativeAdView.bodyView?.visibility = View.INVISIBLE
        } else {
            nativeAdView.bodyView?.visibility = View.VISIBLE
            (nativeAdView.bodyView as TextView?)?.text = nativeAd.body
        }
        if (nativeAd.callToAction == null) {
            nativeAdView.callToActionView?.visibility = View.INVISIBLE
        } else {
            nativeAdView.callToActionView?.visibility = View.VISIBLE
            (nativeAdView.callToActionView as TextView?)?.text = nativeAd.callToAction
            textView.visibility = View.VISIBLE
        }
        if (nativeAd.icon == null) {
            nativeAdView.iconView?.visibility = View.GONE
        } else {
            (nativeAdView.iconView as ImageView?)?.setImageDrawable(nativeAd.icon?.drawable)
            nativeAdView.iconView?.visibility = View.VISIBLE
        }
        if (nativeAd.price == null) {
            nativeAdView.priceView?.visibility = View.INVISIBLE
        } else {
            nativeAdView.priceView?.visibility = View.VISIBLE
            (nativeAdView.priceView as TextView?)?.text = nativeAd.price
        }
        if (nativeAd.store == null) {
            nativeAdView.storeView?.visibility = View.INVISIBLE
        } else {
            nativeAdView.storeView?.visibility = View.VISIBLE
            (nativeAdView.storeView as TextView?)?.text = nativeAd.store
        }
        if (nativeAd.starRating == null) {
            nativeAdView.starRatingView?.visibility = View.INVISIBLE
        } else {
            (nativeAdView.starRatingView as RatingBar?)?.rating = nativeAd.starRating?.toFloat() ?: 0F
            nativeAdView.starRatingView?.visibility = View.VISIBLE
        }
        if (nativeAd.advertiser == null) {
            nativeAdView.advertiserView?.visibility = View.INVISIBLE
        } else {
            (nativeAdView.advertiserView as TextView?)?.text = nativeAd.advertiser
            nativeAdView.advertiserView?.visibility = View.VISIBLE
        }
        nativeAdView.setNativeAd(nativeAd)
        viewGroup.removeAllViews()
        viewGroup.addView(nativeAdView)
    }
}