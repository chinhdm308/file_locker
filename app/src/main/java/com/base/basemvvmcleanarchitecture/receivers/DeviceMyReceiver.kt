package com.base.basemvvmcleanarchitecture.receivers

import android.app.admin.DeviceAdminReceiver
import android.content.Context
import android.content.Intent
import android.os.UserHandle
import com.base.basemvvmcleanarchitecture.presentation.ui.setting.SettingActivity
import com.base.data.local.datastore.DataStoreRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class DeviceMyReceiver : DeviceAdminReceiver(), CoroutineScope by CoroutineScope(Dispatchers.IO) {

    @Inject
    lateinit var dataStoreRepository: DataStoreRepository

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        Timber.e("receiver onReceive")
    }

    override fun onEnabled(context: Context, intent: Intent) {
        Timber.e("receiver onEnabled")
        launch {
            dataStoreRepository.setPreventUninstall(true)
        }
    }

    override fun onDisabled(context: Context, intent: Intent) {
        Timber.e("receiver onDisabled")
        launch {
            dataStoreRepository.setPreventUninstall(false)
        }
    }

    override fun onDisableRequested(context: Context, intent: Intent): CharSequence {
        Timber.e("receiver onDisableRequested")
        val intent2 = Intent(context, SettingActivity::class.java)
        intent2.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent2)
        context.stopService(intent)
        return ""
    }

    override fun onPasswordChanged(context: Context, intent: Intent, user: UserHandle) {

    }

    override fun onPasswordFailed(context: Context, intent: Intent, user: UserHandle) {

    }

    override fun onPasswordSucceeded(context: Context, intent: Intent, user: UserHandle) {

    }
}