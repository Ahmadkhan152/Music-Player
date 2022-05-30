package com.example.musicplayer.Activities

import android.app.Activity
import android.app.ActivityManager
import android.content.*
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.view.WindowInsets
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.net.toUri
import com.example.musicplayer.Constants.*
import com.example.musicplayer.Models.SongModel
import com.example.musicplayer.R
import com.example.musicplayer.Services.MyServices
import com.example.musicplayer.Services.MyServices.LocalBinder
import java.text.SimpleDateFormat
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService


class SongActivity : BaseActivity() {

    lateinit var seekbar: SeekBar
    lateinit var tvSongName:TextView
    lateinit var tvAlbumName:TextView
    lateinit var tvStartPoint:TextView
    lateinit var tvEndPoint:TextView
    lateinit var ivShuffle:ImageView
    lateinit var ivSkipPrevious:ImageView
    lateinit var ivSkipNext:ImageView
    lateinit var ivPlay:ImageView
    lateinit var ivRepeat:ImageView
    lateinit var receiver: BroadcastReceiver
    lateinit var songModel: ArrayList<SongModel>
    lateinit var runnable: Runnable
    lateinit var tvNextSong:TextView
    lateinit var serviceObject:MyServices
    var checkForShuffle=false
    var currentSong:String = ""
    var nextSong:String=""
    var position=0
    var mBounded = false
    lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_song)
        val actionbar=supportActionBar
        actionbar?.setDisplayHomeAsUpEnabled(true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
        {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        }
        val worker: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()
        songModel=intent.getSerializableExtra(OBJECT) as ArrayList<SongModel>
        position=intent.getIntExtra(POSITION,-1)
        seekbar=findViewById(R.id.seekbar)
        tvSongName=findViewById(R.id.tvSongName)
        tvAlbumName=findViewById(R.id.tvAlbumName)
        tvStartPoint=findViewById(R.id.tvStartPoint)
        tvEndPoint=findViewById(R.id.tvEndPoint)
        ivShuffle=findViewById(R.id.ivShuffle)
        ivSkipPrevious=findViewById(R.id.ivSkipPrevious)
        ivSkipNext=findViewById(R.id.ivSkipNext)
        ivPlay=findViewById(R.id.ivPlay)
        mediaPlayer= MediaPlayer()
        ivRepeat=findViewById(R.id.ivRepeatSong)
        tvNextSong=findViewById(R.id.tvNextSong)
        mediaPlayer= MediaPlayer.create(this@SongActivity, songModel[position].dataList.toUri())
        val simpleDateFormat = SimpleDateFormat(PATTERN)
        val time=simpleDateFormat.format(songModel[position].timeList.toInt())
        seekbar.progress=0
        seekbar.max=mediaPlayer.duration

        class MyBroadCast:BroadcastReceiver(){
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent?.action == MY_BROADCAST)
                {
                    val progress=intent?.getIntExtra(PROGRESS_SEEKBAR,-1)
                    var duration=intent?.getIntExtra(DURATION,-1)
                    if (progress!=-1 && progress!=null)
                    {
                        seekbar.progress=progress
                        val simpleDateFormat = SimpleDateFormat(PATTERN)
                        val time=simpleDateFormat.format(progress)
                        tvStartPoint.text=time.toString()
                    }
                }
            }
        }


        val receiver=MyBroadCast()
        val intentFilter=IntentFilter()
        intentFilter.addAction(MY_BROADCAST)
        registerReceiver(receiver,intentFilter)
        val mIntent = Intent(this, MyServices::class.java)
        bindService(mIntent, mConnection, BIND_AUTO_CREATE)
        startService(intent)

        tvEndPoint.text=time
        tvSongName.text=songModel[position].audioList
        tvAlbumName.text=songModel[position].artistName
        if (position!=songModel.size)
            tvNextSong.text="Next: ${songModel[position+1].audioList}"
        else
            tvNextSong.text="Next: ${songModel[0].audioList}"

        ivShuffle.setOnClickListener {

            if (!checkForShuffle)
            {
                checkForShuffle=true
                ivShuffle.setImageResource(R.drawable.ic_baseline_shuffle_24_click)
            }
            else
            {
                checkForShuffle=false
                ivShuffle.setImageResource(R.drawable.ic_baseline_shuffle_24)
            }
        }
        ivSkipPrevious.setOnClickListener {
            serviceObject.stopMedia()
            if (position!=0)
            {
                position--
                    tvNextSong.text="Next: ${songModel[position+1].audioList}"
                mediaPlayer= MediaPlayer.create(this@SongActivity, songModel[position].dataList.toUri())
                val time=simpleDateFormat.format(songModel[position].timeList.toInt())
                tvEndPoint.text=time
                tvSongName.text=songModel[position].audioList
                tvAlbumName.text=songModel[position].artistName
                seekbar.progress=0
                seekbar.max=mediaPlayer.duration
                serviceObject.startMusic(songModel[position].dataList)
            }
            else
            {
                position=songModel.size
                tvNextSong.text="Next: ${songModel[0].audioList}"
                mediaPlayer= MediaPlayer.create(this@SongActivity, songModel[position].dataList.toUri())
                val time=simpleDateFormat.format(songModel[position].timeList.toInt())
                tvEndPoint.text=time
                tvSongName.text=songModel[position].audioList
                tvAlbumName.text=songModel[position].artistName
                seekbar.progress=0
                seekbar.max=mediaPlayer.duration
                serviceObject.startMusic(songModel[position].dataList)
            }

        }
        ivSkipNext.setOnClickListener {
            serviceObject.stopMedia()
            if (position!=songModel.size)
            {
                position++
                if ((position+1) != songModel.size)
                    tvNextSong.text="Next: ${songModel[position+1].audioList}"
                else
                    tvNextSong.text="Next: ${songModel[0].audioList}"
                val time=simpleDateFormat.format(songModel[position].timeList.toInt())
                tvEndPoint.text=time
                tvSongName.text=songModel[position].audioList
                tvAlbumName.text=songModel[position].artistName
                mediaPlayer= MediaPlayer.create(this@SongActivity, songModel[position].dataList.toUri())
                seekbar.progress=0
                seekbar.max=mediaPlayer.duration
                serviceObject.startMusic(songModel[position].dataList)
            }
            else
            {
                position=0
                tvNextSong.text="Next: ${songModel[position+1].audioList}"
                mediaPlayer= MediaPlayer.create(this@SongActivity, songModel[position].dataList.toUri())
                val time=simpleDateFormat.format(songModel[0].timeList.toInt())
                tvEndPoint.text=time
                tvSongName.text=songModel[position].audioList
                tvAlbumName.text=songModel[position].artistName
                seekbar.progress=0
                seekbar.max=mediaPlayer.duration
                serviceObject.startMusic(songModel[position].dataList)

            }
        }
        ivPlay.setOnClickListener {
            if (serviceObject.checkSong())
            {
                serviceObject.pauseSong()
                ivPlay.setImageResource(R.drawable.ic_baseline_play_arrow_24)
            }
            else{
                serviceObject.playSong()
                ivPlay.setImageResource(R.drawable.ic_baseline_pause_24)
            }
        }
        ivRepeat.setOnClickListener {
            if (!checkForShuffle)
            {
                checkForShuffle=true
                ivRepeat.setImageResource(R.drawable.ic_baseline_repeat_24_click)
            }
            else
            {
                checkForShuffle=false
                ivRepeat.setImageResource(R.drawable.ic_baseline_repeat_24)
            }
        }
        seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {

                if (p2)
                {
                    serviceObject.setPositionSong(p1)
                }

            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                //seekbar.progress=p0!!.progress
            }
        })
    }
    private val mConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName) {
            mBounded = false
        }

        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            mBounded = true
            val mLocalBinder = service as LocalBinder
            serviceObject = mLocalBinder.serverInstance()
            serviceObject.startMusic(songModel[position].dataList)
        }
    }
    override fun onStart() {
        super.onStart()

    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
    override fun onPause() {
        super.onPause()
    }

}