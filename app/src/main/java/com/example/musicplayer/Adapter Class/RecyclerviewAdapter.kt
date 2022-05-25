package com.example.musicplayer.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.Activities.SongActivity
import com.example.musicplayer.Constants.*
import com.example.musicplayer.Models.SongModel
import com.example.musicplayer.R
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class RecyclerviewAdapter(val context: Context,val obj:ArrayList<SongModel>):RecyclerView.Adapter<RecyclerviewAdapter.viewHolder>() {
    class viewHolder(itemView:View):RecyclerView.ViewHolder(itemView)
    {
        val tvSongName=itemView.findViewById<TextView>(R.id.tvSongName)
        val tvSongTime=itemView.findViewById<TextView>(R.id.tvSongTime)
        val constraintlayout: ConstraintLayout =itemView.findViewById<ConstraintLayout>(R.id.recyclerviewconstraintlayout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        var view=LayoutInflater.from(context).inflate(R.layout.recyclerviewadapter,parent,false)
        return viewHolder(view)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        holder.tvSongName.text=obj[position].audioList
        if (obj[position].timeList!=null)
        {
            val simpleDateFormat = SimpleDateFormat(PATTERN)
            val time=simpleDateFormat.format(obj[position].timeList.toInt())
            holder.tvSongTime.text = "${time}"
        }
        holder.constraintlayout.setOnClickListener {
            val intent:Intent=Intent(context,SongActivity::class.java)
//            intent.putExtra(CURRENT_SONG,obj[position]?.dataList)
//            intent.putExtra(DURATION,obj[position]?.timeList)
//            if (position!=obj.size)
//                intent.putExtra(NEXT_SONG,obj[position+1].dataList)
            intent.putExtra(OBJECT,obj)
            intent.putExtra(POSITION,position)
            context.startActivity(intent)
        }
    }
    override fun getItemCount(): Int {
        return obj.size
    }
}