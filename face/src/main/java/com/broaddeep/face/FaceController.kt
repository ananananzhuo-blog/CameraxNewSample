package com.broaddeep.face

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.util.Size
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.broaddeep.face.facedete.FaceDetectionController
import com.broaddeep.face.utils.BitmapUtils
import com.example.android.camera.utils.YuvToRgbConverter
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.util.concurrent.Executors

/**
 *   @name mayong
 *   @date 2021/9/7 - 10:59
 *   @describe 活体识别的控制器
 */
class FaceController {
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    lateinit var scope: CoroutineScope
    val faceDetectionController = FaceDetectionController()
    @SuppressLint("UnsafeOptInUsageError")
    fun openCamera(context: Context, previewView: PreviewView) {
        scope = context as CoroutineScope
        cameraProviderFuture = ProcessCameraProvider.getInstance(context)//获得provider实例
        val imageAnalysis = ImageAnalysis.Builder()
            .setTargetResolution(Size(600, 600))
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
        var executor = Executors.newFixedThreadPool(5)
        imageAnalysis.setAnalyzer(executor, ImageAnalysis.Analyzer { image ->
            scope.launch(Dispatchers.IO) {
                val bitmap = Bitmap.createBitmap(image.width, image.height, Bitmap.Config.ARGB_8888)
                image.image?.let { innerImage ->
                    YuvToRgbConverter(context).yuvToRgb(
                        image = innerImage,
                        bitmap
                    )//将image转化为bitmap，参考：https://github.com/android/camera-samples/blob/3730442b49189f76a1083a98f3acf3f5f09222a3/CameraUtils/lib/src/main/java/com/example/android/camera/utils/YuvToRgbConverter.kt
                }
                val path =BitmapUtils.saveImageToGallery(bitmap,"${context.getExternalFilesDir("")}${File.separator}detectionPic","${System.currentTimeMillis()}.jpg")
                faceDetectionController.detection(path){isSucc, result ->
                    image.close()//这里调用了close就会继续生成下一帧图片
                }
            }

        })
        cameraProviderFuture.addListener(Runnable {
            val cameraProvider = cameraProviderFuture.get()
            bindPreview(context,cameraProvider, imageAnalysis, previewView)
        }, ContextCompat.getMainExecutor(context))

    }

    /**
     * 绑定预览view
     */
    fun bindPreview(
        context: Context,
        cameraProvider: ProcessCameraProvider,
        imageAnalysis: ImageAnalysis,
        previewView: PreviewView
    ) {
        val preview: Preview = Preview.Builder()
            .build()
        val cameraSelector: CameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
            .build()
        preview.setSurfaceProvider(previewView.surfaceProvider)
        var camera = cameraProvider.bindToLifecycle(
            context as LifecycleOwner ,
            cameraSelector,
            imageAnalysis,
            preview
        )
    }
}