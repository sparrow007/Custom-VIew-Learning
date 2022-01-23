package com.example.customviewimple.GuoweiLv

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class Timer (context: Context, attrs: AttributeSet) : View(context, attrs) {

    /**
     * Measure -> First child will measure it's size so that it can tell the parent about the
     * how much big it going to be so that parent can easily understand and make that much space
     *
     * Layout --> After the measure part is done parent will position all of it's children and then
     * draw itself and request child to do it same
     *
     * paint -> In order to some stuff we the paint object
     *
     */

    val paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        strokeWidth = 10f
    }



    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
    }


}