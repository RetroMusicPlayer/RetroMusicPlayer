package code.name.monkey.retromusic.fragments.player.tiny

import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import code.name.monkey.appthemehelper.util.ColorUtil
import code.name.monkey.appthemehelper.util.MaterialValueHelper
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.fragments.base.AbsPlayerControlsFragment
import code.name.monkey.retromusic.helper.MusicPlayerRemote
import code.name.monkey.retromusic.service.MusicService
import kotlinx.android.synthetic.main.fragment_tiny_controls_fragment.*

class TinyPlaybackControlsFragment : AbsPlayerControlsFragment() {

    override fun show() {

    }

    override fun hide() {

    }

    override fun setUpProgressSlider() {

    }

    override fun setDark(color: Int) {

        if (ColorUtil.isColorLight(color)) {
            lastPlaybackControlsColor = MaterialValueHelper.getSecondaryTextColor(requireContext(), true)
            lastDisabledPlaybackControlsColor = MaterialValueHelper.getSecondaryDisabledTextColor(requireContext(), true)
        } else {
            lastPlaybackControlsColor = MaterialValueHelper.getPrimaryTextColor(requireContext(), false)
            lastDisabledPlaybackControlsColor = MaterialValueHelper.getPrimaryDisabledTextColor(requireContext(), false)
        }

        updateRepeatState();
        updateShuffleState();
    }

    override fun onUpdateProgressViews(progress: Int, total: Int) {

    }

    private var lastPlaybackControlsColor: Int = 0
    private var lastDisabledPlaybackControlsColor: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tiny_controls_fragment, container, false);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpMusicControllers()
    }

    private fun setUpMusicControllers() {
        setUpRepeatButton()
        setUpShuffleButton()
        setUpProgressSlider()
    }

    private fun setUpShuffleButton() {
        playerShuffleButton.setOnClickListener { MusicPlayerRemote.toggleShuffleMode() }
    }

    private fun setUpRepeatButton() {
        playerRepeatButton.setOnClickListener { MusicPlayerRemote.cycleRepeatMode() }
    }

    override fun updateShuffleState() {
        when (MusicPlayerRemote.shuffleMode) {
            MusicService.SHUFFLE_MODE_SHUFFLE -> playerShuffleButton.setColorFilter(lastPlaybackControlsColor, PorterDuff.Mode.SRC_IN)
            else -> playerShuffleButton.setColorFilter(lastDisabledPlaybackControlsColor, PorterDuff.Mode.SRC_IN)
        }
    }

    override fun updateRepeatState() {
        when (MusicPlayerRemote.repeatMode) {
            MusicService.REPEAT_MODE_NONE -> {
                playerRepeatButton.setImageResource(R.drawable.ic_repeat_white_24dp)
                playerRepeatButton.setColorFilter(lastDisabledPlaybackControlsColor, PorterDuff.Mode.SRC_IN)
            }
            MusicService.REPEAT_MODE_ALL -> {
                playerRepeatButton.setImageResource(R.drawable.ic_repeat_white_24dp)
                playerRepeatButton.setColorFilter(lastPlaybackControlsColor, PorterDuff.Mode.SRC_IN)
            }
            MusicService.REPEAT_MODE_THIS -> {
                playerRepeatButton.setImageResource(R.drawable.ic_repeat_one_white_24dp)
                playerRepeatButton.setColorFilter(lastPlaybackControlsColor, PorterDuff.Mode.SRC_IN)
            }
        }
    }

    override fun onServiceConnected() {
        updateRepeatState()
        updateShuffleState()
    }

    override fun onRepeatModeChanged() {
        updateRepeatState()
    }

    override fun onShuffleModeChanged() {
        updateShuffleState()
    }
}