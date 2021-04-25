package com.example.customviewimple.views

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator

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
    var radius = 40f
    private var angle:Float = 60f

    private var initialX = 0f
    private var initialY = 0f

    private var theta = -1f

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

        //
        canvas.drawCircle(initialX, initialY, radius, paint)
        canvas.drawCircle(width/2f, height / 2f, 20f, paint)

    }

    fun animateRotate() {

        val animator = ValueAnimator.ofFloat(0f, 100f)
        animator.addUpdateListener {
            val centerX = width / 2f
            val centerY = height / 2f

            initialX = centerX + 300 * cos(theta)
            initialY = centerY + 300 * sin(theta)
            theta += 0.04f
            invalidate()
        }

        animator.repeatCount = ValueAnimator.INFINITE

        animator.interpolator = LinearInterpolator()
        animator.start()

    }

    fun animateScale() {

        val animator = ValueAnimator.ofFloat(1f, -1f)
        animator.addUpdateListener {
            val animateValue = it.animatedValue as Float
            val current = width/2f

            radius = 60 * (1 +1 * sin(animateValue))
            invalidate()
        }
        animator.repeatCount = ValueAnimator.INFINITE
        animator.repeatMode = ValueAnimator.REVERSE
        animator.interpolator = LinearInterpolator()
        animator.duration = 1000
        animator.start()

    }

     fun animateTransition() {

        val animator = ValueAnimator.ofFloat(-1f, 1f)
        animator.addUpdateListener {
            val animateValue = it.animatedValue as Float
            val current = width/2f
            initialX = current + 400 * sin(animateValue)
            invalidate()
        }
        animator.repeatCount = ValueAnimator.INFINITE
        animator.repeatMode = ValueAnimator.REVERSE
         animator.interpolator = LinearInterpolator()
        animator.duration = 1000
        animator.start()

    }

    private fun drawPolygons(canvas: Canvas) {
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