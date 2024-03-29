package com.example.customviewimple.waterdrop

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.*

class CircleDropView (context: Context, attributeSet: AttributeSet): View(context, attributeSet) {

    private val radius = 20f
    private var mCenterX = 0f
    private var mCenterY = 0f
    private val paint = Paint().apply {
        color = Color.BLACK
        style = Paint.Style.FILL
    }

    private val path = Path()
    private val start: PointF = PointF(0f,0f)
    private val end: PointF = PointF(0f, 0f)
    private val control: PointF = PointF(0f, 0f)

    private lateinit var rectF: RectF

    private var lastCenter = 0f

    private lateinit var animator: AnimatorSet

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mCenterX = w / 2f
        mCenterY = h / 2f
        lastCenter = mCenterY

        start.x = mCenterX - 33f
        start.y = mCenterY

        end.x = mCenterX + 33f
        end.y = mCenterY

        control.x = mCenterX
        control.y = mCenterY

        rectF = RectF(0f, h/2f - radius, w - 40f, h/2f+200)
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (canvas == null) return

       // canvas.drawCircle(mCenterX, mCenterY, radius, paint)
       // canvas.drawRect(rectF, paint)
        path.reset()
        path.moveTo(start.x, start.y)
        path.quadTo(control.x, control.y, end.x, end.y)
        path.close()
        canvas.drawPath(path, paint)

       // Log.e("MY TAG", "CENTER Y " + mCenterY)
    }

     fun animateCircle() {

       val valueAnimator = ValueAnimator.ofFloat(0f, 120f)
        valueAnimator.interpolator = AccelerateDecelerateInterpolator()

        valueAnimator.addUpdateListener {
            val value = it.animatedValue as Float
            Log.e("MY TAG", "animate value" + value)

            mCenterY = lastCenter - value
            invalidate()
        }

         valueAnimator.addListener(object : Animator.AnimatorListener {
             override fun onAnimationStart(animation: Animator?) {

             }

             override fun onAnimationEnd(animation: Animator?) {
                 lastCenter = mCenterY
             }

             override fun onAnimationCancel(animation: Animator?) {

             }

             override fun onAnimationRepeat(animation: Animator?) {

             }

         })

         val downAnimator = ValueAnimator.ofFloat(0f, 70f)
         downAnimator.interpolator = LinearInterpolator()
         downAnimator.addUpdateListener {
             val value = it.animatedValue as Float
             Log.e("MY TAG", "animate value" + value)

             mCenterY = lastCenter + value

             invalidate()
         }

            downAnimator.addListener(object : Animator.AnimatorListener {
                 override fun onAnimationStart(animation: Animator?) {

                 }

                 override fun onAnimationEnd(animation: Animator?) {
                     mCenterY = height / 2f
                     lastCenter = mCenterY
                 }

                 override fun onAnimationCancel(animation: Animator?) {

                 }

                 override fun onAnimationRepeat(animation: Animator?) {

                 }

             })

         val bezierAnimator = ValueAnimator.ofFloat(0f,120f)
         bezierAnimator.interpolator = LinearInterpolator()
         bezierAnimator.duration = 500
         bezierAnimator.addUpdateListener {
             val value = it.animatedValue as Float
             if (value > 60)
                 control.y = (mCenterY - 60)+ (value - 60)
             else
                 control.y = mCenterY - value

             control.x = mCenterX
             invalidate()
         }
         bezierAnimator.start()

//        animator = AnimatorSet()
//         animator.play(bezierAnimator).after(valueAnimator).before(downAnimator)
//       // animator.playTogether(valueAnimator,bezierAnimator)
//        animator.start()
    }

}