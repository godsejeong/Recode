package com.sttproject

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

class RecodeRecylcerAdapter(items : ArrayList<RecodeListData>) : RecyclerView.Adapter<RecodeRecylcerAdapter.ViewHolder>() {
    var items: ArrayList<RecodeListData> = ArrayList()

    init {
        this.items = items
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent!!.context)
                .inflate(R.layout.recode_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var data = items[position]
        holder.title.text = data.title
        holder.contnet.text = data.content
        holder.img.setImageResource(data.img)
    }



    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title : TextView = itemView.findViewById(R.id.recode_title)
        var contnet : TextView = itemView.findViewById(R.id.recode_content)
        var img : ImageView = itemView.findViewById(R.id.recode_img)
    }

}