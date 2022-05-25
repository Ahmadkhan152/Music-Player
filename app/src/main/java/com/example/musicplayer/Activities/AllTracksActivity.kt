package com.example.musicplayer.Activities

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.musicplayer.Adapter.RecyclerviewAdapter
import com.example.musicplayer.Constants.SONGS
import com.example.musicplayer.Models.SongModel
import com.example.musicplayer.R

class AllTracksActivity : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    lateinit var recyclerAdapter: RecyclerviewAdapter
    lateinit var songModel: ArrayList<SongModel>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_tracks)
        val toolbar:androidx.appcompat.widget.Toolbar= findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val actionbar=supportActionBar
        actionbar?.setDisplayHomeAsUpEnabled(true)

        if (Build.VERSION.SDK_INT >= 21) {
            val window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor= ContextCompat.getColor(this, R.color.orange)
        }
        songModel=intent.getSerializableExtra(SONGS) as ArrayList<SongModel>
        recyclerView=findViewById(R.id.recyclerview)
        recyclerAdapter=RecyclerviewAdapter(this,songModel)
        recyclerView.layoutManager=LinearLayoutManager(this)
        recyclerView.adapter=recyclerAdapter

    }
    fun showData(message:String)
    {
        Toast.makeText(applicationContext,message,Toast.LENGTH_SHORT).show()
    }
}