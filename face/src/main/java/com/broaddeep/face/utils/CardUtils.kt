package com.broaddeep.face.utils

import android.app.Activity
import android.content.Intent
import com.broaddeep.face.activity.ImageShowActivity
import com.broaddeep.face.activity.ScanCardActivity

object CardUtils {
    fun takePic(activity: Activity){
        ScanCardActivity.startForResult(activity)
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?):String {
        return if(requestCode==ScanCardActivity.REQUESTCODE&&resultCode==ScanCardActivity.RESULT_CODE){
            data?.getStringExtra("path")?:""
        }else{
            ""
        }
    }
}