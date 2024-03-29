package com.example.customviewimple.views.qqmessage

import android.animation.*
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.core.animation.addListener
import kotlin.math.*

class QQMessage (context: Context, attributeSet: AttributeSet): View(context, attributeSet) {

    private val paint = Paint().apply {
        isAntiAlias = true
    }
    private var mBigCircleX = 0f
    private var mBigCircleY = 0f
    private var mBigCircleRadius = 20f

    private var mSmallCircleX = 0f
    private var mSmallCircleY = 0f
    private var mSmallCircleShowRadius = 50f
    private var mSmallCircleHideRadius = 15f
    private var mSmallCircleRadius = mSmallCircleShowRadius

    private var p1x = 0f
    private var p1y = 0f

    private var p2x = 0f
    private var p2y = 0f

    private var p3x = 0f
    private var p3y = 0f

    private var p4x = 0f
    private var p4y = 0f

    private var mControlx = 0f
    private var mControly = 0f
    private var dx = 0f
    private var dy = 0f

    private var mAngle = 0f

    private var mBezierPath:Path? = null

    private var isAttached = false
    private var isFirst = true

    private var radius:Float = 0f

    private lateinit var animatorSet: AnimatorSet
    private lateinit var xAnimator: ValueAnimator
    private lateinit var yAnimator: ValueAnimator

    private fun calculateBezierPath(): Path?{

        if(mSmallCircleRadius < mSmallCircleHideRadius) return null

        dx = mBigCircleX - mSmallCircleX
        dy = mBigCircleY - mSmallCircleY

        val tan = dy / dx
        mAngle = atan(tan)

        mControlx = (mBigCircleX + mSmallCircleX) / 2
        mControly = (mBigCircleY + mSmallCircleY) / 2

        p1x = (mSmallCircleX + sin(mAngle) * mSmallCircleRadius)
        p1y = (mSmallCircleY - cos(mAngle) * mSmallCircleRadius)

        p4x = (mSmallCircleX - sin(mAngle) * mSmallCircleRadius)
        p4y = (mSmallCircleY + cos(mAngle) * mSmallCircleRadius)


        p2x = (mBigCircleX + sin(mAngle) * mBigCircleRadius)
        p2y = (mBigCircleY - cos(mAngle) * mBigCircleRadius)

        p3x = (mBigCircleX - sin(mAngle) * mBigCircleRadius)
        p3y = (mBigCircleY + cos(mAngle) * mBigCircleRadius)

        val bezierPath = Path()
        bezierPath.moveTo(p1x, p1y)
        bezierPath.quadTo(mControlx, mControly, p2x, p2y)

        bezierPath.lineTo(p3x, p3y)
        bezierPath.quadTo(mControlx, mControly, p4x, p4y)
      //  bezierPath.close()

        return bezierPath
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        if (event == null) return false

        val downX = event.x
        val downY = event.y

        when (event.action) {

            MotionEvent.ACTION_DOWN -> {
                mBigCircleRadius = 50f
                isFirst = false
                isAttached = true

                mSmallCircleRadius = mSmallCircleShowRadius
                mBigCircleX = downX
                mBigCircleY = downY
                mSmallCircleX = mBigCircleX
                mSmallCircleY = mBigCircleY
            }

            MotionEvent.ACTION_MOVE -> {
                mBigCircleX = downX
                mBigCircleY = downY
                val distance = calDistance()
                mSmallCircleRadius = mSmallCircleShowRadius - distance / mSmallCircleHideRadius
                if (mSmallCircleRadius < mSmallCircleHideRadius) {
                    isAttached = false
                }

            }
            MotionEvent.ACTION_UP -> {
                if (!isAttached) {
                    val radiusAnimator = ObjectAnimator.ofFloat(mBigCircleRadius, 0f)
                    radiusAnimator.addUpdateListener {
                        mBigCircleRadius = it.animatedValue as Float
                        invalidate()
                    }

                    radiusAnimator.duration = 500
                    radiusAnimator.start()
                } else if (mSmallCircleRadius >= mSmallCircleHideRadius) {
                    animatorSet = AnimatorSet()
                    xAnimator = ObjectAnimator.ofFloat(mBigCircleX, mSmallCircleX)
                    xAnimator.addUpdateListener {
                        mBigCircleX = it.animatedValue as Float
                        val distance = calDistance()
                        mSmallCircleRadius =
                            mSmallCircleShowRadius - distance / mSmallCircleHideRadius
                    }

                    yAnimator = ObjectAnimator.ofFloat(mBigCircleY, mSmallCircleY)
                    yAnimator.addUpdateListener {
                        mBigCircleY = it.animatedValue as Float
                        invalidate()
                    }

                    animatorSet.playTogether(xAnimator, yAnimator)
                    animatorSet.interpolator = OvershootInterpolator(2.5f)
                    animatorSet.duration = 500
                    animatorSet.start()
                    animatorSet.addListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator?) {
                            mSmallCircleRadius = 0f
                        }
                    })
                }

            }
        }

        invalidate()
        return true
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (canvas == null) return

        mBezierPath = calculateBezierPath()

        if (mBezierPath != null) {
            canvas.drawPath(mBezierPath!!, paint)
        }

        if (isAttached) {
            canvas.drawCircle(mSmallCircleX, mSmallCircleY, mSmallCircleRadius, paint)
        }

        //first time then don't draw the circle
        if (isFirst) return

        canvas.drawCircle(mBigCircleX, mBigCircleY, mBigCircleRadius, paint)

    }

    private fun calDistance(): Int {
        return sqrt((mSmallCircleX - mBigCircleX).toDouble().pow(2.toDouble()) + (mSmallCircleY - mBigCircleY).toDouble()
            .pow(2.toDouble())
        ).toInt()
    }
}