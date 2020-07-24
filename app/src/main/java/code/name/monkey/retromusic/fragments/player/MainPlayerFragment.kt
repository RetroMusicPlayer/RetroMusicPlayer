package code.name.monkey.retromusic.fragments.player

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.util.PreferenceUtil

class MainPlayerFragment : Fragment(R.layout.fragment_main_player) {
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        when (PreferenceUtil.nowPlayingScreen) {
            else -> findNavController().navigate(R.id.action_mainPlayerFragment_to_adaptiveFragment)
        }
    }
}