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

    private var mCenterX = 0
    private var mCenterY = 0
    private var mRadius = 0f
    init {

        paint.color = Color.RED
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 10f

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
        val angle = 2 * PI / 6
        var radi = mRadius / 6

        for (j in 1..6) {

            for (i in 0..5) {

                if (i == 0) {
                    path.moveTo(mCenterX.toFloat() + radi, mCenterY.toFloat())
                }else {
                    val x: Float = (radi * cos(angle * i)).toFloat()
                    val y = (radi * sin(angle * i)).toFloat()
                    path.lineTo(mCenterX + x, mCenterY + y)
                }
            }
            path.close()
            canvas.drawPath(path, paint)

            radi *= 2
        }
    }


}