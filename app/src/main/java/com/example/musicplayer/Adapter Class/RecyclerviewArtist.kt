package com.example.musicplayer.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.Models.ArtistModel
import com.example.musicplayer.R


class RecyclerviewArtist(val context: Context, val obj:ArrayList<ArtistModel>): RecyclerView.Adapter<RecyclerviewArtist.viewHolder>() {
    class viewHolder(itemView: View):RecyclerView.ViewHolder(itemView)
    {
        val tvArtistName: TextView =itemView.findViewById(R.id.tvAlbumName)
        val tvAlbumCount:TextView=itemView.findViewById(R.id.tvAlbumCount)
        val tvTrackCount:TextView=itemView.findViewById(R.id.tvTrackCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        var view= LayoutInflater.from(context).inflate(R.layout.mainartistlayoutrecycler,parent,false)
        return viewHolder(view)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        holder.tvArtistName.text=obj[position].artistName
        holder.tvAlbumCount.text="${obj[position].albumCount} Album,"
        holder.tvTrackCount.text="${obj[position].trackCount} Track"
    }

    override fun getItemCount(): Int {
        return obj.size
    }
}