package com.example.musicplayer.Fragments

import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.Activities.SongActivity
import com.example.musicplayer.Adapter.RecyclerviewAdapter
import com.example.musicplayer.Constants.ARTIST_NAME
import com.example.musicplayer.Constants.OBJECT
import com.example.musicplayer.Constants.POSITION
import com.example.musicplayer.Constants.SONG_NAME
import com.example.musicplayer.Models.SongModel
import com.example.musicplayer.R


class TracksFragment : Fragment() {


    private val mediaStore = arrayOf<String>(
        MediaStore.Audio.Media._ID,
        MediaStore.Audio.Media.DATA,
        MediaStore.Audio.Media.TITLE,
        MediaStore.Audio.Media.DURATION,
    MediaStore.Audio.Media.ARTIST)
    private var getContent1 = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult? ->

            if (result?.resultCode == Activity.RESULT_OK) {

//                constraintlayout.visibility= View.VISIBLE
//                songName= intent.getStringExtra(SONG_NAME).toString()
//                artistName=intent.getStringExtra(ARTIST_NAME).toString()
//                tvSongName.text=songName+artistName
            }
            else
            {
//                songName=""
//                artistName=""
            }
        }
    lateinit var audioCursor: Cursor
    lateinit var timeList:ArrayList<String>
    lateinit var audioList:ArrayList<String>
    lateinit var dataList:ArrayList<String>
    lateinit var songModel: ArrayList<SongModel>
    lateinit var recyclerview:RecyclerView
    lateinit var adapter:RecyclerviewAdapter
    lateinit var tvSongName:TextView
    lateinit var constraintlayout:ConstraintLayout
    private val getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult? ->

        if (result?.resultCode == Activity.RESULT_OK)
        {

        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view= inflater.inflate(R.layout.fragment_tracks, container, false)
        recyclerview=view.findViewById(R.id.recyclerview)
        recyclerview.layoutManager=LinearLayoutManager(requireContext())
        audioList=ArrayList()
        timeList= ArrayList()
        dataList=ArrayList()
        songModel= ArrayList()
        audioCursor = context?.contentResolver?.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,mediaStore,
            MediaStore.Audio.Media.IS_MUSIC+">0 and "+MediaStore.Audio.Media.DURATION+" > 0",null,null)!!
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
        adapter= RecyclerviewAdapter(requireContext(),songModel) { position ->
            val intent: Intent = Intent(context, SongActivity::class.java)
            intent.putExtra(OBJECT,songModel)
            intent.putExtra(POSITION,position)
            getContent1.launch(intent)
        }
        recyclerview.adapter=adapter
        return view
    }
    fun showData(message:String)
    {
        Toast.makeText(requireContext(),message, Toast.LENGTH_SHORT).show()
    }
    fun checkAlbumName(str:String,albumName:ArrayList<String>):Boolean
    {
        var i:Int=0
        while (i<albumName.size)
        {
            if (str==albumName[i])
                return false
            i++
        }
        return true
    }
}