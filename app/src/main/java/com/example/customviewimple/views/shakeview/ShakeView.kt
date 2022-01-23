package com.example.customviewimple.views.shakeview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class ShakeView (context: Context, attributeSet: AttributeSet): View(context, attributeSet) {

    private val paint = Paint().apply {
        color = Color.BLUE
        style = Paint.Style.FILL
    }

    private var centerX = 0f
    private var centerY = 0f
    private var radius = 40f

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
    }
}