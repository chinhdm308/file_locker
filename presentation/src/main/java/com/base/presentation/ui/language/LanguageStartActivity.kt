package com.base.presentation.ui.language

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.base.data.local.datastore.DataStoreRepository
import com.base.presentation.base.BaseActivity
import com.base.presentation.databinding.ActivityLanguageStartBinding
import com.base.presentation.ui.language.adapter.LanguageAdapter
import com.base.presentation.ui.language.model.LanguageModel
import com.base.presentation.ui.patterncreate.PatternCreateActivity
import com.base.presentation.ui.patternunlock.PatternUnlockActivity
import com.base.presentation.ui.permission.PermissionActivity
import com.base.presentation.utils.AppConstants
import com.base.presentation.utils.SystemUtil
import com.base.presentation.utils.isNetworkConnected
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class LanguageStartActivity : BaseActivity<ActivityLanguageStartBinding>(ActivityLanguageStartBinding::inflate) {

    @Inject
    lateinit var dataStoreRepository: DataStoreRepository

    private var listLanguage: MutableList<LanguageModel> = AppConstants.listLanguages.toMutableList()
    private var codeLang: String = Locale.getDefault().language

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)

        initData()

        val linearLayoutManager = LinearLayoutManager(this)
        val languageAdapter = LanguageAdapter(this, listLanguage) {
            codeLang = it
        }

        languageAdapter.setCheck(codeLang)
        binding.recyclerView.layoutManager = linearLayoutManager
        binding.recyclerView.adapter = languageAdapter

    }

    override fun viewListener() {
        super.viewListener()

        binding.ivDone.setOnClickListener {
            runBlocking {
                SystemUtil.saveLocale(baseContext, codeLang)
                dataStoreRepository.setFirstLanguage()
                startActivity()
            }
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
//                val adView = View.inflate(this@LanguageStartActivity, R.layout.ads_native_small, null) as NativeAdView
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

    private fun initData() {
        val lang = Locale.getDefault().language
        for (i in listLanguage.indices) {
            if (listLanguage[i].code == lang) {
                listLanguage.add(0, listLanguage[i])
                listLanguage.removeAt(i + 1)
            }
        }
    }

    private suspend fun startActivity() {
        val isPermissionGranted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Environment.isExternalStorageManager()
        } else {
            checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        }

        val intent = if (!isPermissionGranted) {
            Intent(this, PermissionActivity::class.java)
        } else {
            if (dataStoreRepository.getPattern().pattern.isEmpty()) {
                Intent(this, PatternUnlockActivity::class.java)
            } else {
                Intent(this, PatternCreateActivity::class.java)
            }
        }
        startActivity(intent)
        finish()
    }
}
