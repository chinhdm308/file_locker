package com.base.presentation.utils.frontpicture

import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.ImageFormat
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CameraMetadata
import android.hardware.camera2.CaptureRequest
import android.hardware.camera2.TotalCaptureResult
import android.hardware.camera2.params.OutputConfiguration
import android.hardware.camera2.params.SessionConfiguration
import android.media.ImageReader
import android.media.ImageReader.OnImageAvailableListener
import android.os.Build
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.util.Size
import android.view.Surface
import androidx.core.app.ActivityCompat
import com.base.presentation.utils.helper.file.DirectoryType
import com.base.presentation.utils.helper.file.FileExtension
import com.base.presentation.utils.helper.file.FileManager
import com.base.presentation.utils.helper.file.FileOperationRequest
import timber.log.Timber
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.LinkedList
import java.util.Queue
import java.util.TreeMap
import java.util.concurrent.Executors


/**
 * The aim of this service is to secretly take pictures (without preview or opening device's camera app)
 * from all available cameras using Android Camera 2 API
 */

@TargetApi(Build.VERSION_CODES.LOLLIPOP) //NOTE: camera 2 api was added in API level 21
class PictureCapturingServiceImpl(activity: Activity) : APictureCapturingService(activity) {

    companion object {
        /**
         * @param activity the activity used to get the app's context and the display manager
         * @return a new instance
         */
        fun getInstance(activity: Activity?): APictureCapturingService {
            return PictureCapturingServiceImpl(activity!!)
        }
    }

    private val fileManager by lazy { FileManager.getInstance(context) }

    /** cameraExecutor */
    private val cameraExecutor = Executors.newSingleThreadExecutor()

    /**
     * [HandlerThread] where all camera operations run
     * An additional thread for running tasks that shouldn't block the UI.
     */
    private val cameraThread = HandlerThread("CameraThread").apply { start() }

    /**
     * [Handler] corresponding to [cameraThread]
     * A {@link Handler} for running tasks in the background.
     */
    private val cameraHandler = Handler(cameraThread.looper)

    /**
     * A reference to the opened {@link CameraDevice}.
     */
    private var cameraDevice: CameraDevice? = null

    /**
     * An {@link ImageReader} that handles still image capture.
     */
    private var imageReader: ImageReader? = null

    /***
     * camera ids queue.
     */
    private var cameraIds: Queue<String>? = null

    /**
     * ID of the current {@link CameraDevice}.
     */
    private var currentCameraId: String? = null

    private var cameraClosed = false

    /**
     * stores a sorted map of (pictureUrlOnDisk, PictureData).
     */
    private var picturesTaken: TreeMap<String, ByteArray>? = null
    private var capturingListener: PictureCapturingListener? = null

    /**
     * Starts pictures capturing treatment.
     *
     * @param listener picture capturing listener
     */
    override fun startCapturing(listener: PictureCapturingListener?) {
        this.picturesTaken = TreeMap()
        this.capturingListener = listener
        this.cameraIds = LinkedList()
        try {
            val cameraIds = manager.cameraIdList
            if (cameraIds.isNotEmpty()) {
                (this.cameraIds as LinkedList<String>).addAll(cameraIds)
                currentCameraId = (this.cameraIds as LinkedList<String>).poll()
                openCamera()
            } else {
                //No camera detected!
                capturingListener?.onDoneCapturingAllPhotos(picturesTaken)
            }
        } catch (e: CameraAccessException) {
            e.printStackTrace()
            Timber.e("Exception occurred while accessing the list of cameras\n$e")
        }
    }

