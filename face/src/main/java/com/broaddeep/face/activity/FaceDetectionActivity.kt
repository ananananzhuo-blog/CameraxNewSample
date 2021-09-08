package com.broaddeep.face.activity


import android.Manifest
import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Size
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.broaddeep.face.FaceController
import com.broaddeep.face.R
import com.example.android.camera.utils.YuvToRgbConverter
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.*
import java.lang.Runnable
import java.nio.ByteBuffer
import java.util.concurrent.Executors
import kotlin.concurrent.thread
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 *   @name mayong
 *   @date 2021/9/7 - 10:59
 *   @describe 活体识别的activity
 */
class FaceDetectionActivity : AppCompatActivity(), CoroutineScope {

    lateinit var faceController: FaceController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_face_detection)
        faceController = FaceController()
        faceController.openCamera(this ,findViewById(R.id.previewView))
    }

    override fun onDestroy() {
        super.onDestroy()
        cancel()
    }

    override val coroutineContext: CoroutineContext
        get() = SupervisorJob() + Dispatchers.Main
}