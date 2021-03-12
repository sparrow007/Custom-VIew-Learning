package com.example.customviewimple

import android.os.Bundle
import android.widget.Toast
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
        setContentView(R.layout.test_main)

        val list = ArrayList<DataModel>()
        list.add(DataModel(R.drawable.image_fifth, "Thi is cool"))
        list.add(DataModel(R.drawable.image_six, "Thi is cool"))
        list.add(DataModel(R.drawable.image_second, "Thi is cool"))
        list.add(DataModel(R.drawable.image_six, "Thi is cool"))
        list.add(DataModel(R.drawable.image_third, "Thi is cool"))
        list.add(DataModel(R.drawable.image_second, "Thi is cool"))

        val adapter = DataAdapter(list)

        recycler.adapter = adapter
        recycler.set3DItem()
        recycler.setInfinite()

        nextButton.setOnClickListener {
            recycler.getCoverLayout().smoothScrollToPosition(null, null, 3)
        }

        recycler.setItemSelectListener(object : CoverLayout.OnSelected {
            override fun onItemSelected(position: Int) {
                Toast.makeText(this@MainActivity, "Position $position", Toast.LENGTH_SHORT).show()
            }

        })

    }

}
