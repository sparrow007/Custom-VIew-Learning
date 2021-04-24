package com.example.customviewimple.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class Polygons(context:Context, attributeSet: AttributeSet): View(context, attributeSet) {


    private val paint = Paint().apply {
        color = Color.BLACK
        style = Paint.Style.FILL
    }

    private val path = Path()

    var sides: Int = 3
    var radius: Int = 170
    private var angle:Float = 60f

    private var initialX = 0f
    private var initialY = 0f

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        initialX = w/2f
        initialY = h / 2f
        angle = ((2 * PI) / 3f).toFloat()

        path.moveTo(initialX + radius, initialY)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas == null) return

        for (i in 1..sides) {
            val y = radius * sin(angle * i)
            val x = radius * cos(angle * i)

            path.lineTo(initialX + x, initialY + y)
        }

        path.close()
        canvas.drawPath(path, paint)

    }

    fun improveSides(side: Int) {
        this.sides = side
        angle = ((2 * PI) / side).toFloat()
        path.reset()
        path.moveTo(initialX + radius, initialY)
        invalidate()

    }

}