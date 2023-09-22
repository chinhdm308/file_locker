package com.base.basemvvmcleanarchitecture.utils.frontpicture

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.SurfaceTexture
import android.hardware.Camera
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.os.Environment
import com.base.basemvvmcleanarchitecture.utils.AppConstants
import com.base.basemvvmcleanarchitecture.utils.helper.file.MediaScannerConnector
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton


class FrontPictureHelper @Inject constructor(
    @ApplicationContext val context: Context,
) {

    private enum class State {
        IDLE, TAKING, TAKEN, ERROR
    }

    private var state: State = State.IDLE

    private var camera: Camera? = null
    private var cameraId = NO_CAMERA_ID

    private val _frontPictureState = MutableSharedFlow<FrontPictureState>()
    val frontPictureState get() = _frontPictureState.asSharedFlow()

    fun takePicture() {
        if (state == State.TAKEN || state == State.TAKING) {
            return
        }

        state = State.TAKING
        startCamera()
        camera?.takePicture(null, null) { data, camera ->
            savePicture(data)
        }
    }

    private fun startCamera() {
        val dummy = SurfaceTexture(0)

        try {
            cameraId = getFrontCameraId()
            if (cameraId == NO_CAMERA_ID) {
                _frontPictureState.tryEmit(FrontPictureState.Error(IllegalStateException("No front camera found")))
                state = State.ERROR
                return
            }
            camera = Camera.open(cameraId).also {
                it.setPreviewTexture(dummy)
                it.startPreview()
            }
            _frontPictureState.tryEmit(FrontPictureState.Started)
        } catch (e: RuntimeException) {
            _frontPictureState.tryEmit(FrontPictureState.Error(e))
            state = State.ERROR
        }
    }

    fun stopCamera() {
        camera?.stopPreview()
        camera?.release()
        camera = null
        _frontPictureState.tryEmit(FrontPictureState.Destroyed)
    }

    private fun getFrontCameraId(): Int {
        var camId = NO_CAMERA_ID
        val numberOfCameras = Camera.getNumberOfCameras()
        val ci = Camera.CameraInfo()
        for (i in 0 until numberOfCameras) {
            Camera.getCameraInfo(i, ci)
            if (ci.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                camId = i
            }
        }
        return camId
    }

    fun getOrientation(cameraId: Int): Int {
        return try {
            val manager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
            val cameraIds = manager.cameraIdList
            val characteristics = manager.getCameraCharacteristics(cameraIds[cameraId])
            characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION)!!
        } catch (e: CameraAccessException) {
            // TODO handle error properly or pass it on
            0
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun savePicture(data: ByteArray) {
        try {
            val mainFolder = File(AppConstants.SD_PATH, AppConstants.MAIN_FOLDER)
            if (!mainFolder.exists() && !mainFolder.mkdir()) {
                return
            }
            val pictureFileDir = File(mainFolder, AppConstants.INTRUDER_FOLDER)
            if (!pictureFileDir.exists() && !pictureFileDir.mkdir()) {
                return
            }
            val dateFormat = SimpleDateFormat("yyyymmddhhmmss")
            val date: String = dateFormat.format(Date())
            val photoFile = "Picture_$date.jpg"
            val fileName = pictureFileDir.path + File.separator + photoFile
            val pictureFile = File(fileName)
            Timber.e("Filename is $fileName")
            val fos = FileOutputStream(pictureFile)
            fos.write(data)
            fos.close()
            refreshFileSystem(pictureFile.absolutePath)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun refreshFileSystem(originalPath: String) {
        MediaScannerConnector.scan(context, originalPath)
    }

    companion object {
        private const val NO_CAMERA_ID = -1
    }
}

sealed class FrontPictureState {
    class Taken(val filePath: String) : FrontPictureState()
    class Error(val error: Throwable) : FrontPictureState()
    object Started : FrontPictureState()
    object Destroyed : FrontPictureState()
    object Failed : FrontPictureState()
}