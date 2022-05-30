package com.example.musicplayer.Services

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.CountDownTimer
import android.os.IBinder
import android.widget.Toast
import androidx.core.net.toUri
import com.example.musicplayer.Constants.DURATION
import com.example.musicplayer.Constants.MY_BROADCAST
import com.example.musicplayer.Constants.PROGRESS_SEEKBAR


class MyServices : Service() {

    var mBinder: IBinder = LocalBinder()
    lateinit var mediaPlayer: MediaPlayer
    var currentSong=""
    lateinit var runnable: Runnable
    var intent: Intent? = Intent()
    var duration: Int? =null
    var position:Int=0
    lateinit var countDownTimer:CountDownTimer
    override fun onBind(intent: Intent?): IBinder? {
        return return mBinder
    }

    inner class LocalBinder() : Binder() {
        fun serverInstance(): MyServices{
            return this@MyServices
        }
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        return START_STICKY
    }

    fun checkSong():Boolean{
        if (mediaPlayer.isPlaying)
            return true
        return false
    }
    fun playSong()
    {
        if (mediaPlayer.currentPosition != 0)
        {
            duration=mediaPlayer.duration-mediaPlayer.currentPosition
            countDownTimer.start()
            mediaPlayer.start()
            if (duration!! /1000==0)
            {
                mediaPlayer.stop()
                countDownTimer.onFinish()
            }
            //Toast.makeText(this,"Count Down Start ${duration!! /1000}",Toast.LENGTH_SHORT).show()
        }
        else{
            //startMusic(currentSong)
            pauseSong()
        }

    }
    fun startMusic(currentSong:String)
    {
        this.currentSong=currentSong
        mediaPlayer = MediaPlayer.create(this, currentSong.toUri())
        mediaPlayer.isLooping = true
        duration=mediaPlayer.duration
        mediaPlayer.start()
        countDownTimer = object : CountDownTimer(duration?.toLong()!!, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                if (duration==0)
                {
                    mediaPlayer.stop()
                }
                var intent=Intent()
                intent.action = MY_BROADCAST
                intent.putExtra(DURATION,mediaPlayer.duration)
                intent.putExtra(PROGRESS_SEEKBAR,mediaPlayer.currentPosition)
                sendBroadcast(intent)
            }
            override fun onFinish() {
                intent?.action = MY_BROADCAST
                intent?.putExtra(DURATION,mediaPlayer.duration)
                intent?.putExtra(PROGRESS_SEEKBAR,mediaPlayer.currentPosition)
                sendBroadcast(intent)
                mediaPlayer.stop()
            }
        }.start()
        mediaPlayer.setOnCompletionListener {
            mediaPlayer.pause()
        }
    }
    fun pauseSong()
    {
        mediaPlayer.pause()
        countDownTimer.cancel()
        //Toast.makeText(this,"${mediaPlayer.duration}  ${mediaPlayer.currentPosition}",Toast.LENGTH_SHORT).show()
    }
    fun stopMedia()
    {
        mediaPlayer.stop()
        mediaPlayer.release()
    }
    fun setPositionSong(position:Int){
        mediaPlayer.seekTo(position)
    }
}