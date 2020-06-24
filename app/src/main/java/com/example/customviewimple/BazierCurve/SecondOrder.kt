package com.example.customviewimple.BazierCurve

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class SecondOrder(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private var isSecondPoint: Boolean = false
    private var startPointX : Float = 0f
    private var startPointY : Float = 0f
    private var endPointX : Float = 0f
    private var endPointY : Float = 0f
    private var controlPointX : Float = 0f
    private var controlPointY : Float = 0f

    private var secondControlPointX : Float = 0f
    private var secondControlPointY : Float = 0f

    private val mPaint = Paint()

    private val mPath = Path()

    init {
        mPaint.color = Color.BLACK
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = 20f
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        startPointX = w / 4f
        startPointY = h / 2 - 200f

        endPointX = w * 3 / 4f
        endPointY = h / 2 - 200f

        controlPointX = w / 3f
        controlPointY = h / 2 - 300f


        secondControlPointX = w*3/3f
        secondControlPointY = h /2 -300f
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        mPath.reset()
        mPath.moveTo(startPointX, startPointY)
        mPath.cubicTo(controlPointX, controlPointY, secondControlPointX, secondControlPointY, endPointX, endPointY)
        //mPath.quadTo(controlPointX, controlPointY, endPointX, endPointY)
        canvas?.drawPoint(startPointX, startPointY, mPaint)
        canvas?.drawPoint(endPointX, endPointY, mPaint)
        canvas?.drawPoint(controlPointX, controlPointY, mPaint)
        canvas?.drawLine(startPointX, startPointY, controlPointX, controlPointY, mPaint)
        canvas?.drawLine(controlPointX, controlPointY, endPointX, endPointY, mPaint)

        canvas?.drawPath(mPath, mPaint)

    }
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_POINTER_DOWN -> isSecondPoint = true
            MotionEvent.ACTION_POINTER_UP -> isSecondPoint = false
            MotionEvent.ACTION_MOVE -> {
                controlPointX = event.getX(0)
                controlPointY = event.getY(0)
                if (isSecondPoint) {
                    secondControlPointX = event.getX(1)
                    secondControlPointY = event.getY(1)
                }
                invalidate()
            }
        }
        return true
    }


}