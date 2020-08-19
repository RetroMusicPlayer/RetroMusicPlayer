package io.github.muntashirakon.music.fragments.player

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import io.github.muntashirakon.music.R
import io.github.muntashirakon.music.extensions.findNavController
import io.github.muntashirakon.music.fragments.NowPlayingScreen.*
import io.github.muntashirakon.music.util.PreferenceUtil

class NowPlayingPlayerFragment : Fragment(R.layout.fragment_now_playing_player) {
    companion object {
        const val TAG = "NowPlaying"
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val navController = findNavController(R.id.playerFragmentContainer)
        updateNowPlaying(navController)
    }

    private fun updateNowPlaying(navController: NavController) {
        when (PreferenceUtil.nowPlayingScreen) {
            Adaptive -> navController.navigate(R.id.adaptiveFragment)
            Blur -> navController.navigate(R.id.blurPlayerFragment)
            BlurCard -> navController.navigate(R.id.cardBlurFragment)
            Card -> navController.navigate(R.id.cardFragment)
            Circle -> navController.navigate(R.id.circlePlayerFragment)
            Classic -> navController.navigate(R.id.classicPlayerFragment)
            Color -> navController.navigate(R.id.colorFragment)
            Fit -> navController.navigate(R.id.fitFragment)
            Flat -> navController.navigate(R.id.flatPlayerFragment)
            Full -> navController.navigate(R.id.fullPlayerFragment)
            Gradient -> navController.navigate(R.id.gradientPlayerFragment)
            Material -> navController.navigate(R.id.materialFragment)
            Plain -> navController.navigate(R.id.plainPlayerFragment)
            Simple -> navController.navigate(R.id.simplePlayerFragment)
            Tiny -> navController.navigate(R.id.tinyPlayerFragment)
            else -> navController.navigate(R.id.playerFragment)
        }
    }
}