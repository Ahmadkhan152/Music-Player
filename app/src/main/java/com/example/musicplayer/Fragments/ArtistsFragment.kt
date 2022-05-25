package com.example.musicplayer.Fragments

import android.database.Cursor
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.Adapter.RecyclerviewArtist
import com.example.musicplayer.Models.ArtistModel
import com.example.musicplayer.R


class ArtistsFragment : Fragment() {

    val mediaStore = arrayOf<String>(MediaStore.Audio.Media.ARTIST_ID,MediaStore.Audio.Media.ALBUM,
        MediaStore.Audio.Media.ARTIST)

    lateinit var audioCursor: Cursor
    lateinit var recyclerAdapter: RecyclerviewArtist
    lateinit var artistList:ArrayList<String>
    lateinit var artistCount:ArrayList<Int>
    lateinit var albumCount:ArrayList<Int>
    lateinit var artistModel: ArtistModel
    lateinit var constraintlayout: ConstraintLayout
    lateinit var recyclerview:RecyclerView
    lateinit var albumName:ArrayList<String>
    lateinit var trackList:ArrayList<Int>
    lateinit var obj:ArrayList<ArtistModel>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view= inflater.inflate(R.layout.fragment_artists, container, false)
        //constraintlayout=view.findViewById(R.id.constraintlayout)
        recyclerview=view.findViewById(R.id.recyclerview)
        artistList=ArrayList()
        artistCount=ArrayList()
        obj=ArrayList()
        albumCount= ArrayList()
        albumName= ArrayList()
        trackList=ArrayList()
        audioCursor = context?.contentResolver?.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,mediaStore,
            MediaStore.Audio.Media.IS_MUSIC+">0 and "+MediaStore.Audio.Media.DURATION+">0",null,null)!!
        if (audioCursor!=null)
        {
            if (audioCursor.moveToFirst())
            {
                var i:Int=0;
                var size:Int=audioCursor.count;
                audioCursor.moveToNext()
                while (i<size-1)
                {
                    var audioIndex=audioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
                    var albumIndex=audioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
                    var index=audioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST_ID)
                    var str:String=audioCursor.getString(audioIndex)
                    if (checkArtistName(str,artistList))
                    {
                        artistList.add(str)
                        albumName.add("${audioCursor.getString(albumIndex)}")
                    }
                    audioCursor.moveToNext()
                    i++
                }
            }
        }
        var j:Int=0;
        while(j<artistList.size)
        {
            var i:Int=0;
            if (audioCursor.moveToFirst())
            {
                var albumCount:Int=0
                var size:Int=audioCursor.count;
                while (i<size)
                {
                    var audioIndex =
                        audioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
                    var str: String = audioCursor.getString(audioIndex)
                    if (artistList[j]==str)
                        albumCount++
                    audioCursor.moveToNext()
                    i++
                }
                trackList.add(albumCount)
                //var objArtist:ArtistModel= ArtistModel(artistList[j],0,albumCount)
                //obj.add(objArtist)
            }
            j++
        }
        j=0
        while(j<albumName.size)
        {
            var i:Int=0;
            if (audioCursor.moveToFirst())
            {
                var albumCount:Int=0
                var size:Int=audioCursor.count;
                while (i<size)
                {
                    var audioIndex =
                        audioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
                    var str: String = audioCursor.getString(audioIndex)
                    if (albumName[j]==str)
                        albumCount++
                    audioCursor.moveToNext()
                    i++
                }

                var objArtist:ArtistModel= ArtistModel(artistList[j],albumCount,trackList[j])
                obj.add(objArtist)
            }
            j++
        }
        audioCursor.close()
        recyclerAdapter = RecyclerviewArtist(requireContext(),obj)
        recyclerview.layoutManager=LinearLayoutManager(context)
        recyclerview.adapter=recyclerAdapter
        return view
    }
    fun checkArtistName(str:String,artistList:ArrayList<String>):Boolean
    {
        var i:Int=0
        while (i<artistList.size)
        {
            if (str==artistList[i])
                return false
            i++
        }

        return true
    }
    fun showData(message:String)
    {
        Toast.makeText(requireContext(),message, Toast.LENGTH_SHORT).show()
    }

}