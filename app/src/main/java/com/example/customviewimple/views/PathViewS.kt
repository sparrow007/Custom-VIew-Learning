package com.example.customviewimple.views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.annotation.MainThread
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

class PathViewS(context: Context, attributeSet: AttributeSet) : View(context,attributeSet) {

    private val paint = Paint()
    private val path = Path()
    private val valuePaint = Paint()

    private var mCenterX = 0
    private var mCenterY = 0
    private var mRadius = 0f

    private var titles = arrayListOf("a", "b", "c", "d", "e", "f")
    private var angle:Float = 0f

    private val data = arrayListOf(100, 60, 60, 60, 100, 50)
    private val maxValue = 100

    init {

        paint.color = Color.RED
        paint.style = Paint.Style.STROKE
        paint.textSize = 20f

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mCenterX = w / 2
        mCenterY = h / 2
        mRadius = min(h, w) / 2*0.9f
       // path.moveTo(mCenterX.toFloat() + mRadius, mCenterY.toFloat())
    }

    /**
     * Draw a simple hexagon
     */

    override fun onDraw(canvas: Canvas?) {

        if(canvas == null) return

        //Start from center fo the screen
        angle = (2 * PI / 6).toFloat()
        val radi = mRadius / 5

        for (j in 1..5) {

            val currRadi = radi * j
            path.reset()

            for (i in 0..5) {

                if (i == 0) {
                    path.moveTo(mCenterX.toFloat() + currRadi, mCenterY.toFloat())
                }else {
                    val x: Float = (currRadi * cos(angle * i)).toFloat()
                    val y = (currRadi * sin(angle * i)).toFloat()
                    path.lineTo(mCenterX + x, mCenterY + y)
                }
            }
            path.close()
            canvas.drawPath(path, paint)

        }
        drawLines(canvas)
        drawText(canvas)
        drawRegion(canvas)

    }
    /**
     * Draw the straight lines
     */
    private fun drawLines(canvas: Canvas) {
        //path.reset()

        for (i in 1..6) {
            path.moveTo(mCenterX.toFloat() , mCenterY.toFloat())

            path.lineTo(mCenterX + (mRadius * cos(angle * i)).toFloat()
                ,mCenterY + (mRadius* sin(angle * i)).toFloat() )
        }

        canvas.drawPath(path, paint)
    }

    private fun drawText(canvas: Canvas) {

//        path.reset()
//
//        path.moveTo(mCenterX.toFloat() + mRadius, mCenterY.toFloat())

        val fontMetric = paint.fontMetrics

        val fonHeight = fontMetric.descent - fontMetric.ascent

        paint.color = Color.BLACK

        for (i in 0..5) {

            val x = mCenterX + (mRadius + fonHeight/2) * cos(angle * i)
            val y = mCenterY + (mRadius + fonHeight/2) * sin(angle * i)

            canvas.drawText(titles[i], x, y, paint)

        }

    }

    private fun drawRegion(canvas: Canvas) {

        path.reset()

        valuePaint.color = Color.BLUE
        for (i in 0..5) {
            val percent = data[i] / maxValue.toFloat()
            val x = mCenterX + mRadius * cos(angle * i) * percent
            val y = mCenterY + mRadius * sin(angle * i) * percent

            if (i == 0) {
                path.moveTo(x, mCenterY.toFloat())
            }else
                path.lineTo(x, y)

            canvas.drawCircle(x, y ,10f, valuePaint)

        }

        valuePaint.style = Paint.Style.FILL_AND_STROKE
        valuePaint.alpha = 100
        canvas.drawPath(path, valuePaint)

    }


}