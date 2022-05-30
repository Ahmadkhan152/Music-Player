package com.example.musicplayer.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.musicplayer.R

open class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)






    }
}