package com.broaddeep.face

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ExifInterface
import android.os.Build
import android.util.Size
import androidx.annotation.RequiresApi
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.broaddeep.face.activity.ImageShowActivity
import com.broaddeep.face.utils.BitmapUtils
import com.broaddeep.face.utils.logE
import com.example.android.camera.utils.YuvToRgbConverter
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.*
import java.io.ByteArrayInputStream
import java.io.File
import java.lang.Runnable
import java.nio.ByteBuffer
import java.util.concurrent.Executors

/**
 *   @name mayong
 *   @date 2021/9/7 - 10:59
 *   @describe 方形取景框的控制器
 */
class ScanCardController {
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private lateinit var imageCapture: ImageCapture
    fun openCamera(
        context: Context,
        previewView: PreviewView
    ) {
        cameraProviderFuture = ProcessCameraProvider.getInstance(context)//获得provider实例
        val imageAnalysis = ImageAnalysis.Builder()
            .setTargetResolution(Size(previewView.measuredWidth, previewView.measuredHeight))
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
        cameraProviderFuture.addListener(Runnable {
            val cameraProvider = cameraProviderFuture.get()
            bindPreview(context, cameraProvider, imageAnalysis, previewView)
        }, ContextCompat.getMainExecutor(context))

    }

    fun takePicToFile(context: Context, scope:CoroutineScope, cropHeight: Float, previewWidth: Float, previewHeight: Float, callback:(path:String)->Unit) {
        var file = File(context.getExternalFilesDir(""), "File")
        if (!file.exists()) {
            file.mkdirs()
        }
        val photoFile = File(file, "${System.currentTimeMillis()}.jpg")
        val outputFileOptions = ImageCapture.OutputFileOptions.Builder(photoFile)
            .build()//构建输出选项
        var cameraExecutor = Executors.newSingleThreadExecutor()
        //点击拍照
        imageCapture.takePicture(outputFileOptions, cameraExecutor,
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(error: ImageCaptureException) {
                    error.printStackTrace()
                }

                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    scope.launch {
                        val bitmap = BitmapFactory.decodeFile(photoFile.absolutePath)
                        val xScale =
                            (bitmap.height * previewWidth / previewHeight) / bitmap.width//拍照的时候相机预览的宽度是高于PreviewView的宽度，所以这里要计算一个虚拟的preview宽度
                        val cropBitmap =
                            getCropBitmap(bitmap, xScale, cropHeight / previewHeight)
                        photoFile.delete()
                        val savePath =
                            context.getExternalFilesDir("")?.absolutePath + File.separator + "temppic"
                        val path =BitmapUtils.saveImageToGallery(
                            cropBitmap,
                            savePath,
                            "${System.currentTimeMillis()}.jpg"
                        )
                        callback.invoke(path)
                    }
                }
            })
    }

    fun  takePicToMemory(context: Context, scope: CoroutineScope, callback:(path:String)->Unit){
        var file = File(context.getExternalFilesDir(""), "File")
        if (!file.exists()) {
            file.mkdirs()
        }
//        val photoFile = File(file, "${System.currentTimeMillis()}.jpg")
        var cameraExecutor = Executors.newSingleThreadExecutor()
        imageCapture.setCropAspectRatio()
        imageCapture.takePicture(cameraExecutor,object : ImageCapture.OnImageCapturedCallback() {
            @RequiresApi(Build.VERSION_CODES.N)
            @SuppressLint("UnsafeOptInUsageError")
            override fun onCaptureSuccess(image: ImageProxy) {
                super.onCaptureSuccess(image)
                scope.launch (Dispatchers.IO){
                    // 将 imageProxy 转为 byte数组
                    val buffer: ByteBuffer = image.planes[0].buffer
                    // 新建指定长度数组
                    val byteArray = ByteArray(buffer.remaining())
                    // 倒带到起始位置 0
                    buffer.rewind()
                    // 数据复制到数组, 这个 byteArray 包含有 exif 相关信息，
                    // 由于 bitmap 对象不会包含 exif 信息，所以转为 bitmap 需要注意保存 exif 信息
                    buffer.get(byteArray)
                    // 获取照片 Exif 信息
//                    val byteArrayInputStream = ByteArrayInputStream(byteArray)
//                    val orientation = ExifInterface(byteArrayInputStream)

                    var bitmap = BitmapFactory.decodeByteArray(byteArray,0,byteArray.size)
                    bitmap=BitmapUtils.rotate(bitmap,image.imageInfo.rotationDegrees.toFloat())
                    val savedPath = BitmapUtils.saveImageToGallery(
                        bitmap,
                        file.absolutePath,
                        "${System.currentTimeMillis()}.jpg"
                    )
                    withContext(Dispatchers.Main){
                        callback.invoke(savedPath)

                    }

                    image.close()

                }
            }

        })
    }
    private suspend fun getCropBitmap(bitmap: Bitmap, wScale: Float, hScale: Float): Bitmap =
        withContext(Dispatchers.IO) {
            val width = bitmap.width * wScale
            val height = bitmap.height * hScale
            val x = (bitmap.width - width) / 2
            val y = (bitmap.height - height) / 2
            val cropBitmap =
                Bitmap.createBitmap(bitmap, x.toInt(), y.toInt(), width.toInt(), height.toInt())
            if (!bitmap.isRecycled) {
                bitmap.recycle()
            }
            cropBitmap
        }
    lateinit var cameraProvider:ProcessCameraProvider
    /**
     * 绑定预览view
     */
    private fun bindPreview(
        context: Context,
        cameraProvider: ProcessCameraProvider,
        imageAnalysis: ImageAnalysis,
        previewView: PreviewView
    ) {
        this.cameraProvider=cameraProvider
        val preview: Preview = Preview.Builder()
            .build()
        val cameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()
        preview.setSurfaceProvider(previewView.surfaceProvider)
        imageCapture = ImageCapture.Builder()
            .setTargetRotation(previewView.display.rotation)
            .build()
        logE("previewView.display.rotation${previewView.display.rotation}")
        var camera = cameraProvider.bindToLifecycle(
            context as LifecycleOwner,
            cameraSelector,
            imageCapture,
            imageAnalysis,
            preview
        )
    }

    @SuppressLint("RestrictedApi")
    fun shutDown(){
       cameraProvider.shutdown()
    }
}