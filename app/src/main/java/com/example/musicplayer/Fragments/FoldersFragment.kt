package com.example.musicplayer.Fragments

import android.database.Cursor
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.Adapter.RecyclerviewAdapter
import com.example.musicplayer.Adapter.RecyclerviewFolder
import com.example.musicplayer.Models.FolderModel
import com.example.musicplayer.R

class FoldersFragment : Fragment() {

    val mediaStore = arrayOf<String>(
        MediaStore.Audio.Media.ALBUM)
    lateinit var recyclerview:RecyclerView
    lateinit var adapter:RecyclerviewFolder
    lateinit var audioCursor: Cursor
    lateinit var audioList:ArrayList<String>
    lateinit var folderList:ArrayList<FolderModel>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_folders, container, false)
        recyclerview=view.findViewById(R.id.recyclerview)
        recyclerview.layoutManager=LinearLayoutManager(requireContext())
        audioList=ArrayList()
        folderList= ArrayList()
        audioCursor = context?.contentResolver?.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,mediaStore,
            MediaStore.Audio.Media.IS_MUSIC+">0 and "+MediaStore.Audio.Media.DURATION+">0",null,MediaStore.Audio.Media.BUCKET_DISPLAY_NAME+ " ASC")!!
        if (audioCursor!=null)
        {
            if (audioCursor.moveToFirst())
            {
                var i:Int=0;
                var size:Int=audioCursor.count;
                var num:Int=0
                while (i<size)
                {
                    var audioIndex=audioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
                    var str=audioCursor.getString(audioIndex)
                    num++
                    if (checkAlbumName(str,audioList))
                    {
                        audioList.add(audioCursor.getString(audioIndex))
                        val obj=FolderModel(audioCursor.getString(audioIndex),num)
                        folderList.add(obj)
                        num=0
                    }
                    audioCursor.moveToNext()
                    i++
                }
            }
        }
        audioCursor.close()
        adapter=RecyclerviewFolder(requireContext(),folderList)
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
