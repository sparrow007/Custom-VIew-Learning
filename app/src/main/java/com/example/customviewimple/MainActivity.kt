package com.example.customviewimple

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearSnapHelper
import com.example.customviewimple.adapter.DataAdapter
import com.example.customviewimple.kotlinLearn.LearnClass
import com.example.customviewimple.layoutManager.StackLayout
import com.example.customviewimple.layoutManager.StackLayoutManager
import com.example.customviewimple.layoutManager.TechLayoutManager
import com.example.customviewimple.layoutManager.source.CoverLayout
import com.example.customviewimple.model.DataModel
import kotlinx.android.synthetic.main.test_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        requestWindowFeature(Window.FEATURE_NO_TITLE)
//        window.setFlags(
//            WindowManager.LayoutParams.FLAG_FULLSCREEN,
//            WindowManager.LayoutParams.FLAG_FULLSCREEN
//        )
        setContentView(R.layout.test_main)

       // LearnClass("Ankit")

        val list = ArrayList<DataModel>()
        list.add(DataModel(R.drawable.image_fifth, "Thi is cool"))
        list.add(DataModel(R.drawable.image_six, "Thi is cool"))
        list.add(DataModel(R.drawable.image_second, "Thi is cool"))
        list.add(DataModel(R.drawable.image_six, "Thi is cool"))
        list.add(DataModel(R.drawable.image_third, "Thi is cool"))
        list.add(DataModel(R.drawable.image_second, "Thi is cool"))

        val adapter = DataAdapter(list)
       // recycler.layoutManager = CoverLayout()
       // val snapHelper = LinearSnapHelper()
        recycler.adapter = adapter
    }

    override fun onResume() {
        super.onResume()


//        Handler().postDelayed( {
//
//            val list = ArrayList<DataModel>()
//            list.add(DataModel(R.drawable.image_third, "Thi is cool"))
//            list.add(DataModel(R.drawable.image_second, "Thi is cool"))
//            list.add(DataModel(R.drawable.image_six, "Thi is cool"))
//            list.add(DataModel(R.drawable.image_fifth, "Thi is cool"))
//            list.add(DataModel(R.drawable.image_third, "Thi is cool"))
//            list.add(DataModel(R.drawable.image_fifth, "Thi is cool"))
//            adapter.updateData(list)
//
//
//        }, 10000)

    }
}
