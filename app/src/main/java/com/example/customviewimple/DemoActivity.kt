package com.example.customviewimple

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.Window
import android.view.WindowManager
import android.view.animation.AccelerateInterpolator
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.LinearInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.addListener
import com.example.customviewimple.views.path.AnimatedPathView
import com.example.customviewimple.views.path.ObjectFollow
import kotlinx.android.synthetic.main.activity_demo.*

class DemoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        window.addFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_demo)

    }

    override fun onResume() {
        super.onResume()
        //animated_path.animation()
    }

//    override fun onResume() {
//        super.onResume()
//
//        val view: ObjectFollow = findViewById<View>(R.id.animated_path) as ObjectFollow
//
//        val headAnim = ObjectAnimator.ofFloat(view, "headPercentage", 0.0f, 1.0f).apply {
//            duration = 3000
//            interpolator = LinearInterpolator()
//        }
//
//        val bodyAnim = ObjectAnimator.ofFloat(view, "percentage", 0.0f, 1.0f).apply {
//            duration = 5000
//            interpolator = LinearInterpolator()
//        }
//
//        val hand1Anim = ObjectAnimator.ofFloat(view, "hand1Percentage", 0.0f, 1.0f).apply {
//            duration = 3000
//            interpolator = LinearInterpolator()
//        }
//        hand1Anim.addUpdateListener {
//            val data = it.animatedValue as Float
//            view.hand1Active = true
//        }
//
//        val hand2Anim = ObjectAnimator.ofFloat(view, "hand2Percentage", 0.0f, 1.0f).apply {
//            duration = 3000
//            interpolator = LinearInterpolator()
//        }
//
//        hand2Anim.addUpdateListener {
//            view.hand2Active = true
//        }
//
//        val eyeAnim = ObjectAnimator.ofFloat(view, "eyePercentage", 0.0f, 1.0f).apply {
//            duration = 3000
//            interpolator = LinearInterpolator()
//        }
//        eyeAnim.addUpdateListener {
//            view.headActive = true
//        }
//
//        val anim = AnimatorSet()
//        anim.play(hand1Anim).before(headAnim).before(eyeAnim).with(hand2Anim).after(bodyAnim)
//        anim.start()
//        anim.addListener(object : Animator.AnimatorListener {
//            override fun onAnimationStart(animation: Animator?) {
//            }
//
//            override fun onAnimationEnd(animation: Animator?) {
//               view.ioActive = true
//            }
//
//            override fun onAnimationCancel(animation: Animator?) {
//            }
//
//            override fun onAnimationRepeat(animation: Animator?) {
//            }
//
//        })
//    }
}