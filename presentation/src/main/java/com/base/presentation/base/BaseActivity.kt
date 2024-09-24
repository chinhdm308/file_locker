package com.base.presentation.base

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.SystemClock
import android.provider.Settings
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.base.presentation.R
import com.base.presentation.widget.dialog.LoadingDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import timber.log.Timber


abstract class BaseActivity<VB : ViewBinding>(
    private val inflater: ActivityBindingInflater<VB>,
) : AppCompatActivity(), ViewInterface {

    private var _binding: VB? = null
    protected val binding: VB
        get() = _binding
            ?: throw IllegalStateException("Cannot access view after view destroyed or before view creation")

    private var lastTimeClick: Long = 0

    private val loadingDialog: LoadingDialog by lazy {
        LoadingDialog()
    }

    open var activeFlagSecured: Boolean = true

    // Initialize the variable in before activity creation is complete.
    private val storagePermissionResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                // Permission granted. Now resume your workflow.
                onAccessStoragePermission()
            }
        }
    }

    private val storageRequestPermission = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions(),
    ) { permissions ->
        when {
            permissions[Manifest.permission.READ_MEDIA_IMAGES] == true
                    || permissions[Manifest.permission.READ_MEDIA_IMAGES] == true
                    || permissions[Manifest.permission.WRITE_EXTERNAL_STORAGE] == true -> {
                // Permission granted. Now resume your workflow.
                onAccessStoragePermission()
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


    override fun applyOverrideConfiguration(overrideConfiguration: Configuration) {
        super.applyOverrideConfiguration(baseContext.resources.configuration)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (activeFlagSecured) setFlagSecure()
        transparentStatusAndNavigation()
//        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        _binding = inflater.invoke(layoutInflater)
        setContentView(requireNotNull(_binding).root)

        initView(savedInstanceState)
        viewListener()
        dataObservable()
    }

    /**
     * Close SoftKeyboard when user click out of EditText
     */
    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    v.clearFocus()
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.windowToken, 0)
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }

    override fun initView(savedInstanceState: Bundle?) {}

    override fun viewListener() {}

    override fun dataObservable() {}

    protected fun isDoubleClick(): Boolean {
        if (SystemClock.elapsedRealtime() - lastTimeClick > 500) {
            lastTimeClick = SystemClock.elapsedRealtime()
            return false
        }
        return true
    }

    fun isLoading(isShow: Boolean) {
        if (isShow) {
            loadingDialog.showDialog(supportFragmentManager)
        } else {
            loadingDialog.hiddenDialog()
        }
    }

    /**
     * It will automatically display a grey background when Recent button is pressed.
     * A possible side-effect would be that you cant take the screenshot of the activity you are defining this way.
     */
    private fun setFlagSecure() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
    }

    fun showPermissionSettingDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle(getString(R.string.permissions_setting))
            .setMessage(getString(R.string.sub_title_permission_step, "storage"))
            .setPositiveButton(getString(R.string.setting)) { dialog, _ ->
                startActivity(Intent().apply {
                    action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    data = Uri.fromParts("package", packageName, null)
                })
                dialog.dismiss()
            }
            .setNegativeButton(getString(R.string.cancel)) { dialog, _ -> dialog.dismiss() }
            .show()
    }

    @RequiresApi(Build.VERSION_CODES.R)
    fun showManageAppAllFilesAccessPermissionDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle(getString(R.string.storage_access))
            .setMessage(getString(R.string.storage_access_message))
            .setPositiveButton(getString(R.string.allow)) { dialog, which ->
                try {
                    val intent = Intent(
                        Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION,
                        Uri.parse("package:$packageName")
                    )
                    storagePermissionResultLauncher.launch(intent)
                } catch (e: Exception) {
                    Timber.e(e.message)
//                    val intent =
//                        Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
//                    storagePermissionResultLauncher.launch(intent)
                }
            }
            .setNegativeButton(getString(R.string.cancel)) { dialog, _ -> dialog.dismiss() }
            .show()
    }

    fun requestStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                showManageAppAllFilesAccessPermissionDialog()
            } else {
                onAccessStoragePermission()
            }
        } else {
            storageRequestPermission.launch(
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            )
        }
    }

    open fun onAccessStoragePermission() {}

    fun transparentStatusAndNavigation(
        systemUiScrim: Int = Color.parseColor("#40000000") // 25% black
    ) {
        var systemUiVisibility = 0
        // Use a dark scrim by default since light status is API 23+
        var statusBarColor = systemUiScrim
        //  Use a dark scrim by default since light nav bar is API 27+
        var navigationBarColor = systemUiScrim
        val winParams = window.attributes


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            systemUiVisibility = systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            statusBarColor = Color.TRANSPARENT
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            systemUiVisibility = systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
            navigationBarColor = Color.TRANSPARENT
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            systemUiVisibility = systemUiVisibility or
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            window.decorView.systemUiVisibility = systemUiVisibility
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            winParams.flags = winParams.flags or
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS or
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            winParams.flags = winParams.flags and
                    (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS or
                            WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION).inv()
            window.statusBarColor = statusBarColor
            window.navigationBarColor = navigationBarColor
        }

        window.attributes = winParams
    }
}
