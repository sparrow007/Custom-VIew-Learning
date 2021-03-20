package com.example.customviewimple.views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class SecondBezier(context: Context, attributeSet: AttributeSet): View(context, attributeSet) {

    private val paint = Paint()
    private val path = Path()
    private var centerX = 0
    private var centerY = 0

    private val start: PointF = PointF(0f,0f)
    private val end: PointF = PointF(0f, 0f)
    private val control: PointF = PointF(0f, 0f)

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        centerX = w / 2
        centerY = h / 2

        start.x = centerX - 200f
        start.y = centerY.toFloat()

        end.x = centerX + 200f
        end.y = centerY.toFloat()

        control.x = centerX.toFloat()
        control.y = centerY - 200f

        paint.color = Color.BLACK
        paint.strokeWidth =  10f
        paint.style = Paint.Style.STROKE


    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {


        return true
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (canvas == null) return

        //draw points at data and control points
        canvas.drawPoint(start.x, start.y, paint)
        canvas.drawPoint(end.x, end.y, paint)
        canvas.drawPoint(control.x, control.y, paint)


        //draw lines
        paint.color = Color.BLUE
        paint.strokeWidth = 3f
        canvas.drawLine(start.x, start.y, control.x, control.y, paint)
        canvas.drawLine(end.x, end.y, control.x, control.y, paint)


    }

}