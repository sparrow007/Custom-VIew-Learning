package com.example.customviewimple.views.qqmessage

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class QQMessage (context: Context, attributeSet: AttributeSet): View(context, attributeSet) {

    private val paint = Paint()
    private var mBigCircleX = 0f
    private var mBigCircleY = 0f
    private var mBigCircleRadius = 50f

    private var mSmallCircleX = 0f
    private var mSmallCircleY = 0f
    private var mSmallCircleRadius = 0f

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
    }

    
}