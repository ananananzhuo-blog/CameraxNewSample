package com.broaddeep.face.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.TextView
import com.broaddeep.face.R

/**
 *   @name mayong
 *   @date 2021/9/7 - 15:11
 *   @describe 企业信息采集dialog
 */
class PicTypeDialog(context: Context) : Dialog(context) {

    private var onSelectListener: ((flag: Int) -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_pictype)
        setCanceledOnTouchOutside(false)
        initView()
    }

    private fun initView() {
        val tvIdcard = findViewById<TextView>(R.id.tv_idcard)
        val tvhuzhao = findViewById<TextView>(R.id.tv_huzhao)
        val tvFaren = findViewById<TextView>(R.id.tv_sydwzj)
        tvIdcard.setOnClickListener {
            onSelectListener?.invoke(0)
            dismiss()
        }
        tvhuzhao.setOnClickListener {
            onSelectListener?.invoke(1)
            dismiss()
        }
        tvFaren.setOnClickListener {
            onSelectListener?.invoke(2)
            dismiss()
        }
    }

    fun setOnSelectListener(onSelectListener: (flag: Int) -> Unit) {
        this.onSelectListener = onSelectListener
    }
}