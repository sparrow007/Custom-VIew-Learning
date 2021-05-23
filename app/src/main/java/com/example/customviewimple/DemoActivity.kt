package com.example.customviewimple

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.view.animation.LinearInterpolator
import androidx.appcompat.app.AppCompatActivity
import com.example.customviewimple.views.cupview.CupView
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

        val view = findViewById<CupView>(R.id.animated_path)

        val bottleAnim = ObjectAnimator.ofFloat(view, "bottleProgress", 0.0f, 1.0f).apply {
            duration = 3000
            interpolator = LinearInterpolator()
        }

        bottleAnim.addUpdateListener {
            view.bodyActive = true
        }

        val strawAnim = ObjectAnimator.ofFloat(view, "strawProgress", 0.0f, 1.0f).apply {
            duration = 5000
            interpolator = LinearInterpolator()
        }
        strawAnim.addUpdateListener {
            view.strawActive = true
        }

        val top2Progress = ObjectAnimator.ofFloat(view, "top2Progress", 0.0f, 1.0f).apply {
            duration = 3000
            interpolator = LinearInterpolator()
        }
        top2Progress.addUpdateListener {
            val data = it.animatedValue as Float
            view.top2BarActive = true
        }

        val top1Progress = ObjectAnimator.ofFloat(view, "top1Progress", 0.0f, 1.0f).apply {
            duration = 3000
            interpolator = LinearInterpolator()
        }

        top1Progress.addUpdateListener {
            view.top1BarActive = true
        }

        val anim = AnimatorSet()
        anim.play(top2Progress).before(top1Progress).after(bottleAnim).before(strawAnim)
        anim.start()
        anim.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
               view.waveAnimation = true
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationRepeat(animation: Animator?) {
            }

        })
    }
}