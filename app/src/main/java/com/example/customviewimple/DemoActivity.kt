package com.example.customviewimple

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jackandphantom.carouselrecyclerview.CarouselRecyclerview
import kotlinx.android.synthetic.main.activity_demo.*

class DemoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo)

        button.setOnClickListener {
            recycler.animateCircle()
        }


    }
}