package com.example.customviewimple.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
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

        path.lineTo(200f, 200f)

        //Move to

        path.lineTo(200f, 0f)
        //it close the path
        //collapse the first point and the last point
        path.close()
        canvas.drawPath(path, paint)
    }


}