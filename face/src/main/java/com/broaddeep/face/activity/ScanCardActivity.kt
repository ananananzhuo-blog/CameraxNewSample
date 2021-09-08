package com.broaddeep.face.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_card)
        val squareView = findViewById<SquareView>(R.id.squareview)
        val preview = findViewById<PreviewView>(R.id.previewView_scancard)
        val ivTakePic = findViewById<TextView>(R.id.tv_takepic)
         scanCardController = ScanCardController()
        scanCardController.openCamera(this, preview)
        ivTakePic.setOnClickListener {
            scanCardController.takePic(
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
    }

    override fun onDestroy() {
        super.onDestroy()
        scanCardController.shutDown()
        scope.cancel()
    }

}