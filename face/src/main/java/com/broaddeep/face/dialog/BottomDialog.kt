package com.broaddeep.face.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import com.broaddeep.face.R


class BottomDialog(
    context: Context,
    val iscancelable: Boolean = false,
    val isBackCancelable: Boolean = true
) : Dialog(context, R.style.MyDialog) {

    private var onSelectListener: ((isCamera: Boolean) -> Unit)? = null
    private var onCancelListener: (() -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initConfig()
        initView()
    }

    private fun initConfig() {
        val view = View.inflate(context, R.layout.dialog_bottomview, null)
        setContentView(view)//这行一定要写在前面
        setCancelable(iscancelable)//点击外部不可dismiss
        setCanceledOnTouchOutside(isBackCancelable)
        window?.setGravity(Gravity.BOTTOM)
        window?.setWindowAnimations(R.style.dialog_anim_style)
        val params = window?.attributes
        params?.width = WindowManager.LayoutParams.MATCH_PARENT;
        params?.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window?.attributes = params
    }

    private fun initView() {
        val tvCancel = findViewById<TextView>(R.id.tv_bottomdialog_cancel)
        val tvCamera = findViewById<TextView>(R.id.tv_bottomdialog_takepic)
        val tvSelectPic = findViewById<TextView>(R.id.tv_bottomdialog_selectpic)
        tvCamera.setOnClickListener {
            onSelectListener?.invoke(true)//选择相机返回true
            dismiss()
        }
        tvSelectPic.setOnClickListener {
            onSelectListener?.invoke(false)
            dismiss()
        }
        tvCancel.setOnClickListener {
            dismiss()
        }
    }

    fun setOnSelectListener(listener: (isCamera: Boolean) -> Unit) {
        this.onSelectListener = listener
    }

    fun setOnCancelListener(listener: () -> Unit) {
        this.onCancelListener = listener
    }
}