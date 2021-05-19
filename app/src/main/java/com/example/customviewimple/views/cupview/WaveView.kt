package com.example.customviewimple.views.cupview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View

class WaveView (context:Context, attributeSet: AttributeSet): View(context, attributeSet) {

    private val paint = Paint().apply {
        color = Color.YELLOW
    }

    private val bezierPath = Path()

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        bezierPath.addCircle(200f, 200f, 250f, Path.Direction.CW)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (canvas == null) return
        canvas.save()
        canvas.clipPath(bezierPath)
        canvas.drawColor(Color.BLUE)
        canvas.restore()
        canvas.drawCircle(100f, 100f, 50f, paint)
    }

}