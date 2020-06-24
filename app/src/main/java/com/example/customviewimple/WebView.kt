package com.example.customviewimple

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View

class WebView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private val mPaint = Paint()

    private var mWidth : Float = 0f
    private var mHeight : Float = 0f

    private var initialRadius : Float = 0f
    private val sidesCount = 6

    private var mCenterX : Float = 0f
    private var mCenterY : Float = 0f

    private val mAngle = ((Math.PI * 2.0f)/sidesCount)

    private val data = arrayOf(100, 30, 40, 60, 100, 50, 10, 20)
    private val maxValue = 100f

    private val valuePaint = Paint()

    private val webPath = Path()

    init {
        mPaint.apply {
            color = Color.LTGRAY
            style = Paint.Style.STROKE
            strokeWidth = 10f
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w.toFloat()
        mHeight = h.toFloat()

        mCenterX = mWidth / 2f
        mCenterY = mHeight / 2f

        initialRadius = Math.min(h.toFloat() , w.toFloat()) / 2 * 0.9f
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)


        val radiusGap = initialRadius / (sidesCount-1)

        for(i in 1 until sidesCount) {
            val radius  = radiusGap * i

            webPath.reset()
            for(j in 0 until sidesCount) {
                val x = mCenterX + (radius * Math.cos(mAngle * j))
                val y = mCenterY + (radius * Math.sin(mAngle * j))
                if(j == 0) {
                    webPath.moveTo(x.toFloat(), y.toFloat())
                }else{
                    webPath.lineTo(x.toFloat(), y.toFloat())
                }
            }
            webPath.close()
            canvas?.drawPath(webPath, mPaint)
        }

        drawLines(canvas)

        drawRegion(canvas)

    }

    private fun drawLines(canvas: Canvas?) {
        val path = Path()
        for (i in 0 until sidesCount) {

            path.moveTo(mCenterX, mCenterY)
            val x = mCenterX + initialRadius * Math.cos(mAngle * i)
            val y = mCenterY + initialRadius * Math.sin(mAngle * i)

            path.lineTo(x.toFloat(), y.toFloat())
            canvas?.drawPath(path, mPaint)
        }

    }

    private fun drawRegion(canvas: Canvas?) {

        val regionPath = Path()
        valuePaint.alpha = 255
        valuePaint.color = Color.BLUE

        for(i in 0 until sidesCount) {
            val percent = data[i]/maxValue
            val x = mCenterX + (initialRadius * Math.cos(mAngle*i)*percent)
            val y = mCenterY + (initialRadius * Math.sin(mAngle*i)*percent)

            if(i == 0) {
                regionPath.moveTo(x.toFloat(), y.toFloat())
            }else {
                regionPath.lineTo(x.toFloat(), y.toFloat())
            }
            valuePaint.style = Paint.Style.FILL
            canvas?.drawCircle(x.toFloat(), y.toFloat() , 10f, valuePaint)

        }
        valuePaint.alpha = 127
        valuePaint.style = Paint.Style.FILL_AND_STROKE
        canvas?.drawPath(regionPath, valuePaint)

    }

}
