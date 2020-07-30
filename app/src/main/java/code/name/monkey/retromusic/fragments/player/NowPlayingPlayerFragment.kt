package code.name.monkey.retromusic.fragments.player

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.extensions.navController
import code.name.monkey.retromusic.fragments.NowPlayingScreen.*
import code.name.monkey.retromusic.util.PreferenceUtil

class NowPlayingPlayerFragment : Fragment(R.layout.fragment_now_playing_player) {
    companion object {
        const val TAG = "NowPlaying"
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val navController = navController(R.id.playerFragmentContainer)
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