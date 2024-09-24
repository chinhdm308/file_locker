package com.base.presentation.utils

import android.os.Build
import android.os.Environment
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader


class SdCardUtil {

    companion object {

        /**
         * Do you need to detect the external sd card path (android 4.4 version cannot read and write)
         *
         * @return
         */
        @JvmStatic
        fun needCheckExtSdCard(): Boolean {
            val sdkInt = Build.VERSION.SDK_INT
            return sdkInt == 19 || sdkInt == 20
        }

        /**
         * Get the external SD card path
         *
         * @return It should be just one record or empty
         */
        @JvmStatic
        fun getExtSdCardPath(): String? {
            val lResult: MutableList<String?> = ArrayList()
            try {
                val rt = Runtime.getRuntime()
                val proc = rt.exec("mount")
                val `is` = proc.inputStream
                val isr = InputStreamReader(`is`)
                val br = BufferedReader(isr)
                var line: String
                while (br.readLine().also { line = it } != null) {
                    if (line.contains("extSdCard")) {
                        val arr = line.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                        val path = arr[1]
                        val file = File(path)
                        if (file.isDirectory) {
                            lResult.add(path)
                        }
                    }
                }
                isr.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return if (lResult.size == 0) null else lResult[0]
        }

        // The return value does not contain File seperater "/". If there is no external second SD card, null is returned.
        @JvmStatic
        fun getSecondExternalPath(): String? {
            val paths: List<String> = getAllExternalSdCardPath()
            return if (paths.size == 2) {
                for (path in paths) {
                    if (path != getFirstExternalPath()) {
                        return path
                    }
                }
                null
            } else {
                null
            }
        }

        @JvmStatic
        fun isFirstSdCardMounted(): Boolean {
            return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
        }

        @JvmStatic
        fun isSecondSdCardMounted(): Boolean {
            val sd2 = getSecondExternalPath() ?: return false
            return checkFsWritable(sd2 + File.separator)
        }

        // Test whether the external SD card is unmounted. You cannot directly determine whether the external SD card is null,
        // because when the external SD card is pulled out, the external SD card path can still be obtained.
        // My method is to follow the Android Google test DICM method, create a file, and then delete it immediately to see if the external SD card is uninstalled.
        // Note that there is a small bug here. Even if the external SD card is not uninstalled, the storage space is not large enough, or The number of files has reached the maximum number.
        // At this time, new files cannot be created. At this time, the user will be prompted to clean up the SD card.
        @JvmStatic
        private fun checkFsWritable(dir: String?): Boolean {
            if (dir == null) return false
            val directory = File(dir)
            if (!directory.isDirectory) {
                if (!directory.mkdirs()) {
                    return false
                }
            }
            val f = File(directory, ".keysharetestgzc")
            try {
                if (f.exists()) {
                    f.delete()
                }
                if (!f.createNewFile()) {
                    return false
                }
                f.delete()
                return true
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return false
        }

        @JvmStatic
        fun getFirstExternalPath(): String {
            return Environment.getExternalStorageDirectory().path
        }

        @JvmStatic
        fun getAllExternalSdCardPath(): List<String> {
            val sdList: MutableList<String> = ArrayList()
            val firstPath = getFirstExternalPath()

            // 得到路径
            try {
                val runtime = Runtime.getRuntime()
                val proc = runtime.exec("mount")
                val `is` = proc.inputStream
                val isr = InputStreamReader(`is`)
                var line: String
                val br = BufferedReader(isr)
                while (br.readLine().also { line = it } != null) {
                    // 将常见的linux分区过滤掉
                    if (line.contains("secure")) continue
                    if (line.contains("asec")) continue
                    if (line.contains("media")) continue
                    if (line.contains("system") || line.contains("cache")
                        || line.contains("sys") || line.contains("data")
                        || line.contains("tmpfs") || line.contains("shell")
                        || line.contains("root") || line.contains("acct")
                        || line.contains("proc") || line.contains("misc")
                        || line.contains("obb")
                    ) {
                        continue
                    }
                    if (line.contains("fat") || line.contains("fuse") || line
                            .contains("ntfs")
                    ) {
                        val columns = line.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                        if (columns.size > 1) {
                            val path = columns[1]
                            if (!sdList.contains(path) && path.contains("sd")) sdList.add(columns[1])
                        }
                    }
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
            if (!sdList.contains(firstPath)) {
                sdList.add(firstPath)
            }
            return sdList
        }
    }
}