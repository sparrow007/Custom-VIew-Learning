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
        canvas.scale(1f, -1f)

        //path.lineTo(200f, 200f)

        //Move to

        path.addRect(-200f, -200f, 200f,
            200f, Path.Direction.CW)

        val src = Path()
        src.addCircle(0f,0f,100f, Path.Direction.CW)

        path.addPath(src, 0f, 200f)

        canvas.drawPath(path, paint)
    }


}