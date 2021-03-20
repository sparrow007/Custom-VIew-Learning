package com.example.customviewimple.views

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View

class CircleToHeartView (context: Context, attributeSet: AttributeSet): View(context, attributeSet) {

    

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
    }

}