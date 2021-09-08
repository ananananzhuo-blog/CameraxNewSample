package com.broaddeep.face.utils

import android.graphics.Bitmap
import android.graphics.Matrix
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import android.content.Intent

import android.os.Environment
import java.text.SimpleDateFormat
import java.util.*


object BitmapUtils {
    /**
     * 镜像
     * @param bitmap
     * @return
     */
    fun mirror(bitmap: Bitmap): Bitmap? {
        val matrix = Matrix()
        matrix.postScale(-1f, 1f)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    /**
     * 旋转图片
     * @param bitmap
     * @param degress
     * @return
     */
    fun rotate(bitmap: Bitmap, degress: Float): Bitmap? {
        val matrix = Matrix()
        matrix.postRotate(degress)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    fun saveImageToGallery(bmp: Bitmap, path: String, name: String): String {
        val directory = File(path)
        if (!directory.exists()) {
            directory.mkdirs()
        }
        val file = File(directory,name)
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(file)
            bmp.compress(Bitmap.CompressFormat.JPEG, 50, fos)
            fos.flush()
            //通知系统相册刷新
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                fos?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return file.absolutePath
    }
}