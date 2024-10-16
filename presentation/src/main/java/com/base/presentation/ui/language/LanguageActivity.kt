package com.base.presentation.ui.language

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.base.presentation.base.BaseActivity
import com.base.presentation.databinding.ActivityLanguageBinding
import com.base.presentation.ui.language.adapter.LanguageAdapter
import com.base.presentation.ui.language.model.LanguageModel
import com.base.presentation.ui.main.MainActivity
import com.base.presentation.utils.AppConstants
import com.base.presentation.utils.SystemUtil
import com.base.presentation.utils.isNetworkConnected


class LanguageActivity : BaseActivity<ActivityLanguageBinding>(ActivityLanguageBinding::inflate) {

    private var listLanguage: MutableList<LanguageModel> = AppConstants.listLanguages.toMutableList()

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        SystemUtil.setLocale(this)

        val lang = SystemUtil.getPreLanguage(baseContext)
        for (i in listLanguage.indices) {
            if (listLanguage[i].code == lang) {
                listLanguage.add(0, listLanguage[i])
                listLanguage.removeAt(i + 1)
            }
        }

        val linearLayoutManager = LinearLayoutManager(this)
        val languageAdapter = LanguageAdapter(this, listLanguage) { codeLang ->
            SystemUtil.saveLocale(baseContext, codeLang)
            back()
        }
        languageAdapter.setCheck(SystemUtil.getPreLanguage(baseContext))
        binding.recyclerView.layoutManager = linearLayoutManager
        binding.recyclerView.adapter = languageAdapter
    }

    override fun viewListener() {
        super.viewListener()

        binding.layoutAppHeader.setOnBackPress {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        if (!isNetworkConnected()) {
            binding.nativeAds.visibility = View.GONE
        } else {
            binding.nativeAds.visibility = View.VISIBLE
            loadNativeAd()
        }
    }

    private fun loadNativeAd() {
//        Admob.getInstance().loadNativeAd(this, BuildConfig.native_language, object : NativeCallback() {
//            override fun onNativeAdLoaded(nativeAd: NativeAd) {
//                super.onNativeAdLoaded(nativeAd)
//                val adView = View.inflate(this@LanguageActivity, R.layout.ads_native_small, null) as NativeAdView
//                binding.nativeAds.removeAllViews()
//                binding.nativeAds.addView(adView)
//                Admob.getInstance().pushAdsToViewCustom(nativeAd, adView)
//            }
//
//            override fun onAdFailedToLoad() {
//                super.onAdFailedToLoad()
//                binding.nativeAds.visibility = View.GONE
//                binding.nativeAds.removeAllViews()
//            }
//        })
    }

    private fun back() {
        finishAffinity()
        startActivity(Intent(this@LanguageActivity, MainActivity::class.java))
    }
}
