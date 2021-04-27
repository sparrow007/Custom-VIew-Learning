package com.example.customviewimple.views.patterns

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout

class ShapeContainer(context: Context, attributeSet: AttributeSet): FrameLayout(context, attributeSet) {

    private val patternList = arrayListOf<Animator>()

    val delayTime = 500
    private val animatorSet = AnimatorSet()

   init {
       animatorSet.interpolator = LinearInterpolator()
       var distance=150f
       for (i in 1..4) {
           val view = CircularPattern(context, attributeSet)
           view.increaseDistance(distance)
           addView(view)
           //patternList.add(view.animateScale(500L-(10 * i)))
           distance += 60f

           val scaleXAnimator = ObjectAnimator.ofFloat(view, "ScaleX", 0f, 1f);
           scaleXAnimator.setRepeatCount(ObjectAnimator.INFINITE);
           scaleXAnimator.setRepeatMode(ObjectAnimator.REVERSE);
           scaleXAnimator.setStartDelay(i * 500L);
           scaleXAnimator.setDuration(3000L);
           patternList.add(scaleXAnimator);
           val scaleYAnimator = ObjectAnimator.ofFloat(view, "ScaleY", 0F,1f);
           scaleYAnimator.setRepeatCount(ObjectAnimator.INFINITE);
           scaleYAnimator.setRepeatMode(ObjectAnimator.REVERSE);
           scaleYAnimator.setStartDelay(i * 500L);
           scaleYAnimator.setDuration(3000L);
           patternList.add(scaleYAnimator);

       }
      // animatorSet.playTogether(patternList)
       animatorSet.playTogether(patternList)
       animatorSet.start()

   }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

    }

}