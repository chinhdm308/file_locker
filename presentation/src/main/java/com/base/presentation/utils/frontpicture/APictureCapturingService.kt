package com.base.presentation.utils.frontpicture

import android.app.Activity
import android.content.Context
import android.hardware.camera2.CameraManager
import android.hardware.display.DisplayManager
import android.util.SparseIntArray
import android.view.Display
import android.view.Surface
import androidx.core.content.getSystemService
import timber.log.Timber


/***
 * constructor.
 *
 * @param activity the activity used to get display manager and the application context
 */
abstract class APictureCapturingService(activity: Activity) {

    private val activity: Activity
    protected val context: Context
    val manager: CameraManager

    /**
     * Conversion from screen rotation to JPEG orientation.
     */
    private val orientations: SparseIntArray = SparseIntArray().apply {
        append(Surface.ROTATION_0, 90)
        append(Surface.ROTATION_90, 0)
        append(Surface.ROTATION_180, 270)
        append(Surface.ROTATION_270, 180)
    }

    init {
        this.activity = activity
        this.context = activity.applicationContext
        this.manager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    }

    /***
     * @return  orientation
     */
    open fun getOrientation(): Int {
        val defaultDisplay = context.getSystemService<DisplayManager>()
            ?.getDisplay(Display.DEFAULT_DISPLAY)?.rotation ?: 0
//        val rotation = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
//            activity.display?.rotation ?: 0
//        } else {
//            @Suppress("DEPRECATION")
//            activity.windowManager.defaultDisplay.rotation
//        }
        Timber.d("orientation: ${orientations[defaultDisplay]}")
        return orientations[defaultDisplay]
    }

    /**
     * Retrieves the JPEG orientation from the specified screen rotation.
     *
     * @param mSensorOrientation Orientation of the camera sensor.
     * @return The JPEG orientation (one of 0, 90, 270, and 360)
     */
    fun getOrientation(mSensorOrientation: Int): Int {
        // Sensor orientation is 90 for most devices, or 270 for some devices (eg. Nexus 5X)
        // We have to take that into account and rotate JPEG properly.
        // For devices with orientation of 90, we simply return our mapping from ORIENTATIONS.
        // For devices with orientation of 270, we need to rotate the JPEG 180 degrees.

        val defaultDisplay = context.getSystemService<DisplayManager>()
            ?.getDisplay(Display.DEFAULT_DISPLAY)

        // The screen rotation.
        val rotation = defaultDisplay?.rotation ?: 0


        return (orientations[rotation] + mSensorOrientation + 270) % 360
    }

    /**
     * starts pictures capturing process.
     *
     * @param listener picture capturing listener
     */
    abstract fun startCapturing(listener: PictureCapturingListener?)
}