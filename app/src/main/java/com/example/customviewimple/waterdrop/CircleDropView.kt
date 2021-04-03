package com.example.customviewimple.waterdrop

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class CircleDropView (context: Context, attributeSet: AttributeSet): View(context, attributeSet) {

    private val radius = 50f
    private var mCenterX = 0f
    private var mCenterY = 0f
    private val paint = Paint().apply {
        color = Color.BLACK
        style = Paint.Style.FILL
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mCenterX = w / 2f
        mCenterY = h / 2f
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (canvas == null) return

        canvas.drawCircle(mCenterX, mCenterY, radius, paint)
    }

}