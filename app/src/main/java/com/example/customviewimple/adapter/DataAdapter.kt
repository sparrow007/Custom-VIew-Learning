package com.example.customviewimple.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.customviewimple.R
import com.example.customviewimple.model.DataModel

class DataAdapter (private val list : List<DataModel>): RecyclerView.Adapter<DataAdapter.ViewHolder>() {

     class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
         val textView :  TextView
         init {
             textView =  itemView.findViewById(R.id.text)
         }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
         val inflater = LayoutInflater.from(parent.context).inflate(R.layout.item_view, parent,false)
         return ViewHolder(inflater)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = list.get(position).text
    }
}