package com.example.customviewimple

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.SeekBar
import com.example.customviewimple.kotlinLearn.LearnClass
import com.jackandphantom.carouselrecyclerview.CarouselRecyclerview
import kotlinx.android.synthetic.main.activity_demo.*

class DemoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_demo)

    }

    override fun onResume() {
        super.onResume()
       // recycler.animateScale()
        recycler.startAnimation()

    }
}