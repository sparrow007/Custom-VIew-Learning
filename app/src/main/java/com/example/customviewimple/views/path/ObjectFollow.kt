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

    private val initialMoveX = 350f
    private var initialMoveY = 0f

    private val handPath1 = Path()
    private val handPath2 = Path()
    private val headPath = Path()

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

        //Hand Path 1 draw
        drawHandPath1()
        drawHandPath2()
        drawHeadPath()


        val pathMeasure = PathMeasure(headPath, false)
        val pathMeasure1 = PathMeasure(path, false)
        val pathMeasure2 = PathMeasure(handPath2, false)
        val pathMeasure3 = PathMeasure(handPath1, false)
        pathLength = pathMeasure.length + pathMeasure1.length + pathMeasure2.length + pathMeasure3.length

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if(canvas == null) return
        //canvas.translate(width / 2f, height / 2f)

//        canvas.drawPath(path, paint)
//        canvas.drawPath(handPath1, paint)
//        canvas.drawPath(handPath2, paint)
        canvas.drawPath(headPath, paint)

    }


    fun setPercentage(percentage: Float) {
        this.pathProgress = percentage
        val pathEffect = DashPathEffect(floatArrayOf(pathLength, pathLength), pathLength - pathLength * pathProgress)
        paint.pathEffect = ComposePathEffect(pathEffect, cornerPathEffect)
        invalidate()
    }

    fun drawHandPath1() {

        handPath1.moveTo(initialMoveX - 130f, initialMoveY - 30f)
        handPath1.lineTo(initialMoveX - 130f, initialMoveY + 350)
        handPath1.lineTo(initialMoveX - 230f, initialMoveY + 350)
        handPath1.lineTo(initialMoveX - 230f, initialMoveY -30f)
        handPath1.close()

    }

    fun drawHandPath2() {

        handPath1.moveTo(initialMoveX + 490f, initialMoveY - 30f)
        handPath1.lineTo(initialMoveX + 490f, initialMoveY + 350)
        handPath1.lineTo(initialMoveX + 590f, initialMoveY + 350)
        handPath1.lineTo(initialMoveX + 590f, initialMoveY -30f)
        handPath1.close()

    }

    fun drawHeadPath() {

      //  headPath.moveTo(initialMoveX, initialMoveY - 100f)
        val rectF = RectF(initialMoveX - 90f, initialMoveY - 300f,
            initialMoveX + 450f,
             initialMoveY+ 250f)
        headPath.arcTo(rectF, 0f, -180f)
        headPath.close()

    }

}