package com.example.customviewimple

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.customviewimple.adapter.DataAdapter
import com.example.customviewimple.layoutManager.source.CoverLayout
import com.example.customviewimple.model.DataModel
import kotlinx.android.synthetic.main.test_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.test_main)

        val list = ArrayList<DataModel>()
        list.add(DataModel(R.drawable.joker, "Thi is cool"))
        list.add(DataModel(R.drawable.hobes, "Thi is cool"))
        list.add(DataModel(R.drawable.moonlight, "Thi is cool"))
        list.add(DataModel(R.drawable.twlight, "Thi is cool"))
        list.add(DataModel(R.drawable.notebook, "Thi is cool"))
        list.add(DataModel(R.drawable.goingdistance, "Thi is cool"))

        /** Popular list */

        val popList = ArrayList<DataModel>()
        popList.add(DataModel(R.drawable.hacker, "Thi is cool"))
        popList.add(DataModel(R.drawable.thehill, "Thi is cool"))
        popList.add(DataModel(R.drawable.shawshank, "Thi is cool"))
        popList.add(DataModel(R.drawable.replicas, "Thi is cool"))
        popList.add(DataModel(R.drawable.theowner, "Thi is cool"))
        popList.add(DataModel(R.drawable.wolverine, "Thi is cool"))

        val adapter = DataAdapter(list)
        val popAdapter = DataAdapter(popList)

        recycler.adapter = adapter
        recycler.setInfinite(true)
        recycler.setFlat(true)

        pop_recycler.adapter = popAdapter
        pop_recycler.setInfinite(true)
        pop_recycler.setAlpha(true)

        recycler.setItemSelectListener(object : CoverLayout.OnSelected {
            override fun onItemSelected(position: Int) {
               // Toast.makeText(this@MainActivity, "Position $position", Toast.LENGTH_SHORT).show()
            }

        })

    }

}
