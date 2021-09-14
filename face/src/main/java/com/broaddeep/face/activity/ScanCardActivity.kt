package com.broaddeep.face.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.camera.view.PreviewView
import com.broaddeep.face.R
import com.broaddeep.face.ScanCardController
import com.broaddeep.face.view.SquareView
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

/**
 *   @name mayong
 *   @date 2021/9/7 - 17:25
 *   @describe  带方形取景框的扫描activity
 */
class ScanCardActivity : AppCompatActivity() {
    private lateinit var scanCardController: ScanCardController
    val scope = MainScope()

    companion object {
        const val REQUESTCODE = 1
        const val RESULT_CODE = 1
        fun startForResult(activity: Activity) {
            activity.startActivityForResult(
                Intent(activity, ScanCardActivity::class.java),
                REQUESTCODE
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_card)
        requestPermissions(arrayOf(Manifest.permission.CAMERA), 1)
        val squareView = findViewById<SquareView>(R.id.squareview)
        val preview = findViewById<PreviewView>(R.id.previewView_scancard)
        val ivTakePic = findViewById<TextView>(R.id.tv_takepic)
        scanCardController = ScanCardController()
        scanCardController.openCamera(this, preview)
        ivTakePic.setOnClickListener {
//            takePicToFile(preview,squareView)
            takePicToMemory()
        }
    }

    private fun takePicToMemory() {
        scanCardController.takePicToMemory(this, scope) {
            val resultIntent = Intent()
            resultIntent.putExtra("path", it)
            setResult(RESULT_CODE, resultIntent)
            finish()
        }
    }

    fun takePicToFile(preview: PreviewView, squareView: SquareView) {
        scanCardController.takePicToFile(
            this, scope, squareView.getCropHeight(),
            preview.measuredWidth.toFloat(),
            preview.measuredHeight.toFloat()
        ) { path ->
            val resultIntent = Intent()
            resultIntent.putExtra("path", path)
            setResult(RESULT_CODE, resultIntent)
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        scanCardController.shutDown()
        scope.cancel()
    }

}