package com.example.musicplayer.Activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.musicplayer.Adapter.PageAdapter
import com.example.musicplayer.Models.SongModel
import com.example.musicplayer.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class HomeActivity : BaseActivity() {
    var permissions=arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.MODIFY_AUDIO_SETTINGS)
    private val requestCode=567



//Start of declaration
val mediaStore = arrayOf<String>(MediaStore.Audio.Media._ID,MediaStore.Audio.Media.DISPLAY_NAME,MediaStore.Audio.Media.DURATION)
    lateinit var viewpager: ViewPager2
    lateinit var tablayout: TabLayout
    lateinit var constraintlayout:ConstraintLayout
    lateinit var tvSongName:TextView
    lateinit var audioList:ArrayList<String>
    lateinit var timeList:ArrayList<String>

//End of declaration
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        if (Build.VERSION.SDK_INT >= 21) {
            val window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor=ContextCompat.getColor(this, R.color.orange)
        }
        //Initialization of all attributes
        audioList=ArrayList()
        timeList= ArrayList()
        constraintlayout=findViewById(R.id.contraintlayoutbottom)
        tvSongName=findViewById(R.id.tvSongName)
        viewpager = findViewById<ViewPager2>(R.id.viewpager)
        tablayout=findViewById<TabLayout>(R.id.tablayout)
    //Finish Initialization of all attributes
        viewpager.adapter= PageAdapter(supportFragmentManager,lifecycle)
        tablayout.setSelectedTabIndicatorColor(Color.parseColor("#FD8427"));
        tablayout.setTabTextColors(Color.parseColor("#FFFFFF"), Color.parseColor("#FD8427"));
    if (checkPermission())
    {
        viewpager.visibility= View.VISIBLE
        tablayout.visibility=View.VISIBLE
    }
    else
    {
        ActivityCompat.requestPermissions(this@HomeActivity,permissions,requestCode)
    }
    TabLayoutMediator(tablayout,viewpager){
            tab,position->
            when (position)
            {
                0 -> tab.text="PLAYLISTS"
                1 -> tab.text="FOLDERS"
                2 -> tab.text="ARTISTS"
                3 -> tab.text="ALBUMS"
                4 -> tab.text="TRACKS"
                else -> tab.text="PLAYLISTS"
            }

        }.attach()

    }

    private fun checkPermission():Boolean{
        for (permission in permissions)
        {
            var result=application.checkCallingOrSelfPermission(permission)
            if (result!=PackageManager.PERMISSION_GRANTED)
            {
                return false
            }
        }
        return true
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode)
        {
            this.requestCode->{
                if (grantResults[0]==PackageManager.PERMISSION_GRANTED && grantResults[1]==PackageManager.PERMISSION_GRANTED)
                {
                    viewpager.visibility=View.VISIBLE
                    tablayout.visibility=View.VISIBLE
                }
                else
                {
                    showToast("Error! Permission Is Denied By User")
                }
            }
            else ->
            {
                showToast("Error! Something Was Wrong")
            }
        }
    }
    private fun showToast(message: String) {
        Toast.makeText(applicationContext,message,Toast.LENGTH_SHORT).show()
    }
}