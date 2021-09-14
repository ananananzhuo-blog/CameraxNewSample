package com.broaddeep.face.activity

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.broaddeep.face.R

class ImageShowActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_imageshow)
//        if (intent.hasExtra("path")) {
//            intent.getParcelableExtra<Uri>("path")?.let {
//                val decodeFile = BitmapFactory.decodeFile(it.path)
//                val height = (600f / decodeFile.width) * decodeFile.height
//                val createScaledBitmap = Bitmap.createScaledBitmap(
//                    decodeFile,
//                    600,
//                    height.toInt(),
//                    true
//                )//这里必须进行压缩，我的手机上直接绘制根本绘制不出来这么大的图片
//                findViewById<ImageView>(R.id.iv).setImageBitmap(createScaledBitmap)
//            }
//        }
        if (intent.hasExtra("path1")) {
            intent.getStringExtra("path1").let {
                val decodeFile = BitmapFactory.decodeFile(it)
                findViewById<ImageView>(R.id.iv).setImageBitmap(BitmapFactory.decodeFile(it))
            }
        }

    }
}