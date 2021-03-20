package com.example.customviewimple.views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
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

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
    }

}