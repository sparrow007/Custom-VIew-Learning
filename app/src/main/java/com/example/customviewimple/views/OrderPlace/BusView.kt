package com.example.customviewimple.views.OrderPlace

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class BusView (context: Context, attributeSet: AttributeSet): View(context, attributeSet) {

    private val paint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = 10f
        color = Color.GREEN
    }

    private val backPaint = Paint().apply {
        style = Paint.Style.FILL
        color = Color.BLACK
    }

    private val frontPaint = Paint().apply {
        style = Paint.Style.FILL
        color = Color.parseColor("#3a5af9")
    }

    private val lightPaint = Paint().apply {
        style = Paint.Style.FILL
        color = Color.YELLOW
    }

    private val path = Path()
    private val frontPath = Path()
    private val lightPath = Path()

    private var initialGate1X = 200f
    private var initialGate1Y = 200f
    private var endGate1X = 150f
    private var endGate1Y = 250f

    private var initialGate2X = 160f
    private var initialGate2Y = 250f
    private var endGate2X = 200f
    private var endGate2Y = 300f

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val rectF = RectF(200f, 200f, 500f, 390f)
        path.addRoundRect(rectF, 15f, 15f, Path.Direction.CW)

        val frontRectF = RectF(410f, 200f, 510f, 300f)
        frontPath.addRoundRect(frontRectF, floatArrayOf(0f,0f,30f,30f, 30f, 30f, 0f,0f), Path.Direction.CW)

        val light1RectF =  RectF(500f, 205f, 510f, 230f)
        val light2RectF =  RectF(500f, 260f, 510f, 285f)
        lightPath.addRoundRect(light1RectF, 20f,20f, Path.Direction.CW)
        lightPath.addRoundRect(light2RectF, 20f,20f, Path.Direction.CW)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas == null) return

        canvas.drawPath(path, backPaint)
        canvas.drawPath(frontPath, frontPaint)
        canvas.drawPath(lightPath, lightPaint)
        canvas.drawLine(initialGate1X, initialGate1Y,  endGate1X, endGate1Y, paint)
        canvas.drawLine(initialGate2X, initialGate2Y,  endGate2X, endGate2Y, paint)
    }

}