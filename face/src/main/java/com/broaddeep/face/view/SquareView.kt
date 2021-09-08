package com.broaddeep.face.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View

class SquareView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private var leftTopY: Float = 0f
    private var leftTopX: Float = 0f
    private var rightBottomX: Float = 0f
    private var rightBottomY: Float = 0f
    private var width1 = 0f
    private var height1 = 0f
    private var ratio = 0.68f//0.68:护照  法人信息：0.7  身份证 ：0.618
    private val paint = Paint().apply {
        color = Color.GREEN
        strokeWidth = 2f
        style = Paint.Style.STROKE
    }
    private val boldPaint = Paint().apply {
        color = Color.GREEN
        strokeWidth = 8f
        style = Paint.Style.STROKE
    }
    private val path = Path()
    private val cornerSize = 40f
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        width1 = w * 0.9f
        height1 = width1 * ratio
        leftTopX = (w - width1) / 2
        leftTopY = (h - height1) / 2
        rightBottomX = w - leftTopX
        rightBottomY = h - leftTopY
        getEdgePath()
    }



    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        getEdgePath()
        canvas?.drawPath(path, paint)
        drawCorner(canvas, 0)
        drawCorner(canvas, 1)
        drawCorner(canvas, 2)
        drawCorner(canvas, 3)
    }

    /**
     * 获取边路径
     */
    private fun getEdgePath() {
        path.run {
            reset()
            moveTo(leftTopX, leftTopY)
            lineTo(rightBottomX, leftTopY)
            lineTo(rightBottomX, rightBottomY)
            lineTo(leftTopX, rightBottomY)
            lineTo(leftTopX, leftTopY)
            close()
        }
    }
    private fun drawCorner(canvas: Canvas?, flag: Int) {
        path.reset()
        when (flag) {
            0 -> {
                path.moveTo(leftTopX, leftTopY + cornerSize)
                path.lineTo(leftTopX, leftTopY)
                path.lineTo(leftTopX + cornerSize, leftTopY)
                path.lineTo(leftTopX, leftTopY)
                path.close()
            }
            1 -> {
                path.moveTo(rightBottomX - cornerSize, leftTopY)
                path.lineTo(rightBottomX, leftTopY)
                path.lineTo(rightBottomX, leftTopY + cornerSize)
                path.lineTo(rightBottomX, leftTopY)
                path.close()
            }
            2 -> {
                path.moveTo(rightBottomX, rightBottomY - cornerSize)
                path.lineTo(rightBottomX, rightBottomY)
                path.lineTo(rightBottomX - cornerSize, rightBottomY)
                path.lineTo(rightBottomX, rightBottomY)
                path.close()
            }
            3 -> {
                path.moveTo(leftTopX, rightBottomY-cornerSize)
                path.lineTo(leftTopX, rightBottomY)
                path.lineTo(leftTopX + cornerSize, rightBottomY)
                path.lineTo(leftTopX, rightBottomY)
                path.close()
            }
        }
        canvas?.drawPath(path, boldPaint)
    }
    fun getCropWidth()=width1
    fun getCropHeight()=height1
}