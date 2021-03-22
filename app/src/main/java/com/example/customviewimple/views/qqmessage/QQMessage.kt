package com.example.customviewimple.views.qqmessage

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import java.lang.Math.pow
import kotlin.math.sqrt

class QQMessage (context: Context, attributeSet: AttributeSet): View(context, attributeSet) {

    private val paint = Paint()
    private var mBigCircleX = 0f
    private var mBigCircleY = 0f
    private var mBigCircleRadius = 50f

    private var mSmallCircleX = 0f
    private var mSmallCircleY = 0f
    private var mSmallCircleShowRadius = 4f
    private var mSmallCircleHideRadius = 0f
    private var mSmallCircleRadius = mSmallCircleShowRadius

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (canvas == null) return

        if (mSmallCircleHideRadius <= mSmallCircleShowRadius) {
            canvas.drawCircle(mSmallCircleX, mSmallCircleY, mSmallCircleRadius, paint)
        }

        canvas.drawCircle(mBigCircleX, mBigCircleY, mBigCircleRadius, paint)
    }

    private fun calDistance(): Int {
        return sqrt(pow((mSmallCircleX - mBigCircleX).toDouble(), 2.toDouble())+ pow((mSmallCircleY - mBigCircleY).toDouble(), 2.toDouble())).toInt()
    }
}