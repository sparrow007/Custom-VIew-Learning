package com.example.customviewimple.views.path

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class ObjectFollow(context: Context, attributeSet: AttributeSet): View(context, attributeSet) {

    val path = Path()
    val paint = Paint().apply {
        color = Color.GREEN
        style = Paint.Style.STROKE
        strokeWidth = 10f
        pathEffect = CornerPathEffect(30f)
    }

    private var pathLength = 0f
    private var pathProgress = 0f
    lateinit var pathEffect: DashPathEffect

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        path.moveTo(100f, height/2f)
//       val rect = RectF(0f, 0f, 200f, 200f)
//        path.addRect(rect,Path.Direction.CW)
        path.lineTo(450f,height/2f)
        path.lineTo(450f, height/2f+ 400f)
        path.lineTo(100f, height/2f+400f)
        path.lineTo(100f, height/2f)

        val pathMeasure = PathMeasure(path, false)
        pathLength = pathMeasure.length

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if(canvas == null) return
        //canvas.translate(width / 2f, height / 2f)

        pathEffect = DashPathEffect(floatArrayOf(pathLength, pathLength), pathLength - pathLength * pathProgress)
        paint.pathEffect = pathEffect
        canvas.drawPath(path, paint)

    }


    fun setPercentage(percentage: Float) {
        this.pathProgress = percentage
        invalidate()
    }

}