    private fun openCamera() {
        Timber.d("opening camera $currentCameraId")
        try {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                manager.openCamera(currentCameraId!!, stateCallback, null)
            }
        } catch (e: CameraAccessException) {
            Timber.e(" exception occurred while opening camera $currentCameraId\n$e")
        }
    }

    private val captureListener = object : CameraCaptureSession.CaptureCallback() {
        override fun onCaptureCompleted(
            session: CameraCaptureSession,
            request: CaptureRequest,
            result: TotalCaptureResult,
        ) {
            super.onCaptureCompleted(session, request, result)
            if (picturesTaken?.lastEntry() != null) {
                capturingListener!!.onCaptureDone(
                    picturesTaken?.lastEntry()?.key,
                    picturesTaken?.lastEntry()?.value
                )
                Timber.i("done taking picture from camera ${cameraDevice?.id}")
            }
            closeCamera()
        }
    }

    private val onImageAvailableListener = OnImageAvailableListener { imReader: ImageReader ->
        val image = imReader.acquireLatestImage()
        val buffer = image.planes[0].buffer
        val bytes = ByteArray(buffer.capacity())
        buffer[bytes]
        saveImageToDisk(bytes)
        image.close()
    }

    private val stateCallback: CameraDevice.StateCallback = object : CameraDevice.StateCallback() {
        override fun onOpened(camera: CameraDevice) {
            cameraClosed = false
            Timber.d("camera " + camera.id + " opened")
            cameraDevice = camera
            Timber.i("Taking picture from camera " + camera.id)
            //Take the picture after some delay. It may resolve getting a black dark photos.
            Handler(Looper.getMainLooper()).postDelayed({
                try {
                    takePicture()
                } catch (e: CameraAccessException) {
                    Timber.e(" exception occurred while taking picture from $currentCameraId\n$e")
                }
            }, 500)
        }

        override fun onDisconnected(camera: CameraDevice) {
            Timber.d(" camera " + camera.id + " disconnected")
            if (cameraDevice != null && !cameraClosed) {
                cameraClosed = true
                cameraDevice!!.close()
            }
        }

        override fun onClosed(camera: CameraDevice) {
            cameraClosed = true
            Timber.d("camera " + camera.id + " closed")
            //once the current camera has been closed, start taking another picture
            if (!cameraIds!!.isEmpty()) {
                takeAnotherPicture()
            } else {
                capturingListener!!.onDoneCapturingAllPhotos(picturesTaken)
            }
        }

        override fun onError(camera: CameraDevice, error: Int) {
            val errorMsg = when (error) {
                ERROR_CAMERA_DEVICE -> "Fatal (device)"
                ERROR_CAMERA_DISABLED -> "Device policy"
                ERROR_CAMERA_IN_USE -> "Camera in use"
                ERROR_CAMERA_SERVICE -> "Fatal (service)"
                ERROR_MAX_CAMERAS_IN_USE -> "Maximum cameras in use"
                else -> "Unknown"
            }
            Timber.e("Error when trying to connect camera $errorMsg")
            if (cameraDevice != null && !cameraClosed) {
                cameraDevice!!.close()
            }
        }
    }

    @Throws(CameraAccessException::class)
    private fun takePicture() {
        if (cameraDevice == null) {
            Timber.e("cameraDevice is null")
            return
        }
        val cameraCharacteristics = manager.getCameraCharacteristics(cameraDevice!!.id)
        var jpegSizes: Array<Size>? = null
        val streamConfigurationMap =
            cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
        if (streamConfigurationMap != null) {
            jpegSizes = streamConfigurationMap.getOutputSizes(ImageFormat.JPEG)
        }
        val jpegSizesNotEmpty = !jpegSizes.isNullOrEmpty()
        val width = if (jpegSizesNotEmpty) jpegSizes!![0].width else 640
        val height = if (jpegSizesNotEmpty) jpegSizes!![0].height else 480
        val reader = ImageReader.newInstance(width, height, ImageFormat.JPEG, 1)
        val outputSurfaces: MutableList<Surface> = mutableListOf()
        outputSurfaces.add(reader.surface)
        val captureBuilder =
            cameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE)
        captureBuilder.addTarget(reader.surface)
        captureBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO)

        val mSensorOrientation = cameraCharacteristics.get(CameraCharacteristics.SENSOR_ORIENTATION)
        captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, getOrientation(mSensorOrientation!!))


        reader.setOnImageAvailableListener(onImageAvailableListener, null)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            // Create a capture session using the predefined targets; this also involves defining the
            // session state callback to be notified of when the session is ready
            val outConfig = mutableListOf<OutputConfiguration>()
            val targets = outputSurfaces
            for (target in targets) {
                outConfig.add(OutputConfiguration(target))
            }
            val sessionConfig = SessionConfiguration(
                SessionConfiguration.SESSION_REGULAR,
                outConfig,
                cameraExecutor,
                object : CameraCaptureSession.StateCallback() {

                    override fun onConfigured(session: CameraCaptureSession) {
                        try {
                            session.capture(captureBuilder.build(), captureListener, cameraHandler)
                        } catch (e: CameraAccessException) {
                            Timber.e("Exception occurred while accessing $currentCameraId\n$e")
                        }
                    }

                    override fun onConfigureFailed(session: CameraCaptureSession) {
//                        val exc = RuntimeException("Camera ${cameraDevice?.id} session configuration failed")
//                        cont.resumeWithException(exc)
                    }
                }
            )

            cameraDevice?.createCaptureSession(sessionConfig)
            return
        }


        @Suppress("DEPRECATION")
        cameraDevice?.createCaptureSession(
            outputSurfaces,
            object : CameraCaptureSession.StateCallback() {
                override fun onConfigured(session: CameraCaptureSession) {
                    try {
                        session.capture(captureBuilder.build(), captureListener, null)
                    } catch (e: CameraAccessException) {
                        Timber.e("Exception occurred while accessing $currentCameraId\n$e")
                    }
                }

                override fun onConfigureFailed(session: CameraCaptureSession) {}
            },
            null
        )
    }

    @SuppressLint("SimpleDateFormat")
    private fun saveImageToDisk(bytes: ByteArray) {
//        val cameraId = if (cameraDevice == null) UUID.randomUUID().toString() else cameraDevice!!.id
//        val file =
//            File(Environment.getExternalStorageDirectory().toString() + "/" + cameraId + "_pic.jpg")
//        val mainFolder = File(AppConstants.SD_PATH, AppConstants.MAIN_FOLDER)
//        if (!mainFolder.exists() && !mainFolder.mkdir()) {
//            return
//        }
//        val pictureFileDir = File(mainFolder, AppConstants.INTRUDER_FOLDER)
//        if (!pictureFileDir.exists() && !pictureFileDir.mkdir()) {
//            return
//        }

        val dateFormat = SimpleDateFormat("yyyyMMdd-hhmmss")
        val fileSuffix: String = dateFormat.format(Date())
//        val photoFile = "Picture_$fileSuffix.jpg"
//        val fileName = pictureFileDir.path + File.separator + photoFile
//        val pictureFile = File(fileName)
//        Timber.e("Filename is $fileName")

        val fileOperationRequest = FileOperationRequest("IMG_$fileSuffix", FileExtension.JPEG, DirectoryType.EXTERNAL)
        val pictureFile = fileManager.createFile(fileOperationRequest, FileManager.SubFolder.INTRUDERS)
        try {
            FileOutputStream(pictureFile).use { output ->
                output.write(bytes)
                picturesTaken!![pictureFile.path] = bytes
                output.flush()
            }
        } catch (e: IOException) {
            Timber.e("Exception occurred while saving picture to external storage \n$e")
        }
    }

    private fun takeAnotherPicture() {
        currentCameraId = cameraIds!!.poll()
        openCamera()
    }

    private fun closeCamera() {
        Timber.d("closing camera " + cameraDevice!!.id)
        if (cameraDevice != null && !cameraClosed) {
            cameraDevice!!.close()
            cameraDevice = null
        }
        if (imageReader != null) {
            imageReader?.close()
            imageReader = null
        }
    }

    fun getFrontFacingCameraId(cManager: CameraManager): String? {
        try {
            for (cameraId in cManager.cameraIdList) {
                val characteristics = cManager.getCameraCharacteristics(cameraId)
                val cOrientation = characteristics.get(CameraCharacteristics.LENS_FACING)
                if (cOrientation == CameraCharacteristics.LENS_FACING_FRONT) return cameraId
            }
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
        return null
    }

}