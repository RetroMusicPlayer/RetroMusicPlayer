package code.name.monkey.retromusic.fragments.player.classic

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import code.name.monkey.appthemehelper.ThemeStore
import code.name.monkey.appthemehelper.util.*
import code.name.monkey.retromusic.CustomBottomSheetBehavior
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.activities.base.AbsSlidingMusicPanelActivity
import code.name.monkey.retromusic.adapter.song.PlayingQueueAdapter
import code.name.monkey.retromusic.extensions.hide
import code.name.monkey.retromusic.extensions.ripAlpha
import code.name.monkey.retromusic.extensions.show
import code.name.monkey.retromusic.fragments.VolumeFragment
import code.name.monkey.retromusic.fragments.base.AbsPlayerFragment
import code.name.monkey.retromusic.fragments.player.PlayerAlbumCoverFragment
import code.name.monkey.retromusic.helper.MusicPlayerRemote
import code.name.monkey.retromusic.helper.MusicProgressViewUpdateHelper
import code.name.monkey.retromusic.helper.PlayPauseButtonOnClickHandler
import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.service.MusicService
import code.name.monkey.retromusic.util.MusicUtil
import code.name.monkey.retromusic.util.PreferenceUtil
import code.name.monkey.retromusic.util.ViewUtil
import code.name.monkey.retromusic.util.color.MediaNotificationProcessor
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.card.MaterialCardView
import kotlinx.android.synthetic.main.fragment_clasic_player.*
import kotlinx.android.synthetic.main.fragment_classic_controls.*
import kotlinx.android.synthetic.main.status_bar.*

class ClassicPlayerFragment : AbsPlayerFragment(), View.OnLayoutChangeListener,
    MusicProgressViewUpdateHelper.Callback {

    private var lastColor: Int = 0
    private var lastPlaybackControlsColor: Int = 0
    private var lastDisabledPlaybackControlsColor: Int = 0
    private lateinit var progressViewUpdateHelper: MusicProgressViewUpdateHelper
    private lateinit var queueAdapter: PlayingQueueAdapter
    private var volumeFragment: VolumeFragment? = null


    private val bottomSheetCallbackList = object : BottomSheetBehavior.BottomSheetCallback() {
        override fun onSlide(bottomSheet: View, slideOffset: Float) {
            (requireActivity() as AbsSlidingMusicPanelActivity).getBottomSheetBehavior()
                .setAllowDragging(false)

            playerQueueSheet.setContentPadding(
                playerQueueSheet.contentPaddingLeft,
                (slideOffset * status_bar.height).toInt(),
                playerQueueSheet.contentPaddingRight,
                playerQueueSheet.contentPaddingBottom
            )
        }

        override fun onStateChanged(bottomSheet: View, newState: Int) {
            val activity = requireActivity() as AbsSlidingMusicPanelActivity
            if (newState == BottomSheetBehavior.STATE_EXPANDED || newState == BottomSheetBehavior.STATE_DRAGGING) {
                activity.getBottomSheetBehavior().setAllowDragging(false)
            } else {
                activity.getBottomSheetBehavior().setAllowDragging(true)
            }
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
        return inflater.inflate(R.layout.fragment_clasic_player, container, false)
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


        playerQueueSheet.setOnTouchListener { _, _ ->
            (requireActivity() as AbsSlidingMusicPanelActivity).getBottomSheetBehavior()
                .setAllowDragging(false)
            getQueuePanel().setAllowDragging(true)
            return@setOnTouchListener false
        }
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

    private fun getQueuePanel(): CustomBottomSheetBehavior<MaterialCardView> {
        playerQueueSheet as MaterialCardView
        return CustomBottomSheetBehavior.from(playerQueueSheet) as CustomBottomSheetBehavior<MaterialCardView>
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
        recyclerView.apply {
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
        return Color.WHITE
    }

    override val paletteColor: Int
        get() = lastColor

    override fun onColorChanged(color: MediaNotificationProcessor) {
        lastColor = color.backgroundColor
        ToolbarContentTintHelper.colorizeToolbar(
            playerToolbar,
            Color.WHITE,
            requireActivity()
        )

        if (!ATHUtil.isWindowBackgroundDark(requireContext())) {
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
            color.backgroundColor
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
        ViewUtil.setProgressDrawable(progressSlider, colorFinal, true)
        player_queue_sub_header.setTextColor(colorFinal)
        volumeFragment?.setTintable(colorFinal)
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
        progressSlider.valueTo = total.toFloat()
        progressSlider.value = progress.toFloat()
        songTotalTime.text = MusicUtil.getReadableDurationString(total.toLong())
        songCurrentProgress.text = MusicUtil.getReadableDurationString(progress.toLong())
    }

    fun setUpProgressSlider() {
        progressSlider.addOnChangeListener { _, value, fromUser ->
            if (fromUser) {
                MusicPlayerRemote.seekTo(value.toInt())
                onUpdateProgressViews(
                    MusicPlayerRemote.songProgressMillis,
                    MusicPlayerRemote.songDurationMillis
                )
            }
        }
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
        val finalHeight = height - width
        val panel = getQueuePanel()
        panel.peekHeight = finalHeight
    }
}