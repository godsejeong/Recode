package com.sttproject

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.sdk25.coroutines.onLongClick
import org.jetbrains.anko.startActivity
import android.support.v4.content.ContextCompat.startActivity
import android.support.v4.view.accessibility.AccessibilityEventCompat.setAction





class RecordRecylcerAdapter(items : ArrayList<RecordListData>,context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var items: ArrayList<RecordListData> = ArrayList()
    var contexts : Context? = null
    init {
        this.items = items
        this.contexts = context
    }

    override fun getItemViewType(position: Int): Int {
        if (items[position].viewtype == 0) {
            return 0
        } else {
            return 1
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view: View? = null
        return when {
            viewType === 0 -> {
                view = LayoutInflater.from(parent!!.context)
                        .inflate(R.layout.record_item, parent, false)
                Holder(view)
            }
            viewType === 1 -> {
                view = LayoutInflater.from(parent!!.context)
                        .inflate(R.layout.record_date_item, parent, false)
                DateHolder(view)
            }
            else -> Holder(view!!)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var data = items[position]
        if(holder is Holder){
        holder.title.text = data.title
        holder.contnet.text = data.content
        Glide.with(holder.contnet).load(Uri.parse(data.img)).into(holder.img)

        holder.itemView.onLongClick {
            //공유
            var i = Intent(Intent.ACTION_SEND)
            i.action = android.content.Intent.ACTION_VIEW
            i.setDataAndType(Uri.parse(data.path), "audio/*")

            contexts!!.startActivity(Intent.createChooser(i,"공유"))
        }

        holder.itemView.onClick {

            contexts?.startActivity<WriteRecode>(
                    "path" to data.path,
                    "title" to data.title,
                    "content" to data.content)
        }


        }else{
            (holder as DateHolder)
            holder.date.text = data.date
        }
    }

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title : TextView = itemView.findViewById(R.id.record_title)
        var contnet : TextView = itemView.findViewById(R.id.record_content)
        var img : ImageView = itemView.findViewById(R.id.record_img)
    }

    class DateHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var date : TextView = itemView.findViewById(R.id.recordDateText) as TextView

    }
}