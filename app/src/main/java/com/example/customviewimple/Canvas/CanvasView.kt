package com.example.customviewimple.Canvas

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.example.customviewimple.R

public class CanvasView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private val mPaint = Paint()
    private var bitmap:Bitmap? = null

    init {
        mPaint.apply {
            color = Color.BLACK
            style = Paint.Style.FILL
            strokeWidth = 15f
            textSize = 35f

        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        bitmap = BitmapFactory.decodeResource(context.resources, R.raw.anime)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        /*canvas?.translate(width/2f, height/2f)

        val rect = RectF(0f,-400f, 400f, 0f)
        canvas?.drawRect(rect, mPaint)

        //Change the scaling of the canvas

        canvas?.scale(-0.5f, -0.5f, 170f,0f)
        mPaint.color = Color.GREEN
        canvas?.drawRect(rect, mPaint) */

        canvas?.translate(width/2f, height/2f)

        drawText(canvas)

    }

    private fun drawInnerScaleLoop(canvas: Canvas?) {
        canvas?.translate(width/2f, height/2f)
        val rectF = RectF(-200f, -200f, 200f, 200f)
        for(i in 0 until 11) {
            canvas?.scale(0.85f, 0.85f)
            canvas?.drawRect(rectF, mPaint)
        }
    }

    private fun drawText(canvas: Canvas?) {
        val str = "SLOOP"

        canvas!!.drawPosText(
            str,
            floatArrayOf(100f, 100f, 200f, 200f, 300f, 300f, 400f, 400f, 500f, 500f),
            mPaint
        )
    }

    private fun drawSrcBitmap(canvas: Canvas?) {

// 指定图片绘制区域(左上角的四分之一)

// 指定图片绘制区域(左上角的四分之一)
        val src = Rect(0, 0, bitmap!!.width , bitmap!!.height )

// 指定图片在屏幕上显示的区域

// 指定图片在屏幕上显示的区域
        val dst = Rect(0, 0, 200, 400)

// 绘制图片

// 绘制图片
        canvas?.drawBitmap(bitmap!!, src, dst, null)
    }
}