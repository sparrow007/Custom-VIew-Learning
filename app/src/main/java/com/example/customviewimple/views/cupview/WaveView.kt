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
        color = Color.BLUE
    }

    private val bezierPath = Path()

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        bezierPath.addCircle(200f, 200f, 50f, Path.Direction.CW)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (canvas == null) return
        canvas.clipPath(bezierPath)
        canvas.drawColor(Color.BLUE)
    }

}