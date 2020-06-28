package com.example.customviewimple

import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.customviewimple.adapter.DataAdapter
import com.example.customviewimple.layoutManager.CustomLayout
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
    }

    override fun onResume() {
        super.onResume()

        val list = ArrayList<DataModel>()
        list.add(DataModel(1, "Thi is cool"))
        list.add(DataModel(2, "Thi is cool"))
        list.add(DataModel(2, "Thi is cool"))
        list.add(DataModel(2, "Thi is cool"))
        list.add(DataModel(2, "Thi is cool"))
        list.add(DataModel(2, "Thi is cool"))
        list.add(DataModel(2, "Thi is cool"))
        list.add(DataModel(2, "Thi is cool"))
        list.add(DataModel(2, "Thi is cool"))
        list.add(DataModel(2, "Thi is cool"))
        list.add(DataModel(2, "Thi is cool"))
        list.add(DataModel(2, "Thi is cool"))
        list.add(DataModel(2, "Thi is cool"))
        list.add(DataModel(2, "Thi is cool"))
        list.add(DataModel(2, "Thi is cool"))
        list.add(DataModel(2, "Thi is cool"))
        list.add(DataModel(2, "Thi is cool"))
        list.add(DataModel(2, "Thi is cool"))
        list.add(DataModel(2, "Thi is cool"))
        list.add(DataModel(2, "Thi is cool"))
        list.add(DataModel(2, "Thi is cool"))
        list.add(DataModel(2, "Thi is cool"))
        list.add(DataModel(2, "Thi is cool"))
        list.add(DataModel(2, "Thi is cool"))
        list.add(DataModel(2, "Thi is cool"))
        list.add(DataModel(2, "Thi is cool"))
        list.add(DataModel(2, "Thi is cool"))
        list.add(DataModel(2, "Thi is cool"))
        list.add(DataModel(2, "Thi is cool"))
        list.add(DataModel(2, "Thi is cool"))
        list.add(DataModel(2, "Thi is cool"))
        list.add(DataModel(2, "Thi is cool"))
        list.add(DataModel(2, "Thi is cool"))
        list.add(DataModel(2, "Thi is cool"))

       val adapter = DataAdapter(list)
        recycler.layoutManager = CustomLayout()
        recycler.adapter = adapter

    }
}
