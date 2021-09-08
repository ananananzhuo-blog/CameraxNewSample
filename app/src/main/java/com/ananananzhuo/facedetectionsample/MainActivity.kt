package com.ananananzhuo.facedetectionsample

import android.content.Intent
import android.widget.Toast
import com.ananananzhuo.mvvm.activity.CustomAdapterActivity
import com.ananananzhuo.mvvm.bean.bean.ItemData
import com.ananananzhuo.mvvm.callback.CallData
import com.ananananzhuo.mvvm.callback.Callback
import com.broaddeep.face.dialog.BottomDialog
import com.broaddeep.face.activity.FaceDetectionActivity
import com.broaddeep.face.activity.ImageShowActivity
import com.broaddeep.face.activity.ScanCardActivity
import com.broaddeep.face.dialog.PicTypeDialog
import com.broaddeep.face.utils.CardUtils

class MainActivity : CustomAdapterActivity() {
    override fun getAdapterDatas(): MutableList<ItemData> = mutableListOf(
        ItemData(title = "使用Camera2预览", callback = object : Callback {
            override fun callback(callData: CallData) {
                startActivity(Intent(this@MainActivity, FaceDetectionActivity::class.java))
            }
        }),
        ItemData(title = "底部弹出dialog", callback = object : Callback {
            override fun callback(callData: CallData) {
                val dialog = BottomDialog(this@MainActivity)
                dialog.show()
                dialog.setOnSelectListener { isCamera ->
                    Toast.makeText(
                        this@MainActivity, if (isCamera) {
                            "选择相机"
                        } else {
                            "选择相册"
                        }, Toast.LENGTH_LONG
                    ).show()
                }
            }
        }),
        ItemData(title = "选择企业信息采集类型", callback = object : Callback {
            override fun callback(callData: CallData) {
                val dialog = PicTypeDialog(this@MainActivity)
                dialog.show()
                dialog.setOnSelectListener { flag ->
                    when (flag) {
                        0 -> {

                        }
                        1 -> {

                        }
                        2 -> {

                        }
                    }
                }
            }
        }),
        ItemData(title = "跳转取景框拍照页面", callback = object : Callback {
            override fun callback(callData: CallData) {
                ScanCardActivity.startForResult(this@MainActivity)
            }
        }),
    )

    override fun showFirstItem(): Boolean {
        return false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        CardUtils.onActivityResult(requestCode,resultCode,data)

    }

}