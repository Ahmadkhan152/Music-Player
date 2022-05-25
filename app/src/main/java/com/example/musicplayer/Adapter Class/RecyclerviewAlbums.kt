package com.example.musicplayer.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.Models.FolderModel
import com.example.musicplayer.R

class RecyclerviewAlbums(val context: Context, val obj:ArrayList<FolderModel>): RecyclerView.Adapter<RecyclerviewAlbums.viewHolder>() {
    class viewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
    {

        val tvArtistName: TextView =itemView.findViewById(R.id.tvAlbumName)
        val tvAlbumCount: TextView =itemView.findViewById(R.id.tvAlbumCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder
    {
        var view= LayoutInflater.from(context).inflate(R.layout.recyclerviewalbums,parent,false)
        return viewHolder(view)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int)
    {
        holder.tvArtistName.text=obj[position].albumName
        holder.tvAlbumCount.text="${obj[position].albumCount} Track"
    }

    override fun getItemCount(): Int {
        return obj.size
    }
}