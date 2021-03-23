package com.example.customviewimple.views.qqmessage

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
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
    private var mSmallCircleShowRadius = 40f
    private var mSmallCircleHideRadius = 15f
    private var mSmallCircleRadius = mSmallCircleShowRadius

    private var p1x = 0f
    private var p1y = 0f

    private var p2x = 0f
    private var p2y = 0f

    private var p3x = 0f
    private var p3y = 0f

    private var p4x = 0f
    private var p4y = 0f

    private var mControlx = 0f
    private var mControly = 0f
    private var dx = 0f
    private var dy = 0f

    private var mAngle = 0f

    private var bezierPath = Path()
    private var mBezierPath = Path()

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        if (event == null) return false

        val downX = event.x
        val downY = event.y

        when (event.action) {

            MotionEvent.ACTION_DOWN -> {
                mSmallCircleRadius = mSmallCircleShowRadius
                mBigCircleX = downX
                mSmallCircleX = downX
                mSmallCircleY = downY
                mBigCircleY = downY

            }

            MotionEvent.ACTION_MOVE -> {
                mBigCircleX = downX
                mBigCircleY = downY
                val distance = calDistance()
                mSmallCircleRadius = mSmallCircleShowRadius - distance / mSmallCircleHideRadius

            }

            MotionEvent.ACTION_UP -> {
                mSmallCircleRadius = 0f
            }

        }

        invalidate()
        return true
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