package com.example.musicplayer.Activities

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.Adapter.RecyclerviewAdapter
import com.example.musicplayer.Constants.*
import com.example.musicplayer.Fragments.FragmentLayout
import com.example.musicplayer.Models.SongModel
import com.example.musicplayer.R

class AllTracksActivity : BaseActivity() {
    lateinit var recyclerView: RecyclerView
    lateinit var recyclerAdapter: RecyclerviewAdapter
    lateinit var songModel: ArrayList<SongModel>
    lateinit var constraintlayout: ConstraintLayout
    lateinit var tvSongName: TextView
    lateinit var frameLayout: FrameLayout
    var songName=""
    var artistName=""
    private val getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {

        if(it.resultCode == Activity.RESULT_OK){
                constraintlayout.visibility= View.VISIBLE
                songName= it!!.data?.getStringExtra(SONG_NAME).toString()
                artistName=it!!.data?.getStringExtra(ARTIST_NAME).toString()
                tvSongName.text=songName+artistName
            }
            else
            {
                songName=""
                artistName=""
            }


        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_tracks)
        val toolbar: androidx.appcompat.widget.Toolbar =
            findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val actionbar = supportActionBar
        actionbar?.setDisplayHomeAsUpEnabled(true)

        if (Build.VERSION.SDK_INT >= 21) {
            val window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ContextCompat.getColor(this, R.color.orange)
        }
        songModel = intent.getSerializableExtra(SONGS) as ArrayList<SongModel>
        recyclerView = findViewById(R.id.recyclerview)
//        tvSongName=findViewById(R.id.tvSongName)
        frameLayout=findViewById(R.id.framelayout)
        val fragmentManager=supportFragmentManager
        val fragmenTransaction=fragmentManager.beginTransaction()
        val fragmentLayout= FragmentLayout()
        fragmenTransaction.add(R.id.framelayout,fragmentLayout).commit()
        recyclerAdapter = RecyclerviewAdapter(this, songModel) { position ->
            val intent: Intent = Intent(this,SongActivity::class.java)
            intent.putExtra(OBJECT,songModel)
            intent.putExtra(POSITION,position)
            getContent.launch(intent)
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = recyclerAdapter
        class MyBroadCast: BroadcastReceiver(){
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent?.action == MY_BROADCAST_AllTracks)
                {
                    val songName=intent?.getStringExtra(SONG_NAME)
                    fragmentLayout.setText(songName!!)
                    frameLayout.visibility=View.VISIBLE
                }
            }
        }


        val receiver=MyBroadCast()
        val intentFilter= IntentFilter()
        intentFilter.addAction(MY_BROADCAST_AllTracks)
        registerReceiver(receiver,intentFilter)

    }

    fun showData(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }
}