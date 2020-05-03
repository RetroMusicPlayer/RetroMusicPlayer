package code.name.monkey.retromusic.fragments.player

import android.animation.ObjectAnimator
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import code.name.monkey.appthemehelper.ThemeStore
import code.name.monkey.appthemehelper.util.*
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.adapter.song.PlayingQueueAdapter
import code.name.monkey.retromusic.extensions.hide
import code.name.monkey.retromusic.extensions.ripAlpha
import code.name.monkey.retromusic.extensions.show
import code.name.monkey.retromusic.fragments.VolumeFragment
import code.name.monkey.retromusic.fragments.base.AbsPlayerControlsFragment
import code.name.monkey.retromusic.fragments.base.AbsPlayerFragment
import code.name.monkey.retromusic.helper.MusicPlayerRemote
import code.name.monkey.retromusic.helper.MusicProgressViewUpdateHelper
import code.name.monkey.retromusic.helper.PlayPauseButtonOnClickHandler
import code.name.monkey.retromusic.misc.SimpleOnSeekbarChangeListener
import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.service.MusicService
import code.name.monkey.retromusic.util.MusicUtil
import code.name.monkey.retromusic.util.PreferenceUtil
import code.name.monkey.retromusic.util.ViewUtil
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.card.MaterialCardView
import kotlinx.android.synthetic.main.fragment_full_player.*
import kotlinx.android.synthetic.main.fragment_layout_test.*

