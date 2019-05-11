package code.name.monkey.retromusic.fragments.player.classic

import android.animation.ObjectAnimator
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.SeekBar
import androidx.annotation.NonNull
import code.name.monkey.appthemehelper.util.ColorUtil
import code.name.monkey.appthemehelper.util.MaterialValueHelper
import code.name.monkey.appthemehelper.util.TintHelper
import code.name.monkey.retromusic.helper.MusicPlayerRemote
import code.name.monkey.retromusic.helper.MusicProgressViewUpdateHelper
import code.name.monkey.retromusic.helper.PlayPauseButtonOnClickHandler
import code.name.monkey.retromusic.misc.SimpleOnSeekbarChangeListener
import code.name.monkey.retromusic.service.MusicService
import code.name.monkey.retromusic.fragments.base.AbsPlayerControlsFragment
import code.name.monkey.retromusic.util.MusicUtil
import code.name.monkey.retromusic.views.PlayPauseDrawable
import kotlinx.android.synthetic.main.fragment_classic_player_playback_controls.*


class ClassicPlayerPlaybackControlsFragment : AbsPlayerControlsFragment() {
    public override fun show() {
        playerPlayPauseFab.animate()
                .scaleX(1f)
                .scaleY(1f)
                .setInterpolator(DecelerateInterpolator())
                .start()
    }

    public override fun hide() {
        if (playerPlayPauseFab != null) {
            playerPlayPauseFab.scaleX = 0f
            playerPlayPauseFab.scaleY = 0f
            playerPlayPauseFab.rotation = 0f
        }
    }


    override fun setDark(color: Int) {

    }

    fun setDark(dark: Boolean) {
        if (dark) {
            lastPlaybackControlsColor = MaterialValueHelper.getSecondaryTextColor(activity, true)
            lastDisabledPlaybackControlsColor = MaterialValueHelper.getSecondaryDisabledTextColor(activity, true)
        } else {
            lastPlaybackControlsColor = MaterialValueHelper.getPrimaryTextColor(activity, false)
            lastDisabledPlaybackControlsColor = MaterialValueHelper.getPrimaryDisabledTextColor(activity, false)
        }

        //volumeFragment?.setTintableColor(lastPlaybackControlsColor)

        updateRepeatState()
        updateShuffleState()
        updatePrevNextColor()
        updateProgressTextColor()
    }

    private var playerFabPlayPauseDrawable: PlayPauseDrawable? = null

    private var lastPlaybackControlsColor = 0
    private var lastDisabledPlaybackControlsColor = 0

    private lateinit var progressViewUpdateHelper: MusicProgressViewUpdateHelper



