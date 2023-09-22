package com.base.basemvvmcleanarchitecture.utils

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.nio.charset.StandardCharsets
import javax.inject.Inject
import javax.inject.Singleton


class FileServiceUtils @Inject constructor(@ApplicationContext val context: Context) {

    /**
     * save document
     *
     * @param fileName
     * file name
     * @param content
     * document content
     * @throws Exception
     */
    fun save(fileName: String, content: String) {
        try {
            // Since the page input is all text information, when the file name does not end with the .txt suffix,
            // the .txt suffix is automatically added.
            var newFileName = fileName
            if (!newFileName.endsWith(".txt")) {
                newFileName = "$newFileName.txt"
            }
            val buf = newFileName.toByteArray(charset("iso8859-1"))
            Timber.e(String(buf, StandardCharsets.UTF_8))
            newFileName = String(buf, StandardCharsets.UTF_8)
            Timber.e(newFileName)

            // Context.MODE_PRIVATE：为默认操作模式，代表该文件是私有数据，只能被应用本身访问，在该模式下，写入的内容会覆盖原文件的内容，如果想把新写入的内容追加到原文件中。可以使用Context.MODE_APPEND
            // Context.MODE_APPEND：模式会检查文件是否存在，存在就往文件追加内容，否则就创建新文件。
            // Context.MODE_WORLD_READABLE和Context.MODE_WORLD_WRITEABLE用来控制其他应用是否有权限读写该文件。
            // MODE_WORLD_READABLE：表示当前文件可以被其他应用读取；MODE_WORLD_WRITEABLE：表示当前文件可以被其他应用写入。
            // 如果希望文件被其他应用读和写，可以传入：openFileOutput("output.txt", Context.MODE_WORLD_READABLE + Context.MODE_WORLD_WRITEABLE);
            context.openFileOutput(newFileName, Context.MODE_PRIVATE).use {
                it.write(content.toByteArray())
                it.flush()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Read file contents
     *
     * @param fileName
     * file name
     * @return document content
     * @throws Exception
     */
    @Throws(java.lang.Exception::class)
    fun read(fileName: String): String {

        // Since the page input is all text information, when the file name does not end with the .txt suffix,
        // the .txt suffix is automatically added.
        var newFileName = fileName
        if (!fileName.endsWith(".txt")) {
            newFileName = "$fileName.txt"
        }
        val fis = context.openFileInput(newFileName)
        val baos = ByteArrayOutputStream()
        val buf = ByteArray(1024)
        var len: Int

        // 将读取后的数据放置在内存中---ByteArrayOutputStream
        while (fis.read(buf).also { len = it } != -1) {
            baos.write(buf, 0, len)
        }
        fis.close()
        baos.close()

        // 返回内存中存储的数据
        return baos.toString()
    }

    fun createShareImage(bm: Bitmap, context: Context?): String? {
        val name = "lockwiz_show_2.0.jpg"
        // File dir = context.getFilesDir();
        val dir = File(Environment.getExternalStorageDirectory(), "lockwiz")
        if (!dir.exists()) {
            dir.mkdir()
        }
        val file = File(dir.absolutePath, name)
        if (!file.exists()) {
            val s: Boolean = save(bm, dir.absolutePath, name, false)
            Timber.d("share pic :$s")
        }
        Timber.d("file:" + file.absolutePath)
        Timber.d("uri:" + Uri.fromFile(file).toString())
        return file.absolutePath
    }

    private fun save(bm: Bitmap, dir: String, name: String, alpha: Boolean): Boolean {
        var fos: FileOutputStream? = null
        try {
            val file = File(dir)
            if (!file.exists()) {
                file.mkdirs()
            }
            Timber.d("save:$dir/$name")
            fos = FileOutputStream("$dir/$name")
            return if (alpha) {
                bm.compress(Bitmap.CompressFormat.PNG, 60, fos)
            } else {
                bm.compress(Bitmap.CompressFormat.JPEG, 60, fos)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        } finally {
            if (fos != null) {
                try {
                    fos.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        return false
    }
}