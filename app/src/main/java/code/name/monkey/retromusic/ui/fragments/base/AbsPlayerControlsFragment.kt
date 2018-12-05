package code.name.monkey.retromusic.ui.fragments.base

import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.AppCompatSeekBar
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.helper.MusicProgressViewUpdateHelper

/**
 * Created by hemanths on 24/09/17.
 */

abstract class AbsPlayerControlsFragment : AbsMusicServiceFragment(), MusicProgressViewUpdateHelper.Callback {

    protected abstract fun show()

    protected abstract fun hide()

    protected abstract fun updateShuffleState()

    protected abstract fun updateRepeatState()

    protected abstract fun setUpProgressSlider()

    abstract fun setDark(color: Int)

    fun showBouceAnimation(view: View) {
        view.clearAnimation()

        view.scaleX = 0.9f
        view.scaleY = 0.9f
        view.visibility = View.VISIBLE
        view.pivotX = (view.width / 2).toFloat()
        view.pivotY = (view.height / 2).toFloat()

        view.animate()
                .setDuration(200)
                .setInterpolator(DecelerateInterpolator())
                .scaleX(1.1f)
                .scaleY(1.1f)
                .withEndAction {
                    view.animate()
                            .setDuration(200)
                            .setInterpolator(AccelerateInterpolator())
                            .scaleX(1f)
                            .scaleY(1f)
                            .alpha(1f)
                            .start()
                }
                .start()
    }


  /*  var prevButton: ImageButton? = null
    var nextButton: ImageButton? = null
    var repeatButton: ImageButton? = null
    var shuffleButton: ImageButton? = null
    var progressSlider: AppCompatSeekBar? = null
    var songTotalTime: TextView? = null
    var songCurrentProgress: TextView? = null
    var volumeContainer: View? = null
    var playPauseFab: ImageButton? = null*/

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       /* playPauseFab = view.findViewById(R.id.player_play_pause_button)
        prevButton = view.findViewById(R.id.player_prev_button)
        nextButton = view.findViewById(R.id.player_next_button)
        repeatButton = view.findViewById(R.id.player_repeat_button)
        shuffleButton = view.findViewById(R.id.player_shuffle_button)
        progressSlider = view.findViewById(R.id.player_progress_slider)
        songTotalTime = view.findViewById(R.id.player_song_total_time)
        songCurrentProgress = view.findViewById(R.id.player_song_current_progress)
        volumeContainer = view.findViewById(R.id.volume_fragment_container)*/
    }
}
