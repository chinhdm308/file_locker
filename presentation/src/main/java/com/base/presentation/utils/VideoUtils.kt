package com.base.presentation.utils

import android.graphics.Bitmap
import android.media.ThumbnailUtils
import android.os.Build
import android.util.Size
import timber.log.Timber
import java.io.File


/**
 * Get a thumbnail of a video. First, create a thumbnail of the video using ThumbnailUtils,
 * and then generate a thumbnail of the specified size using ThumbnailUtils.
 * If the desired width and height of the thumbnail are both smaller than MICRO_KIND,
 * use MICRO_KIND as the 'kind' parameter value to save memory.
 *
 * @param videoPath The path to the video.
 * @param width The specified width of the output video thumbnail.
 * @param height The specified height of the output video thumbnail.
 * @param kind Refer to constants MINI_KIND and MICRO_KIND in the MediaStore.Images.Thumbnails class.
 * 其中，MINI_KIND: 512 x 384，MICRO_KIND: 96 x 96
 * @return The video thumbnail of the specified size.
 */
fun getVideoThumbnail(
    videoPath: String,
    width: Int,
    height: Int,
    kind: Int
): Bitmap? {
    // Get thumbnail of video
    var bitmap: Bitmap? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        ThumbnailUtils.createVideoThumbnail(File(videoPath), Size(96, 96), null)
    } else {
        @Suppress("DEPRECATION")
        ThumbnailUtils.createVideoThumbnail(videoPath, kind)
    }

    Timber.e("w:" + bitmap?.width)
    Timber.e("h:" + bitmap?.height)
    bitmap = ThumbnailUtils.extractThumbnail(
        bitmap,
        width,
        height,
        ThumbnailUtils.OPTIONS_RECYCLE_INPUT
    )
    return bitmap
}