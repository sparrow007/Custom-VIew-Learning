package com.example.customviewimple.views.drawable

import android.animation.ObjectAnimator
import android.animation.ValueAnimator.RESTART

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation.INFINITE
import android.view.animation.LinearInterpolator

class PolyViewDraw(context: Context, attributeSet: AttributeSet): View(context, attributeSet) {

    val polygonLapsDrawable = PolygonLapsDrawable()

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas != null) {
            polygonLapsDrawable.draw(canvas)
        }
    }

    fun startAnimation() {
        ObjectAnimator.ofFloat(polygonLapsDrawable, PolygonLapsDrawable.PROGRESS, 0f, 1f).apply {
            duration = 4000L
            interpolator = LinearInterpolator()
            repeatCount = INFINITE
            repeatMode = RESTART
        }.start()
    }

}