package com.example.customviewimple

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.Window
import android.view.WindowManager
import android.view.animation.LinearInterpolator
import androidx.appcompat.app.AppCompatActivity
import com.example.customviewimple.views.path.AnimatedPathView
import com.example.customviewimple.views.path.ObjectFollow
import kotlinx.android.synthetic.main.activity_demo.*

class DemoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        setContentView(R.layout.activity_demo)


//        val observer: ViewTreeObserver = view.getViewTreeObserver()
//        if (observer != null && view is AnimatedPathView) {
//            observer.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
//                override fun onGlobalLayout() {
//
//                    view.getViewTreeObserver().removeGlobalOnLayoutListener(this)
//
//                    view.setPath(floatArrayOf(0f, 0f),
//                        floatArrayOf(view.getWidth().toFloat(), 0f),
//                        floatArrayOf(view.getWidth().toFloat(), view.getHeight().toFloat()),
//                        floatArrayOf(0f, view.getHeight().toFloat()),
//                        floatArrayOf(0f, 0f),
//                        floatArrayOf(view.getWidth().toFloat(), view.getHeight().toFloat()),
//                        floatArrayOf(view.getWidth().toFloat(), 0f),
//                        floatArrayOf(0f, view.getHeight().toFloat()))
//                }
//            })
//        }
//
//        view.setOnClickListener(View.OnClickListener { view ->
//
//        })

    }

    override fun onResume() {
        super.onResume()
       // recycler.animateScale()
       // recycler.startAnimation()


        val view: ObjectFollow = findViewById<View>(R.id.animated_path) as ObjectFollow

        val anim = ObjectAnimator.ofFloat(view, "percentage", 0.0f, 1.0f)
        anim.duration = 3000
        anim.interpolator = LinearInterpolator()
        anim.start()

    }
}