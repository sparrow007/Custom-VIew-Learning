package com.example.customviewimple.views.path

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class ObjectFollow(context: Context, attributeSet: AttributeSet): View(context, attributeSet) {

    val path = Path()
    val paint = Paint().apply {
        color = Color.GREEN
        style = Paint.Style.STROKE
        strokeWidth = 10f
        pathEffect = CornerPathEffect(50f)
    }

    private var pathLength = 0f
    private var pathProgress = 0f

    private val initialMoveX = 300f
    private var initialMoveY = 0f

    private val cornerPathEffect = CornerPathEffect(50f)

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        initialMoveY = height / 2f


        path.moveTo(initialMoveX, initialMoveY)
        path.lineTo(initialMoveX + 450f ,height/2f)
        path.lineTo(initialMoveX + 450f, height/2f+ 400f)

        path.lineTo(initialMoveX + 350f, initialMoveY + 400f)
        path.lineTo(initialMoveX + 350f, initialMoveY + 700f)
        path.lineTo(initialMoveX + 250f, initialMoveY + 700f)
        path.lineTo(initialMoveX + 250f, initialMoveY + 400f)
        path.lineTo(initialMoveX + 120f, initialMoveY + 400f)

        path.lineTo(initialMoveX + 120f, initialMoveY + 700f)
        path.lineTo(initialMoveX + 20f, initialMoveY + 700f)
        path.lineTo(initialMoveX + 20f, initialMoveY + 400f)


        path.lineTo(initialMoveX - 80f, initialMoveY + 400f)
        path.lineTo(initialMoveX - 80f, initialMoveY)
        path.lineTo(initialMoveX , initialMoveY)



//        path.lineTo(initialMoveX, height/2f+400f)
//        path.lineTo(initialMoveX, height/2f)



        val pathMeasure = PathMeasure(path, false)
        pathLength = pathMeasure.length

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if(canvas == null) return
        //canvas.translate(width / 2f, height / 2f)

        canvas.drawPath(path, paint)

    }


    fun setPercentage(percentage: Float) {
        this.pathProgress = percentage
        val pathEffect = DashPathEffect(floatArrayOf(pathLength, pathLength), pathLength - pathLength * pathProgress)
        paint.pathEffect = ComposePathEffect(pathEffect, cornerPathEffect)
        invalidate()
    }

}