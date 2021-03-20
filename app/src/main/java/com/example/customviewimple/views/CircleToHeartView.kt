package com.example.customviewimple.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View

class CircleToHeartView (context: Context, attributeSet: AttributeSet): View(context, attributeSet) {

    private val c = 0.5519f
    private val paint = Paint().apply {
        color = Color.RED
        strokeWidth = 8f
        style = Paint.Style.STROKE
    }
    private val path = Path()

    private var mCenterX = 0f
    private var mCenterY = 0f
    private val mCircleRadius = 200f
    private val mDifference = mCircleRadius * c

    private val mData = FloatArray(8)
    private val mControl = FloatArray(16)

    private val duration: Float = 0f
    private val current = 0f
    private val count = 100f
    private val piece = duration / count

    init {

        //Initialize data points
        mData[0] = 0f
        mData[1] = mCircleRadius

        mData[2] = mCircleRadius
        mData[3] = 0f

        mData[4] = 0f
        mData[5] = -mCircleRadius

        mData[6] = -mCircleRadius
        mData[7] = 0f

        //Initialize the control points
        mControl[0] = mData[0] + mDifference
        mControl[1] = mData[1]

        mControl[2] = mData[2]
        mControl[3] = mData[3] + mDifference

        mControl[4] = mData[2]
        mControl[5] = mData[3] - mDifference

        mControl[6] = mData[4] + mDifference
        mControl[7] = mData[5]

        mControl[8] = mData[4] - mDifference
        mControl[9] = mData[5]

        mControl[10] = mData[6]
        mControl[11] = mData[7] - mDifference

        mControl[12] = mData[6]
        mControl[13] = mData[3] + mDifference

        mControl[14] = mData[0] - mDifference
        mControl[15] = mData[1]

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mCenterX = w / 2f
        mCenterY = h / 2f
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas == null) return
        drawCoordinateSystem(canvas)

    }

    //Draw the coordinate system
    private fun drawCoordinateSystem(canvas: Canvas) {
        canvas.save()
        canvas.translate(mCenterX, mCenterY)
        canvas.scale(1f, -1f)

        val axisPaint = Paint().apply {
            color = Color.BLACK
            style = Paint.Style.STROKE
            strokeWidth = 8f
        }

        canvas.drawLine(0f, -2000f, 0f, 2000f, axisPaint)
        canvas.drawLine(-2000f, 0f, 2000f, 0f, axisPaint)

        canvas.restore()
    }


}