package com.example.customviewimple.views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class ThirdBezier (context: Context, attributeSet: AttributeSet): View(context, attributeSet){


    private val paint = Paint()
    private val path = Path()
    private var start = PointF()
    private var end = PointF()
    private var controlF = PointF()
    private var controlS = PointF()

    private var centerX = 0
    private var centerY = 0

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        centerX = w / 2
        centerY = h / 2

        start.x = centerX - 200f
        start.y = centerY.toFloat()

        end.x = centerX + 200f
        end.y = centerY.toFloat()

        controlF.x = centerX - 200f
        controlF.y = centerY - 200f

        controlS.x = centerX + 200f
        controlS.y = centerY - 200f

        paint.color = Color.BLACK
        paint.strokeWidth = 10f

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (canvas == null) return
        //draw points
        canvas.drawPoint(start.x, start.y, paint)
        canvas.drawPoint(end.x, end.y, paint)
        canvas.drawPoint(controlF.x, controlF.y, paint)
        canvas.drawPoint(controlS.x, controlS.y, paint)

        //draw lines
        paint.color = Color.BLUE
        paint.strokeWidth = 4f
        canvas.drawLine(start.x, start.y, controlF.x, controlF.y, paint)
        canvas.drawLine(end.x, end.y, controlS.x, controlS.y, paint)
        canvas.drawLine(controlF.x, controlF.y, controlS.x, controlS.y, paint)

    }
}