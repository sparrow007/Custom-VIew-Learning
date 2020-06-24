package com.example.customviewimple

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View

class WaveTest (context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    val mLowerWavePaint  = Paint()
    private val mUpperWavePaint = Paint()
    private var φ  = 0f
    private val mUpperWavePath = Path()
    private val mLowerWavePath = Path()
   private var onWaveAnimationListener : OnWaveAnimationListener? = null
    init {
        mLowerWavePaint.apply {
            color = Color.WHITE
            style = Paint.Style.FILL
            alpha = 120
        }

        mUpperWavePaint.apply {
            color = Color.WHITE
            style = Paint.Style.FILL
        }
    }

    interface OnWaveAnimationListener {
        fun onWaveAnimate(v : Float)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        mLowerWavePath.reset()
        mUpperWavePath.reset()
        φ -= 0.1f
        val w = 2* Math.PI/ width
        var y : Float
        var y2 = 0f
        mUpperWavePath.moveTo(left.toFloat(), bottom.toFloat())
        mLowerWavePath.moveTo(left.toFloat(), bottom.toFloat())


        var x = 0f

        while(x <= width) {
            y2 = (10 * Math.cos(w * x + φ) + 10).toFloat()
            y = (10 * Math.sin(w * x + φ)).toFloat()
            mUpperWavePath.lineTo(x, y2)
            mLowerWavePath.lineTo(x, y)
            x += 20
        }

        mUpperWavePath.lineTo(right.toFloat(), bottom.toFloat())
        mLowerWavePath.lineTo(right.toFloat(), bottom.toFloat())


        canvas?.drawPath(mUpperWavePath, mUpperWavePaint)
        canvas?.drawPath(mLowerWavePath, mLowerWavePaint)

        postInvalidateDelayed(20)

        onWaveAnimationListener?.onWaveAnimate(y2)

    }

    fun setOnWaveAnimationListener(onWaveAnimationListener: OnWaveAnimationListener) {
        this.onWaveAnimationListener = onWaveAnimationListener
    }


}