    override fun onCreateView(@NonNull inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(code.name.monkey.retromusic.R.layout.fragment_classic_player_playback_controls, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        progressViewUpdateHelper = MusicProgressViewUpdateHelper(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpMusicControllers()
        updateProgressTextColor()

        //volumeFragment = childFragmentManager.findFragmentById(R.id.volumeFragment) as VolumeFragment
    }

    private fun setUpMusicControllers() {
        setUpPlayPauseFab()
        setUpPrevNext()
        setUpRepeatButton()
        setUpShuffleButton()
        setUpProgressSlider()
    }

    private fun updateProgressTextColor() {
        val color = MaterialValueHelper.getPrimaryTextColor(context, false)
        playerSongTotalTime.setTextColor(color)
        playerSongCurrentProgress.setTextColor(color)
    }

    private fun setUpPlayPauseFab() {
        val fabColor = Color.WHITE
        TintHelper.setTintAuto(playerPlayPauseFab, fabColor, true)

        playerFabPlayPauseDrawable = PlayPauseDrawable(activity!!)

        playerPlayPauseFab.setImageDrawable(playerFabPlayPauseDrawable) // Note: set the drawable AFTER TintHelper.setTintAuto() was called
        playerPlayPauseFab.setColorFilter(MaterialValueHelper.getPrimaryTextColor(context, ColorUtil.isColorLight(fabColor)), PorterDuff.Mode.SRC_IN)
        playerPlayPauseFab.setOnClickListener(PlayPauseButtonOnClickHandler())
        playerPlayPauseFab.post {
            if (playerPlayPauseFab != null) {
                playerPlayPauseFab.pivotX = (playerPlayPauseFab.width / 2).toFloat()
                playerPlayPauseFab.pivotY = (playerPlayPauseFab.height / 2).toFloat()
            }
        }
    }

    private fun setUpPrevNext() {
        updatePrevNextColor()
        playerNextButton.setOnClickListener { _ -> MusicPlayerRemote.playNextSong() }
        playerPrevButton.setOnClickListener { _ -> MusicPlayerRemote.back() }
    }

    private fun updatePrevNextColor() {
        playerNextButton.setColorFilter(lastPlaybackControlsColor, PorterDuff.Mode.SRC_IN)
        playerPrevButton.setColorFilter(lastPlaybackControlsColor, PorterDuff.Mode.SRC_IN)
    }

    private fun setUpShuffleButton() {
        playerShuffleButton.setOnClickListener { MusicPlayerRemote.toggleShuffleMode() }
    }

    override fun updateShuffleState() {
        when (MusicPlayerRemote.shuffleMode) {
            MusicService.SHUFFLE_MODE_SHUFFLE -> playerShuffleButton.setColorFilter(lastPlaybackControlsColor, PorterDuff.Mode.SRC_IN)
            else -> playerShuffleButton.setColorFilter(lastDisabledPlaybackControlsColor, PorterDuff.Mode.SRC_IN)
        }
    }

    private fun setUpRepeatButton() {
        playerRepeatButton.setOnClickListener { MusicPlayerRemote.cycleRepeatMode() }
    }

    override fun updateRepeatState() {
        when (MusicPlayerRemote.repeatMode) {
            MusicService.REPEAT_MODE_NONE -> {
                playerRepeatButton.setImageResource(code.name.monkey.retromusic.R.drawable.ic_repeat_white_24dp)
                playerRepeatButton.setColorFilter(lastDisabledPlaybackControlsColor, PorterDuff.Mode.SRC_IN)
            }
            MusicService.REPEAT_MODE_ALL -> {
                playerRepeatButton.setImageResource(code.name.monkey.retromusic.R.drawable.ic_repeat_white_24dp)
                playerRepeatButton.setColorFilter(lastPlaybackControlsColor, PorterDuff.Mode.SRC_IN)
            }
            MusicService.REPEAT_MODE_THIS -> {
                playerRepeatButton.setImageResource(code.name.monkey.retromusic.R.drawable.ic_repeat_one_white_24dp)
                playerRepeatButton.setColorFilter(lastPlaybackControlsColor, PorterDuff.Mode.SRC_IN)
            }
        }
    }

    override fun setUpProgressSlider() {
        val color = MaterialValueHelper.getPrimaryTextColor(context, false)
        playerProgressSlider.thumb.mutate().setColorFilter(color, PorterDuff.Mode.SRC_IN)
        playerProgressSlider.progressDrawable.mutate().setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.SRC_IN)

        playerProgressSlider.setOnSeekBarChangeListener(object : SimpleOnSeekbarChangeListener() {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    MusicPlayerRemote.seekTo(progress)
                    onUpdateProgressViews(MusicPlayerRemote.songProgressMillis, MusicPlayerRemote.songDurationMillis)
                }
            }
        })
    }

    override fun onUpdateProgressViews(progress: Int, total: Int) {
        playerProgressSlider.max = total

        val animator = ObjectAnimator.ofInt(playerProgressSlider, "progress", progress)
        animator.duration = SLIDER_ANIMATION_TIME
        animator.interpolator = LinearInterpolator()
        animator.start()


        playerSongTotalTime.text = MusicUtil.getReadableDurationString(total.toLong())
        playerSongCurrentProgress.text = MusicUtil.getReadableDurationString(progress.toLong())
    }

    override fun onResume() {
        super.onResume()
        progressViewUpdateHelper.start()
    }

    override fun onPause() {
        super.onPause()
        progressViewUpdateHelper.stop()
    }

    override fun onServiceConnected() {
        updatePlayPauseDrawableState(false)
        updateRepeatState()
        updateShuffleState()
    }

    override fun onPlayStateChanged() {
        updatePlayPauseDrawableState(true)
    }

    override fun onRepeatModeChanged() {
        updateRepeatState()
    }

    override fun onShuffleModeChanged() {
        updateShuffleState()
    }

    fun updatePlayPauseDrawableState(animate: Boolean) {
        if (MusicPlayerRemote.isPlaying) {
            playerFabPlayPauseDrawable?.setPause(animate)
        } else {
            playerFabPlayPauseDrawable?.setPlay(animate)
        }
    }

}