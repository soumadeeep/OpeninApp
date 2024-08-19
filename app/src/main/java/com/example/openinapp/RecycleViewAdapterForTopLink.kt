package com.example.openinapp

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.openinapp.dataclass.MainData
import com.example.openinapp.dataclass.TopLink
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class RecycleViewAdapterForTopLink(private var itemList: List<TopLink>) :
    RecyclerView.Adapter<RecycleViewAdapterForTopLink.ItemViewHolder>() {
    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val linkName: TextView = itemView.findViewById(R.id.total_link_name)
        val linkCount: TextView = itemView.findViewById(R.id.total_link_count)
        val time: TextView = itemView.findViewById(R.id.total_link_time)
        val link: TextView = itemView.findViewById(R.id.top_link_cp)
        val image:ImageView=itemView.findViewById(R.id.image_for_total_click)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.model_for_top_click, parent, false)
        return ItemViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(
        holder: ItemViewHolder,
        position: Int
    ) {
        val item = itemList[position]
        holder.linkName.text = item.title
        holder.linkCount.text = item.total_clicks.toString()
        holder.time.text = main(item.created_at)
        holder.link.text = item.web_link
        Glide.with(holder.itemView.context).load(item.original_image).placeholder(R.drawable.socialmedia)
            .error(R.drawable.mark).into(holder.image)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newItems: List<TopLink>) {
        itemList = newItems
        notifyDataSetChanged()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun main(time:String):String{
        // Define the input format
       val inputFormat=DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX")
        // Parse the string into a LocalDateTime object
        val datetime=ZonedDateTime.parse(time,inputFormat)
        // Define the output format.
        val outputFormat=DateTimeFormatter.ofPattern("dd MMM yyyy")
        // Convert the LocalDateTime object to the desired output string and return it
        return datetime.format(outputFormat)

    }


}