class TestPlayerFragment : AbsPlayerFragment(), View.OnLayoutChangeListener,
    MusicProgressViewUpdateHelper.Callback {

    private var lastColor: Int = 0
    private var lastPlaybackControlsColor: Int = 0
    private var lastDisabledPlaybackControlsColor: Int = 0
    private lateinit var progressViewUpdateHelper: MusicProgressViewUpdateHelper
    private lateinit var queueAdapter: PlayingQueueAdapter
    private var volumeFragment: VolumeFragment? = null

    private val bottomSheetCallbackList = object : BottomSheetBehavior.BottomSheetCallback() {
        override fun onSlide(bottomSheet: View, slideOffset: Float) {
            handle.alpha = 1 - slideOffset
        }

        override fun onStateChanged(bottomSheet: View, newState: Int) {

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        progressViewUpdateHelper = MusicProgressViewUpdateHelper(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_full_player, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupPanel()
        setUpMusicControllers()
        setUpPlayerToolbar()
        hideVolumeIfAvailable()
        setUpQueue()
        val coverFragment =
            childFragmentManager.findFragmentById(R.id.playerAlbumCoverFragment) as PlayerAlbumCoverFragment
        coverFragment.setCallbacks(this)

        getQueuePanel().addBottomSheetCallback(bottomSheetCallbackList)
    }

    private fun hideVolumeIfAvailable() {
        if (PreferenceUtil.getInstance(requireContext()).volumeToggle) {
            childFragmentManager.beginTransaction()
                .replace(R.id.volumeFragmentContainer, VolumeFragment.newInstance())
                .commit()
            childFragmentManager.executePendingTransactions()
            volumeFragment =
                childFragmentManager.findFragmentById(R.id.volumeFragmentContainer) as VolumeFragment?
        }
    }

    private fun updateSong() {
        val song = MusicPlayerRemote.currentSong
        title.text = song.title
        text.text = song.artistName

        if (PreferenceUtil.getInstance(requireContext()).isSongInfo) {
            songInfo.text = getSongInfo(song)
            songInfo.show()
        } else {
            songInfo.hide()
        }
    }

    override fun onResume() {
        super.onResume()
        progressViewUpdateHelper.start()
    }

    override fun onPause() {
        super.onPause()
        progressViewUpdateHelper.stop()
    }

    private fun getQueuePanel(): BottomSheetBehavior<MaterialCardView> {
        playerQueueSheet as MaterialCardView
        return BottomSheetBehavior.from(playerQueueSheet)
    }

    private fun setupPanel() {
        if (!ViewCompat.isLaidOut(playerContainer) || playerContainer.isLayoutRequested) {
            playerContainer.addOnLayoutChangeListener(this)
            return
        }
        val height = playerContainer.height
        val width = playerContainer.width
        val finalHeight = height - (playerControlsContainer.height + width)
        val panel = getQueuePanel()
        panel.peekHeight = finalHeight
    }

    private fun setUpPlayerToolbar() {
        playerToolbar.inflateMenu(R.menu.menu_player)
        playerToolbar.setNavigationOnClickListener { requireActivity().onBackPressed() }
        playerToolbar.setOnMenuItemClickListener(this)

        ToolbarContentTintHelper.colorizeToolbar(
            playerToolbar,
            ATHUtil.resolveColor(requireContext(), R.attr.colorControlNormal),
            requireActivity()
        )
    }


    private fun setUpQueue() {
        queueAdapter = PlayingQueueAdapter(
            requireActivity() as AppCompatActivity, mutableListOf(),
            MusicPlayerRemote.position,
            R.layout.item_queue
        )
        playerQueueRecyclerView.apply {
            adapter = queueAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        updateSong()
        queueAdapter.swapDataSet(MusicPlayerRemote.playingQueue)
    }

    override fun onPlayingMetaChanged() {
        super.onPlayingMetaChanged()
        updateSong()
    }


    override fun onQueueChanged() {
        super.onQueueChanged()
        queueAdapter.swapDataSet(MusicPlayerRemote.playingQueue)
    }


    override fun playerToolbar(): Toolbar? {
        return playerToolbar
    }

    override fun onShow() {

    }

    override fun onHide() {

    }

    override fun onBackPressed(): Boolean {
        if (getQueuePanel().state == BottomSheetBehavior.STATE_EXPANDED) {
            getQueuePanel().state = BottomSheetBehavior.STATE_COLLAPSED
        }
        return false
    }

    override fun toolbarIconColor(): Int {
        return ATHUtil.resolveColor(requireContext(), R.attr.colorControlNormal)
    }

    override val paletteColor: Int
        get() = lastColor

    override fun onColorChanged(color: Int) {
        playerContainer.setBackgroundColor(color)
        val colorBg = ATHUtil.resolveColor(requireContext(), color)
        if (ColorUtil.isColorLight(colorBg)) {
            lastPlaybackControlsColor =
                MaterialValueHelper.getSecondaryTextColor(requireContext(), true)
            lastDisabledPlaybackControlsColor =
                MaterialValueHelper.getSecondaryDisabledTextColor(requireContext(), true)
        } else {
            lastPlaybackControlsColor =
                MaterialValueHelper.getPrimaryTextColor(requireContext(), false)
            lastDisabledPlaybackControlsColor =
                MaterialValueHelper.getPrimaryDisabledTextColor(requireContext(), false)
        }

        val colorFinal = if (PreferenceUtil.getInstance(requireContext()).adaptiveColor) {
            color
        } else {
            ThemeStore.accentColor(requireContext())
        }.ripAlpha()

        TintHelper.setTintAuto(
            playPauseButton,
            MaterialValueHelper.getPrimaryTextColor(
                requireContext(),
                ColorUtil.isColorLight(colorFinal)
            ),
            false
        )
        title.setTextColor(lastPlaybackControlsColor)
        text.setTextColor(lastDisabledPlaybackControlsColor)
        songInfo.setTextColor(lastDisabledPlaybackControlsColor)
        songCurrentProgress.setTextColor(lastPlaybackControlsColor)
        songTotalTime.setTextColor(lastPlaybackControlsColor)
        volumeFragment?.setTintableColor(lastPlaybackControlsColor)
        TintHelper.setTintAuto(playPauseButton, colorFinal, true)
        ViewUtil.setProgressDrawable(progressSlider, colorFinal, true)
        updateRepeatState()
        updateShuffleState()
        updatePrevNextColor()
    }

    override fun toggleFavorite(song: Song) {
        super.toggleFavorite(song)
        if (song.id == MusicPlayerRemote.currentSong.id) {
            updateIsFavorite()
        }
    }

    override fun onFavoriteToggled() {
        toggleFavorite(MusicPlayerRemote.currentSong)
    }

    override fun onUpdateProgressViews(progress: Int, total: Int) {
        progressSlider.max = total

        val animator = ObjectAnimator.ofInt(progressSlider, "progress", progress)
        animator.duration = AbsPlayerControlsFragment.SLIDER_ANIMATION_TIME
        animator.interpolator = LinearInterpolator()
        animator.start()

        songTotalTime.text = MusicUtil.getReadableDurationString(total.toLong())
        songCurrentProgress.text = MusicUtil.getReadableDurationString(progress.toLong())
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

    private fun setUpPlayPauseFab() {
        playPauseButton.setOnClickListener(PlayPauseButtonOnClickHandler())
    }

    private fun updatePlayPauseDrawableState() {
        if (MusicPlayerRemote.isPlaying) {
            playPauseButton.setImageResource(R.drawable.ic_pause_white_24dp)
        } else {
            playPauseButton.setImageResource(R.drawable.ic_play_arrow_white_24dp)
        }
    }

    private fun setUpMusicControllers() {
        setUpPlayPauseFab()
        setUpPrevNext()
        setUpRepeatButton()
        setUpShuffleButton()
        setUpProgressSlider()
    }

    fun setUpProgressSlider() {
        progressSlider.setOnSeekBarChangeListener(object : SimpleOnSeekbarChangeListener() {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    MusicPlayerRemote.seekTo(progress)
                    onUpdateProgressViews(
                        MusicPlayerRemote.songProgressMillis,
                        MusicPlayerRemote.songDurationMillis
                    )
                }
            }
        })
    }

    private fun setUpPrevNext() {
        updatePrevNextColor()
        nextButton.setOnClickListener { MusicPlayerRemote.playNextSong() }
        previousButton.setOnClickListener { MusicPlayerRemote.back() }
    }

    private fun updatePrevNextColor() {
        nextButton.setColorFilter(lastPlaybackControlsColor, PorterDuff.Mode.SRC_IN)
        previousButton.setColorFilter(lastPlaybackControlsColor, PorterDuff.Mode.SRC_IN)
    }

    private fun setUpShuffleButton() {
        shuffleButton.setOnClickListener { MusicPlayerRemote.toggleShuffleMode() }
    }

    fun updateShuffleState() {
        when (MusicPlayerRemote.shuffleMode) {
            MusicService.SHUFFLE_MODE_SHUFFLE -> shuffleButton.setColorFilter(
                lastPlaybackControlsColor,
                PorterDuff.Mode.SRC_IN
            )
            else -> shuffleButton.setColorFilter(
                lastDisabledPlaybackControlsColor,
                PorterDuff.Mode.SRC_IN
            )
        }
    }

    private fun setUpRepeatButton() {
        repeatButton.setOnClickListener { MusicPlayerRemote.cycleRepeatMode() }
    }

    fun updateRepeatState() {
        when (MusicPlayerRemote.repeatMode) {
            MusicService.REPEAT_MODE_NONE -> {
                repeatButton.setImageResource(R.drawable.ic_repeat_white_24dp)
                repeatButton.setColorFilter(
                    lastDisabledPlaybackControlsColor,
                    PorterDuff.Mode.SRC_IN
                )
            }
            MusicService.REPEAT_MODE_ALL -> {
                repeatButton.setImageResource(R.drawable.ic_repeat_white_24dp)
                repeatButton.setColorFilter(lastPlaybackControlsColor, PorterDuff.Mode.SRC_IN)
            }
            MusicService.REPEAT_MODE_THIS -> {
                repeatButton.setImageResource(R.drawable.ic_repeat_one_white_24dp)
                repeatButton.setColorFilter(lastPlaybackControlsColor, PorterDuff.Mode.SRC_IN)
            }
        }
    }

    override fun onLayoutChange(
        v: View?,
        left: Int,
        top: Int,
        right: Int,
        bottom: Int,
        oldLeft: Int,
        oldTop: Int,
        oldRight: Int,
        oldBottom: Int
    ) {
        val height = playerContainer.height
        val width = playerContainer.width
        val finalHeight = height - (playerControlsContainer.height + width)
        val panel = getQueuePanel()
        panel.peekHeight = finalHeight
    }
}