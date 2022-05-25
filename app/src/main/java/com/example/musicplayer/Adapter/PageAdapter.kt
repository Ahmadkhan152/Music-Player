package com.example.musicplayer.Adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.musicplayer.Fragments.*

class PageAdapter(val fragment:FragmentManager,val lifecycle: Lifecycle):FragmentStateAdapter(fragment,lifecycle) {
    private val size=5
    override fun getItemCount(): Int {
        return size
    }

    override fun createFragment(position: Int): Fragment {

        when(position)
        {
            0 -> return PlaylistsFragment()
            1 -> return FoldersFragment()
            2 -> return ArtistsFragment()
            3 -> return AlbumsFragment()
            4 -> return TracksFragment()
            else -> return PlaylistsFragment()
        }

    }

}