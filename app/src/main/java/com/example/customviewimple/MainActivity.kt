package com.example.customviewimple

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.customviewimple.adapter.DataAdapter
import com.example.customviewimple.layoutManager.source.CarouselLayoutManager
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
        //Get the instance of layout manager from recyclerview
        val customLayoutManager = recycler.getCoverLayout()

        /**
         * Start listening for the clicked position
         */
        adapter.setOnItemSelectListener(object : DataAdapter.OnItemListener {
            /**
             * @param pos It's a position where user would have clicked
             *
             */
            override fun onItemSelect(pos: Int) {
                //Scroll to that position where user clicked
                customLayoutManager.scrollToPosition(pos)
            }
        })

        pop_recycler.adapter = popAdapter
        pop_recycler.setInfinite(true)
        pop_recycler.setAlpha(true)


        recycler.setItemSelectListener(object : CarouselLayoutManager.OnSelected {
            override fun onItemSelected(position: Int) {
               // Toast.makeText(this@MainActivity, "Position $position", Toast.LENGTH_SHORT).show()
            }

        })

        val popCustomLayout = pop_recycler.getCarouselLayoutManager()
        popAdapter.setOnItemSelectListener(object : DataAdapter.OnItemListener {
            override fun onItemSelect(pos: Int) {
                popCustomLayout.scrollToPosition(pos)
            }
        })


    }

}
