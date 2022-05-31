package com.example.musicplayer.Fragments

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.musicplayer.Constants.*
import com.example.musicplayer.R

class FragmentLayout() : Fragment() {

    lateinit var tvSongName: TextView
    lateinit var ivSong:ImageView
    var check=false
    var songState= PLAY
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view= inflater.inflate(R.layout.fragment_layout, container, false)

        tvSongName=view.findViewById<TextView>(R.id.tvSongName)
        ivSong=view.findViewById(R.id.ivPlay)
        class MyBroadCast: BroadcastReceiver(){
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent?.action == BROADCAST_SONG)
                {
                    val songName=intent?.getStringExtra(SONG_NAME)
                    songState=intent?.getIntExtra(CHECK_SONG_STATE,2)
                    if (songState== PAUSE)
                    {
                        ivSong.setImageResource(R.drawable.ic_baseline_play_arrow_24)
                        check=true
                    }
                    else if (songState == PLAY)
                    {
                        ivSong.setImageResource(R.drawable.ic_baseline_pause_24)
                        check=false
                    }

                }
            }
        }


        val receiver=MyBroadCast()
        val intentFilter= IntentFilter()
        intentFilter.addAction(BROADCAST_SONG)
        context?.registerReceiver(receiver,intentFilter)
        ivSong.setOnClickListener {
            if (check)
            {
                check=false
                ivSong.setImageResource(R.drawable.ic_baseline_pause_24)
                var intent= Intent()
                intent.action = MY_BROADCAST
                intent.putExtra(CHECK_EVENT_FROM_MAIN_ACTIVITY,true)
                intent.putExtra(CHECK_SONG_STATE,PAUSE)
                context?.sendBroadcast(intent)
            }
            else
            {
                check=true
                ivSong.setImageResource(R.drawable.ic_baseline_play_arrow_24)
                var intent= Intent()
                intent.action = MY_BROADCAST
                intent.putExtra(CHECK_EVENT_FROM_MAIN_ACTIVITY,true)
                intent.putExtra(CHECK_SONG_STATE,PLAY)
                context?.sendBroadcast(intent)
            }
        }


        return view
    }
    fun setText(songName:String)
    {
        tvSongName.text=songName
    }
}