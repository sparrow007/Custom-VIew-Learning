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
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if(canvas == null) return
        //canvas.translate(width / 2f, height / 2f)

        path.moveTo(0f, height/2f)
//       val rect = RectF(0f, 0f, 200f, 200f)
//        path.addRect(rect,Path.Direction.CW)
        path.lineTo(450f,height/2f)

        canvas.drawPath(path, paint)
    }




}