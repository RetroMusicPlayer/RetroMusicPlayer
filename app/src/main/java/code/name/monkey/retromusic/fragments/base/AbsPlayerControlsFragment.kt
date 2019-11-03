package code.name.monkey.retromusic.fragments.base

import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.extensions.fistElement
import code.name.monkey.retromusic.extensions.hidden
import code.name.monkey.retromusic.extensions.lastElement
import code.name.monkey.retromusic.extensions.show
import code.name.monkey.retromusic.fragments.VolumeFragment
import code.name.monkey.retromusic.helper.MusicPlayerRemote
import code.name.monkey.retromusic.helper.MusicProgressViewUpdateHelper
import code.name.monkey.retromusic.util.PreferenceUtil
import kotlinx.android.synthetic.main.fragment_adaptive_player_playback_controls.*


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

    fun showBonceAnimation(view: View) {
        view.apply {
            clearAnimation()
            scaleX = 0.9f
            scaleY = 0.9f
            visibility = View.VISIBLE
            pivotX = (view.width / 2).toFloat()
            pivotY = (view.height / 2).toFloat()

            animate().setDuration(200)
                    .setInterpolator(DecelerateInterpolator())
                    .scaleX(1.1f)
                    .scaleY(1.1f)
                    .withEndAction {
                        animate().setDuration(200)
                                .setInterpolator(AccelerateInterpolator())
                                .scaleX(1f)
                                .scaleY(1f)
                                .alpha(1f)
                                .start()
                    }
                    .start()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideVolumeIfAvailable()
    }

    protected var volumeFragment: VolumeFragment? = null

    private fun hideVolumeIfAvailable() {
        if (PreferenceUtil.getInstance(requireContext()).volumeToggle) {
            childFragmentManager.beginTransaction().replace(R.id.volumeFragmentContainer, VolumeFragment()).commit()
            childFragmentManager.executePendingTransactions()
            volumeFragment = childFragmentManager.findFragmentById(R.id.volumeFragmentContainer) as VolumeFragment?
        }
    }

    companion object {
        const val SLIDER_ANIMATION_TIME: Long = 400
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        updateButtons()
    }

    override fun onPlayingMetaChanged() {
        super.onPlayingMetaChanged()
        updateButtons()
    }

    private fun updateButtons() {
        if (MusicPlayerRemote.playingQueue.fistElement()) {
            previousButton?.hidden()
        } else {
            previousButton?.show()
        }

        if (MusicPlayerRemote.playingQueue.lastElement()) {
            nextButton?.hidden()
        } else {
            nextButton?.show()
        }
    }
}
