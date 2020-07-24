package code.name.monkey.retromusic.fragments.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.fragments.base.AbsMusicServiceFragment

class MainFragment : AbsMusicServiceFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }
}