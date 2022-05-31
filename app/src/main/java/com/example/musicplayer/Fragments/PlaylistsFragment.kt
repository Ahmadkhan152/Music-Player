package com.example.musicplayer.Fragments

import android.app.DownloadManager
import android.content.Intent
import android.database.Cursor
import android.media.MediaPlayer
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.net.toUri
import androidx.fragment.app.FragmentManager
import com.example.musicplayer.Activities.AllTracksActivity
import com.example.musicplayer.Activities.HomeActivity
import com.example.musicplayer.Constants.SONGS
import com.example.musicplayer.Interface.SongDataPassInterface
import com.example.musicplayer.Models.SongModel
import com.example.musicplayer.R

class PlaylistsFragment : Fragment() {

    //Declaraction of all attributes

    val mediaStore = arrayOf<String>(MediaStore.Audio.Media._ID,MediaStore.Audio.Media.TITLE,
        MediaStore.Audio.Media.DURATION,
        MediaStore.Audio.Media.DATA,
    MediaStore.Audio.Media.ARTIST)


    lateinit var audioCursor: Cursor
    lateinit var songModel: ArrayList<SongModel>
    lateinit var tvNoOfSongs:TextView
    lateinit var mediaPlayer: MediaPlayer
    lateinit var constraintlayout: ConstraintLayout


    //Finish Declaration of all attributes
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view= inflater.inflate(R.layout.fragment_playlists, container, false)
        constraintlayout=view.findViewById(R.id.constraintlayout)
        tvNoOfSongs=view.findViewById(R.id.tvNoOfSongs)
        songModel= ArrayList()
        audioCursor = context?.contentResolver?.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,mediaStore,
            MediaStore.Audio.Media.IS_MUSIC+">0 and "+MediaStore.Audio.Media.DURATION+">0",null,null)!!
        if (audioCursor!=null)
        {
            if (audioCursor.moveToFirst())
            {
                var i:Int=0;
                var size:Int=audioCursor.count;
                while (i<size)
                {
                    var audioIndex=audioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
                    var durationIndex=audioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
                    var dataIndex=audioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
                    var artistIndex=audioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
                    val model=SongModel(audioCursor.getString(dataIndex),audioCursor.getString(audioIndex),audioCursor.getString(durationIndex),audioCursor.getString(artistIndex))
                    songModel.add(model)
                    audioCursor.moveToNext()
                    i++
                }
            }
        }
        audioCursor.close()
        tvNoOfSongs.text="${songModel.size-1}"
        constraintlayout.setOnClickListener {
            val intent= Intent(context, AllTracksActivity::class.java)
            intent.putExtra(SONGS,songModel)
            startActivity(intent)
        }
        return view
    }
    fun showData(message:String)
    {
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show()
    }
}