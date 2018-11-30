package code.name.monkey.retromusic.ui.fragments.player.blur

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
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder
import code.name.monkey.appthemehelper.util.TintHelper
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.helper.MusicPlayerRemote
import code.name.monkey.retromusic.helper.MusicProgressViewUpdateHelper
import code.name.monkey.retromusic.helper.PlayPauseButtonOnClickHandler
import code.name.monkey.retromusic.misc.SimpleOnSeekbarChangeListener
import code.name.monkey.retromusic.service.MusicService
import code.name.monkey.retromusic.ui.fragments.VolumeFragment
import code.name.monkey.retromusic.ui.fragments.base.AbsPlayerControlsFragment
import code.name.monkey.retromusic.util.MusicUtil
import code.name.monkey.retromusic.util.PreferenceUtil


class BlurPlaybackControlsFragment : AbsPlayerControlsFragment() {

    lateinit var songTitle: AppCompatTextView
    lateinit var text: TextView

    private var lastPlaybackControlsColor: Int = 0
    private var lastDisabledPlaybackControlsColor: Int = 0
    private var progressViewUpdateHelper: MusicProgressViewUpdateHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        progressViewUpdateHelper = MusicProgressViewUpdateHelper(this)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_blur_playback_controls, container, false)
        songTitle = view.findViewById(R.id.title)
        text = view.findViewById(R.id.text)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpMusicControllers()

        volumeContainer!!.visibility = if (PreferenceUtil.getInstance().volumeToggle) View.VISIBLE else View.GONE
        val mVolumeFragment = childFragmentManager.findFragmentById(R.id.volume_fragment) as VolumeFragment
        mVolumeFragment.tintWhiteColor()
    }

    private fun updateSong() {
        val song = MusicPlayerRemote.currentSong
        songTitle.text = song.title
        text.text = song.artistName
    }

    override fun onResume() {
        super.onResume()
        progressViewUpdateHelper!!.start()
    }

    override fun onPause() {
        super.onPause()
        progressViewUpdateHelper!!.stop()
    }

    override fun onServiceConnected() {
        updatePlayPauseDrawableState()
        updateRepeatState()
        updateShuffleState()
        updateSong()
    }

    override fun onPlayingMetaChanged() {
        super.onPlayingMetaChanged()
        updateSong()
    }

    override fun onPlayStateChanged() {
        updatePlayPauseDrawableState()
    }

    override fun onRepeatModeChanged() {
        updateRepeatState()
    }

    override fun onShuffleModeChanged() {
        updateShuffleState()
    }

    override fun setDark(color: Int) {
        lastPlaybackControlsColor = Color.WHITE
        lastDisabledPlaybackControlsColor = ContextCompat.getColor(context!!, R.color.md_grey_500)

        songTitle.setTextColor(lastPlaybackControlsColor)
        text.setTextColor(lastDisabledPlaybackControlsColor)

        setProgressBarColor()

        songCurrentProgress!!.setTextColor(lastPlaybackControlsColor)
        songTotalTime!!.setTextColor(lastPlaybackControlsColor)

        updateRepeatState()
        updateShuffleState()
        updatePrevNextColor()
    }

    private fun setProgressBarColor() {
        TintHelper.setTintAuto(progressSlider!!, Color.WHITE, false)
    }

    private fun setUpPlayPauseFab() {
        TintHelper.setTintAuto(playPauseFab!!, Color.WHITE, true)
        TintHelper.setTintAuto(playPauseFab!!, Color.BLACK, false)
        playPauseFab!!.setOnClickListener(PlayPauseButtonOnClickHandler())
    }

    protected fun updatePlayPauseDrawableState() {
        if (MusicPlayerRemote.isPlaying) {
            playPauseFab!!.setImageResource(R.drawable.ic_pause_white_24dp)
        } else {
            playPauseFab!!.setImageResource(R.drawable.ic_play_arrow_white_24dp)
        }
    }


    private fun setUpMusicControllers() {
        setUpPlayPauseFab()
        setUpPrevNext()
        setUpRepeatButton()
        setUpShuffleButton()
        setUpProgressSlider()
    }

    private fun setUpPrevNext() {
        updatePrevNextColor()
        nextButton!!.setOnClickListener { MusicPlayerRemote.playNextSong() }
        prevButton!!.setOnClickListener { MusicPlayerRemote.back() }
    }

    private fun updatePrevNextColor() {
        nextButton!!.setColorFilter(lastPlaybackControlsColor, PorterDuff.Mode.SRC_IN)
        prevButton!!.setColorFilter(lastPlaybackControlsColor, PorterDuff.Mode.SRC_IN)
    }

    private fun setUpShuffleButton() {
        shuffleButton!!.setOnClickListener { MusicPlayerRemote.toggleShuffleMode() }
    }

    override fun updateShuffleState() {
        when (MusicPlayerRemote.shuffleMode) {
            MusicService.SHUFFLE_MODE_SHUFFLE -> shuffleButton!!.setColorFilter(lastPlaybackControlsColor, PorterDuff.Mode.SRC_IN)
            else -> shuffleButton!!.setColorFilter(lastDisabledPlaybackControlsColor, PorterDuff.Mode.SRC_IN)
        }
    }

    private fun setUpRepeatButton() {
        repeatButton!!.setOnClickListener { MusicPlayerRemote.cycleRepeatMode() }
    }

    override fun updateRepeatState() {
        when (MusicPlayerRemote.repeatMode) {
            MusicService.REPEAT_MODE_NONE -> {
                repeatButton!!.setImageResource(R.drawable.ic_repeat_white_24dp)
                repeatButton!!.setColorFilter(lastDisabledPlaybackControlsColor, PorterDuff.Mode.SRC_IN)
            }
            MusicService.REPEAT_MODE_ALL -> {
                repeatButton!!.setImageResource(R.drawable.ic_repeat_white_24dp)
                repeatButton!!.setColorFilter(lastPlaybackControlsColor, PorterDuff.Mode.SRC_IN)
            }
            MusicService.REPEAT_MODE_THIS -> {
                repeatButton!!.setImageResource(R.drawable.ic_repeat_one_white_24dp)
                repeatButton!!.setColorFilter(lastPlaybackControlsColor, PorterDuff.Mode.SRC_IN)
            }
        }
    }


    override fun show() {
        playPauseFab!!.animate()
                .scaleX(1f)
                .scaleY(1f)
                .rotation(360f)
                .setInterpolator(DecelerateInterpolator())
                .start()
    }

    override fun hide() {
        if (playPauseFab != null) {
            playPauseFab!!.scaleX = 0f
            playPauseFab!!.scaleY = 0f
            playPauseFab!!.rotation = 0f
        }
    }

    override fun setUpProgressSlider() {
        progressSlider!!.setOnSeekBarChangeListener(object : SimpleOnSeekbarChangeListener() {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    MusicPlayerRemote.seekTo(progress)
                    onUpdateProgressViews(MusicPlayerRemote.songProgressMillis, MusicPlayerRemote.songDurationMillis)
                }
            }
        })
    }

    override fun onUpdateProgressViews(progress: Int, total: Int) {
        progressSlider!!.max = total

        val animator = ObjectAnimator.ofInt(progressSlider, "progress", progress)
        animator.duration = 1500
        animator.interpolator = LinearInterpolator()
        animator.start()

        songTotalTime!!.text = MusicUtil.getReadableDurationString(total.toLong())
        songCurrentProgress!!.text = MusicUtil.getReadableDurationString(progress.toLong())
    }
}
