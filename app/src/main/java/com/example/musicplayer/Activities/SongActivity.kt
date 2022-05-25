package com.example.musicplayer.Activities

import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.WindowInsets
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.example.musicplayer.Constants.*
import com.example.musicplayer.Models.SongModel
import com.example.musicplayer.R
import java.lang.Runnable
import java.lang.reflect.Array
import java.text.SimpleDateFormat
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

class SongActivity : AppCompatActivity() {

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
    lateinit var songModel: ArrayList<SongModel>
    lateinit var runnable: Runnable
    lateinit var tvNextSong:TextView
    var currentSong:String = ""
    var nextSong:String=""
    var position=0
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
        tvEndPoint.text=time
        tvSongName.text=songModel[position].audioList
        tvAlbumName.text=songModel[position].artistName
        if (position!=songModel.size)
            tvNextSong.text="Next: ${songModel[position+1].audioList}"
        else
            tvNextSong.text="Next: ${songModel[0].audioList}"
        seekbar.progress=0
        seekbar.max=mediaPlayer.duration
        mediaPlayer.start()
        runnable= Runnable {
            seekbar.progress=mediaPlayer.currentPosition
            worker.schedule(runnable,1000, TimeUnit.MILLISECONDS)
            val simpleDateFormat = SimpleDateFormat(PATTERN)
            val time=simpleDateFormat.format(mediaPlayer.currentPosition.toString().toInt())
            tvStartPoint.text=time.toString()
        }
        ivShuffle.setOnClickListener {

        }
        ivSkipPrevious.setOnClickListener {
            mediaPlayer.pause()
            mediaPlayer.release()
            if (position!=0)
            {
                position--
                if ((position-1) != songModel.size)
                    tvNextSong.text=songModel[position+1].audioList
                else
                    tvNextSong.text=songModel[0].audioList
                mediaPlayer= MediaPlayer.create(this@SongActivity, songModel[position].dataList.toUri())
                val time=simpleDateFormat.format(songModel[position].timeList.toInt())
                tvEndPoint.text=time
                tvSongName.text=songModel[position].audioList
                tvAlbumName.text=songModel[position].artistName
                seekbar.progress=0
                seekbar.max=mediaPlayer.duration
                mediaPlayer.start()
            }
            else
            {
                position=songModel.size
                tvNextSong.text=songModel[0].audioList
                mediaPlayer= MediaPlayer.create(this@SongActivity, songModel[position].dataList.toUri())
                val time=simpleDateFormat.format(songModel[position].timeList.toInt())
                tvEndPoint.text=time
                tvSongName.text=songModel[position].audioList
                tvAlbumName.text=songModel[position].artistName
                seekbar.progress=0
                seekbar.max=mediaPlayer.duration
                mediaPlayer.start()

            }

        }
        ivSkipNext.setOnClickListener {
            mediaPlayer.pause()
            mediaPlayer.release()
            if (position!=songModel.size)
            {
                position++
                if ((position+1) != songModel.size)
                    tvNextSong.text=songModel[position+1].audioList
                else
                    tvNextSong.text=songModel[0].audioList
                mediaPlayer= MediaPlayer.create(this@SongActivity, songModel[position].dataList.toUri())
                val time=simpleDateFormat.format(songModel[position].timeList.toInt())
                tvEndPoint.text=time
                tvSongName.text=songModel[position].audioList
                tvAlbumName.text=songModel[position].artistName
                seekbar.progress=0
                seekbar.max=mediaPlayer.duration
                mediaPlayer.start()
            }
            else
            {
                position=0
                tvNextSong.text=songModel[position].audioList
                mediaPlayer= MediaPlayer.create(this@SongActivity, songModel[position].dataList.toUri())
                val time=simpleDateFormat.format(songModel[position].timeList.toInt())
                tvEndPoint.text=time
                tvSongName.text=songModel[position].audioList
                tvAlbumName.text=songModel[position].artistName
                seekbar.progress=0
                seekbar.max=mediaPlayer.duration
                mediaPlayer.start()

            }
        }
        ivPlay.setOnClickListener {

            if (!mediaPlayer.isPlaying){
                mediaPlayer.start()
                ivPlay.setImageResource(R.drawable.ic_baseline_pause_24)
            }
            else
            {
                mediaPlayer.pause()
                ivPlay.setImageResource(R.drawable.ic_baseline_play_arrow_24)
            }
        }
        ivRepeat.setOnClickListener {

        }
        seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {

                if (p2){
                    mediaPlayer.seekTo(p1)
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }
        })
        worker.schedule(runnable,1000, TimeUnit.MILLISECONDS)
        mediaPlayer.setOnCompletionListener {
            mediaPlayer.pause()
            ivPlay.setImageResource(R.drawable.ic_baseline_play_arrow_24)
        }
    }

    override fun onPause() {
        super.onPause()

        //mediaPlayer.pause()
        //mediaPlayer.release()

    }
}