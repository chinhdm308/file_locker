package com.base.basemvvmcleanarchitecture.presentation.ui.permission

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Environment
import com.base.basemvvmcleanarchitecture.R
import com.base.basemvvmcleanarchitecture.base.BaseActivity
import com.base.basemvvmcleanarchitecture.databinding.ActivityPermissionBinding
import com.base.basemvvmcleanarchitecture.presentation.ui.patterncheck.PatternCheckActivity
import com.base.basemvvmcleanarchitecture.presentation.ui.patterncreate.PatternCreateActivity
import com.base.data.local.datastore.DataStoreRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class PermissionActivity :
    BaseActivity<ActivityPermissionBinding>(ActivityPermissionBinding::inflate) {

    @Inject
    lateinit var dataStoreRepository: DataStoreRepository

    override var activeFlagSecured: Boolean = false

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)

        binding.textSub.text = getString(R.string.sub_title_permission_step, "storage")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                binding.buttonAllow.backgroundTintList = ColorStateList.valueOf(Color.GREEN)
            }
        } else if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
            || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        ) {
            binding.buttonAllow.backgroundTintList = ColorStateList.valueOf(Color.GREEN)
        }
    }

    override fun setOnClick() {
        super.setOnClick()

        binding.buttonDonAccess.setOnClickListener {
            finish()
        }

        binding.buttonAllow.setOnClickListener {
            requestStoragePermission()
        }
    }

    override fun onAccessStoragePermission() = runBlocking {
        val intent = if (dataStoreRepository.getAppFirstSettingInstanceDone()) {
            Intent(this@PermissionActivity, PatternCheckActivity::class.java)
        } else {
            Intent(this@PermissionActivity, PatternCreateActivity::class.java)
        }
        startActivity(intent)
        finish()
    }
}