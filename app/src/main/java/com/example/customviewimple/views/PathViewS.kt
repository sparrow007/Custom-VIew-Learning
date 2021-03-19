package com.example.customviewimple.views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class PathViewS(context: Context, attributeSet: AttributeSet) : View(context,attributeSet) {

    private val paint = Paint()
    private val path = Path()
    init {

        paint.color = Color.RED
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 10f

    }

    override fun onDraw(canvas: Canvas?) {

        if(canvas == null) return

        //Start from center fo the screen
        canvas.translate(width / 2f, height /2f)
        canvas.scale(1f, -1f)

        path.lineTo(100f, 100f)

        val rect = RectF(0f,0f, 300f, 300f)

        path.arcTo(rect, 0f, 270f)

        canvas.drawPath(path, paint)
    }


}