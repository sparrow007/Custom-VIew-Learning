package com.example.customviewimple.waterdrop

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View

class WaterDropView (context: Context, attributeSet: AttributeSet): View(context, attributeSet) {


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

    }

}