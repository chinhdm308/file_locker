package com.base.presentation.ui.setting

import android.Manifest
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.os.bundleOf
import com.base.presentation.R
import com.base.presentation.base.BaseActivity
import com.base.presentation.databinding.ActivitySettingBinding
import com.base.presentation.ui.intruders.IntrudersPhotosActivity
import com.base.presentation.receivers.DeviceMyReceiver
import com.base.presentation.utils.collectLifecycleFlow
import com.base.presentation.utils.helper.CamouflageIconHelper
import com.base.presentation.widget.dialog.singlechoice.SingleChoiceItemDialog
import com.base.presentation.widget.dialog.singlechoice.SingleChoiceType
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SettingActivity : BaseActivity<ActivitySettingBinding>(ActivitySettingBinding::inflate) {

    private val viewModel by viewModels<SettingViewModel>()

    private val requestPermissions = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        when {
            permissions[Manifest.permission.CAMERA] == true -> {
                // Permission granted. Now resume your workflow.
                viewModel.setEnableIntrudersCatchers(true)
            }

            else -> {
                val permissionString = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    Manifest.permission.READ_MEDIA_IMAGES
                } else Manifest.permission.WRITE_EXTERNAL_STORAGE

                val showRationale =
                    shouldShowRequestPermissionRationale(permissionString)
                if (!showRationale) {
                    showPermissionSettingDialog()
                }
            }
        }
    }


    override fun initView(savedInstanceState: Bundle?) {
        window.statusBarColor = getColor(R.color.color_toolbar_hide_file)
        window.navigationBarColor = Color.parseColor("#F2F2F2")
        super.initView(savedInstanceState)

        binding.textTitle.setText(R.string.setting)

        binding.switchFingerPrint.isChecked = viewModel.isFingerPrintEnable()

        binding.switchEnableIntrudersCatcher.isChecked = viewModel.isIntrudersCatcherEnabled()

        binding.switchSoundAlarm.isChecked = viewModel.getPlayWarringSoundState()

        binding.textNumOfTimes.text = getString(R.string.num_of_times, viewModel.getNumOfTimesEnterIncorrectPwd())

        binding.switchPreventUninstall.isChecked = viewModel.getPreventUninstall()

        binding.imgCamouflageIcon.setImageResource(
            when (viewModel.getCamouflageIconName()) {
                CamouflageIconHelper.CamouflageIconType.CALCULATE.key -> R.drawable.ic_calculate_app_96
                CamouflageIconHelper.CamouflageIconType.MUSIC.key -> R.drawable.ic_music_app_65
                CamouflageIconHelper.CamouflageIconType.WEATHER.key -> R.drawable.ic_weather_app_65
                CamouflageIconHelper.CamouflageIconType.ALARM.key -> R.drawable.ic_alarm_clock_app_96
                else -> R.mipmap.ic_launcher_round
            }
        )
    }

    override fun viewListener() {
        super.viewListener()

        binding.buttonBack.setOnClickListener {
            finish()
        }

        binding.switchFingerPrint.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setEnableFingerPrint(isChecked)
        }

        binding.switchEnableIntrudersCatcher.setOnCheckedChangeListener { _, isChecked ->
            enableIntrudersCatcher(isChecked)
        }

        binding.switchSoundAlarm.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setPlayWarringSoundState(isChecked)
        }

        binding.layoutIntrudersFolder.setOnClickListener {
            if (viewModel.isIntrudersCatcherEnabled().not()) {
                enableIntrudersCatcher(true)
            } else {
                startActivity(Intent(this, IntrudersPhotosActivity::class.java))
            }
        }

        binding.llNumAllows.setOnClickListener {
            val singleItems = arrayOf("2 times", "3 times", "4 times", "5 times")
            val checkedItem = when (viewModel.getNumOfTimesEnterIncorrectPwd()) {
                2 -> 0
                3 -> 1
                4 -> 2
                5 -> 3
                else -> 0
            }

            val dialog = MaterialAlertDialogBuilder(this)
                .setTitle(getString(R.string.number_of_times_allowed))
                .setNeutralButton(getString(R.string.cancel)) { dialog, which ->
                    // Respond to neutral button press
                }
                .setPositiveButton("OK") { dialog, which ->
                    // Respond to positive button press

                }
                // Single-choice items (initialized with checked item)
                .setSingleChoiceItems(singleItems, checkedItem) { dialog, which ->
                    // Respond to item chosen
                    viewModel.setNumOfTimesEnterIncorrectPwd(which + 2)
                    binding.textNumOfTimes.text =
                        getString(R.string.num_of_times, which + 2)
                    dialog.dismiss()
                }

            dialog.show()

        }

        binding.llPreventUninstall.setOnClickListener {
            if (!binding.switchPreventUninstall.isChecked) {
                deviceMgr()
            } else {
                val componentName = ComponentName(this, DeviceMyReceiver::class.java)
                val dpm = getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
                dpm.removeActiveAdmin(componentName)
                binding.switchPreventUninstall.isChecked = false
            }
        }

        binding.llCamouflageIcon.setOnClickListener {
            changeAppIconDynamically()
        }

        binding.textChangePattern.setOnClickListener {

        }
    }

    override fun dataObservable() {
        super.dataObservable()

        collectLifecycleFlow(viewModel.fingerPrintStatusViewState) { state ->
            binding.textFingerprintRegisterTitle.text = state.getFingerPrintSettingTitle(this)
            binding.textFingerprintRegisterDescription.text =
                state.getFingerPrintSettingSubtitle(this)

            binding.switchFingerPrint.isEnabled = state.isFingerPrintCheckBoxEnabled()
        }
    }

    private fun enableIntrudersCatcher(isChecked: Boolean) {
        if (isChecked) {
            requestPermissions.launch(
                arrayOf(
                    Manifest.permission.CAMERA,
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) Manifest.permission.READ_MEDIA_IMAGES
                    else Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            )
        } else {
            viewModel.setEnableIntrudersCatchers(false)
        }
    }

    override fun onResume() {
        super.onResume()

        binding.switchPreventUninstall.isChecked = viewModel.getPreventUninstall()
    }

    private fun deviceMgr() {
        enableDeviceManager()
    }

    private fun enableDeviceManager() {
        val componentName = ComponentName(this, DeviceMyReceiver::class.java)
        val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN)
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName)
        intent.putExtra(
            DevicePolicyManager.EXTRA_ADD_EXPLANATION,
            getString(R.string.pwdsetting_advance_uninstallapp_detail, getString(R.string.app_name))
        )
        startActivity(intent)
    }

    private fun changeAppIconDynamically() {
        val dialog = SingleChoiceItemDialog.newInstance(
            bundle = bundleOf(
                SingleChoiceItemDialog.BUNDLE_TYPE to SingleChoiceType.CAMOUFLAGE_ICON.name
            )
        )
        dialog.show(supportFragmentManager, null)
    }
}