package code.name.monkey.retromusic.fragments.player.classic

import android.animation.Animator
import android.animation.AnimatorSet
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import code.name.monkey.appthemehelper.ThemeStore
import code.name.monkey.appthemehelper.util.ATHUtil
import code.name.monkey.appthemehelper.util.ColorUtil
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.dialogs.SongShareDialog
import code.name.monkey.retromusic.helper.MusicPlayerRemote
import code.name.monkey.retromusic.helper.menu.SongMenuHelper
import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.model.lyrics.Lyrics
import code.name.monkey.retromusic.activities.base.AbsSlidingMusicPanelActivity
import code.name.monkey.retromusic.adapter.base.MediaEntryViewHolder
import code.name.monkey.retromusic.adapter.song.PlayingQueueAdapter
import code.name.monkey.retromusic.fragments.base.AbsPlayerFragment
import code.name.monkey.retromusic.fragments.player.PlayerAlbumCoverFragment
import code.name.monkey.retromusic.util.MusicUtil
import code.name.monkey.retromusic.util.RetroUtil
import code.name.monkey.retromusic.util.ViewUtil
import code.name.monkey.retromusic.views.WidthFitSquareLayout
import com.h6ah4i.android.widget.advrecyclerview.animator.RefactoredDefaultItemAnimator
import com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager
import com.h6ah4i.android.widget.advrecyclerview.utils.WrapperAdapterUtils
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState.ANCHORED
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState.COLLAPSED
import kotlinx.android.synthetic.main.fragment_classic_player.*
import kotlinx.android.synthetic.main.fragment_classic_player_playback_controls.*


class ClassicPlayerFragment : AbsPlayerFragment(), PlayerAlbumCoverFragment.Callbacks, SlidingUpPanelLayout.PanelSlideListener {
    override fun onPanelSlide(p0: View?, p1: Float) {

    }

    override fun onPanelStateChanged(p0: View?, p1: SlidingUpPanelLayout.PanelState?, p2: SlidingUpPanelLayout.PanelState?) {
        when (p2) {
            COLLAPSED -> onPanelCollapsed(p0!!)
            ANCHORED -> playerSlidingLayout.panelState = COLLAPSED // this fixes a bug where the panel would get stuck for some reason
            else -> {

            }
        }
    }

    override fun playerToolbar(): Toolbar {
        return playerToolbar
    }

    override fun onShow() {
        classicPlaybackControlsFragment.show()
    }

    override fun onHide() {
        classicPlaybackControlsFragment.hide()
        onBackPressed()
    }

    override fun onBackPressed(): Boolean {
        var wasExpanded = false
        if (playerSlidingLayout != null) {
            wasExpanded = playerSlidingLayout.panelState === SlidingUpPanelLayout.PanelState.EXPANDED
            playerSlidingLayout.panelState = COLLAPSED
        }

        return wasExpanded
    }

    override fun toolbarIconColor(): Int {
        return Color.WHITE
    }

    override val paletteColor: Int
        get() = lastColor

    override fun onColorChanged(color: Int) {
        animateColorChange(color)
        classicPlaybackControlsFragment.setDark(ColorUtil.isColorLight(color))
        callbacks?.onPaletteColorChanged()
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


    var lastColor: Int = 0

    lateinit var classicPlaybackControlsFragment: ClassicPlayerPlaybackControlsFragment
    private var playerAlbumCoverFragment: PlayerAlbumCoverFragment? = null

    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var playingQueueAdapter: PlayingQueueAdapter
    private lateinit var wrappedAdapter: RecyclerView.Adapter<*>
    private lateinit var recyclerViewDragDropManager: RecyclerViewDragDropManager

    private var lyricsClassic: Lyrics? = null

    private lateinit var impl: Impl

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (RetroUtil.isLandscape()) {
            impl = LandscapeImpl(this)
        } else {
            impl = PortraitImpl(this)
        }

        return inflater.inflate(R.layout.fragment_classic_player, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        impl.init()

        setUpPlayerToolbar()
        setUpSubFragments()
        setUpRecyclerView()

        playerSlidingLayout.addPanelSlideListener(this)
        playerSlidingLayout.setAntiDragView(view.findViewById<View>(code.name.monkey.retromusic.R.id.draggableArea))

        view.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                view.viewTreeObserver.removeOnGlobalLayoutListener(this)
                impl.setUpPanelAndAlbumCoverHeight()
            }
        })

        // for some reason the xml attribute doesn't get applied here.
        playingQueueCard.setCardBackgroundColor(ATHUtil.resolveColor(activity, code.name.monkey.retromusic.R.attr.cardBackgroundColor))
    }

    private fun setUpPlayerToolbar() {
        playerToolbar.apply {
            inflateMenu(code.name.monkey.retromusic.R.menu.menu_player)
            setNavigationIcon(code.name.monkey.retromusic.R.drawable.ic_close_white_24dp)
            setNavigationOnClickListener { activity!!.onBackPressed() }
            setOnMenuItemClickListener(this@ClassicPlayerFragment)
        }
    }


    private fun setUpSubFragments() {
        classicPlaybackControlsFragment = childFragmentManager.findFragmentById(R.id.playbackControlsFragment) as ClassicPlayerPlaybackControlsFragment
        playerAlbumCoverFragment = childFragmentManager.findFragmentById(R.id.playerAlbumCoverFragment) as PlayerAlbumCoverFragment
        playerAlbumCoverFragment?.setCallbacks(this)

    }

    private fun setUpRecyclerView() {
        recyclerViewDragDropManager = RecyclerViewDragDropManager()
        val animator = RefactoredDefaultItemAnimator()

        playingQueueAdapter = PlayingQueueAdapter(
                activity as AppCompatActivity,
                MusicPlayerRemote.playingQueue,
                MusicPlayerRemote.position,
                R.layout.item_queue)
        wrappedAdapter = recyclerViewDragDropManager.createWrappedAdapter(playingQueueAdapter)

        layoutManager = LinearLayoutManager(activity)

        playerRecyclerView.layoutManager = layoutManager
        playerRecyclerView.adapter = wrappedAdapter
        playerRecyclerView.itemAnimator = animator

        recyclerViewDragDropManager.attachRecyclerView(playerRecyclerView)

        layoutManager.scrollToPositionWithOffset(MusicPlayerRemote.position + 1, 0)
    }

    override fun onDestroyView() {
        if (playerSlidingLayout != null) {
            playerSlidingLayout.removePanelSlideListener(this)
        }
        recyclerViewDragDropManager.release()

        if (playerRecyclerView != null) {
            playerRecyclerView.itemAnimator = null
            playerRecyclerView.adapter = null
        }
        WrapperAdapterUtils.releaseAll(wrappedAdapter)
        super.onDestroyView()
    }

    override fun onPause() {
        recyclerViewDragDropManager.cancelDrag()
        super.onPause()
    }

    override fun onServiceConnected() {
        updateQueue()
        updateCurrentSong()
        updateIsFavorite()
        //updateLyrics()
    }

    override fun onPlayingMetaChanged() {
        updateCurrentSong()
        updateIsFavorite()
        updateQueuePosition()
        //updateLyrics()
    }

    override fun onQueueChanged() {
        updateQueue()
    }

    override fun onMediaStoreChanged() {
        updateQueue()
    }

    private fun updateQueue() {
        playingQueueAdapter.swapDataSet(MusicPlayerRemote.playingQueue, MusicPlayerRemote.position)
        playerQueueSubHeader.text = getUpNextAndQueueTime()
        if (playerSlidingLayout.panelState === COLLAPSED) {
            resetToCurrentPosition()
        }
    }

    private fun updateQueuePosition() {
        playingQueueAdapter.setCurrent(MusicPlayerRemote.position)
        playerQueueSubHeader.text = getUpNextAndQueueTime()
        if (playerSlidingLayout.panelState === COLLAPSED) {
            resetToCurrentPosition()
        }
    }

    private fun updateCurrentSong() {
        impl.updateCurrentSong(MusicPlayerRemote.currentSong)
    }

    private fun animateColorChange(newColor: Int) {
        impl.animateColorChange(newColor)
        lastColor = newColor
    }

    private fun onPanelCollapsed(panel: View) {
        resetToCurrentPosition()
    }

    private fun resetToCurrentPosition() {
        playerRecyclerView.stopScroll()
        layoutManager.scrollToPositionWithOffset(MusicPlayerRemote.position + 1, 0)
    }
}

abstract class BaseImpl(private val fragment: ClassicPlayerFragment) : Impl {
    fun createDefaultColorChangeAnimatorSet(color: Int): AnimatorSet {
        val backgroundAnimator: Animator
        backgroundAnimator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val x = (fragment.classicPlaybackControlsFragment.playerPlayPauseFab.x + (fragment.classicPlaybackControlsFragment.playerPlayPauseFab.width / 2).toFloat() + fragment.classicPlaybackControlsFragment.view!!.x).toInt()
            val y = (fragment.classicPlaybackControlsFragment.playerPlayPauseFab.y + (fragment.classicPlaybackControlsFragment.playerPlayPauseFab.height / 2).toFloat() + fragment.classicPlaybackControlsFragment.view!!.y + fragment.classicPlaybackControlsFragment.playerProgressSlider.height.toFloat()).toInt()
            val startRadius = Math.max(fragment.classicPlaybackControlsFragment.playerPlayPauseFab.width / 2, fragment.classicPlaybackControlsFragment.playerPlayPauseFab.height / 2).toFloat()
            val endRadius = Math.max(fragment.colorBackground.width, fragment.colorBackground.height).toFloat()
            fragment.colorBackground.setBackgroundColor(color)
            ViewAnimationUtils.createCircularReveal(fragment.colorBackground, x, y, startRadius, endRadius)
        } else {
            ViewUtil.createBackgroundColorTransition(fragment.colorBackground, fragment.lastColor, color)
        }

        val animatorSet = AnimatorSet()

        animatorSet.play(backgroundAnimator)

        if (!ATHUtil.isWindowBackgroundDark(fragment.activity!!)) {
            val adjustedLastColor = if (ColorUtil.isColorLight(fragment.lastColor)) ColorUtil.darkenColor(fragment.lastColor) else fragment.lastColor
            val adjustedNewColor = if (ColorUtil.isColorLight(color)) ColorUtil.darkenColor(color) else color
            val subHeaderAnimator = ViewUtil.createTextColorTransition(fragment.playerQueueSubHeader, adjustedLastColor, adjustedNewColor)
            animatorSet.play(subHeaderAnimator)
        }
        animatorSet.duration = ViewUtil.RETRO_MUSIC_ANIM_TIME.toLong()
        return animatorSet
    }

    override fun animateColorChange(newColor: Int) {
        if (ATHUtil.isWindowBackgroundDark(fragment.activity!!)) {
            fragment.playerQueueSubHeader.setTextColor(ThemeStore.textColorSecondary(fragment.activity!!))
        }
    }
}

class PortraitImpl(private val fragment: ClassicPlayerFragment) : BaseImpl(fragment) {
    override fun init() {
        currentSongViewHolder = MediaEntryViewHolder(fragment.view?.findViewById(R.id.currentSong)!!)

        currentSongViewHolder?.apply {
            separator?.visibility = View.VISIBLE
            shortSeparator?.visibility = View.GONE
            image?.apply {
                scaleType = ImageView.ScaleType.CENTER
                setColorFilter(ATHUtil.resolveColor(fragment.activity!!, code.name.monkey.retromusic.R.attr.iconColor, ThemeStore.textColorSecondary(fragment.activity!!)), PorterDuff.Mode.SRC_IN)
                setImageResource(code.name.monkey.retromusic.R.drawable.ic_equalizer_white_24dp)

            }
            imageTextContainer?.cardElevation = 0f
            itemView.setOnClickListener {
                // toggle the panel
                if (fragment.playerSlidingLayout.panelState == COLLAPSED) {
                    fragment.playerSlidingLayout.panelState = SlidingUpPanelLayout.PanelState.EXPANDED
                } else if (fragment.playerSlidingLayout.panelState == SlidingUpPanelLayout.PanelState.EXPANDED) {
                    fragment.playerSlidingLayout.panelState = COLLAPSED
                }
            }
            menu?.setOnClickListener(object : SongMenuHelper.OnClickSongMenu((fragment.activity as AppCompatActivity?)!!) {
                override val song: Song
                    get() = currentSong

                override val menuRes: Int
                    get() = code.name.monkey.retromusic.R.menu.menu_item_playing_queue_song

                override fun onMenuItemClick(item: MenuItem): Boolean {
                    when (item.itemId) {
                        code.name.monkey.retromusic.R.id.action_remove_from_playing_queue -> {
                            MusicPlayerRemote.removeFromQueue(MusicPlayerRemote.position)
                            return true
                        }
                        code.name.monkey.retromusic.R.id.action_share -> {
                            SongShareDialog.create(song).show(fragment.fragmentManager!!, "SONG_SHARE_DIALOG")
                            return true
                        }
                    }
                    return super.onMenuItemClick(item)
                }
            })
        }
    }

    override fun updateCurrentSong(song: Song) {
        currentSong = song
        currentSongViewHolder?.apply {
            title?.text = song.title
            text?.text = MusicUtil.getSongInfoString(song)
        }
    }

    override fun animateColorChange(newColor: Int) {
        fragment.playerSlidingLayout.setBackgroundColor(fragment.lastColor)
        createDefaultColorChangeAnimatorSet(newColor).start()
    }

    override fun setUpPanelAndAlbumCoverHeight() {
        val albumCoverContainer = fragment.view!!.findViewById<WidthFitSquareLayout>(R.id.albumCoverContainer)
        val availablePanelHeight = fragment.playerSlidingLayout.height - fragment.view!!.findViewById<View>(R.id.playerContent).height + ViewUtil.convertDpToPixel(8f, fragment.resources).toInt()
        val minPanelHeight = ViewUtil.convertDpToPixel(72f + 24f, fragment.resources).toInt()

        if (availablePanelHeight < minPanelHeight) {
            albumCoverContainer.layoutParams.height = albumCoverContainer.height - (minPanelHeight - availablePanelHeight)
            albumCoverContainer.forceSquare(false)
        }
        fragment.playerSlidingLayout.panelHeight = Math.max(minPanelHeight, availablePanelHeight)

        (fragment.activity as AbsSlidingMusicPanelActivity).setAntiDragView(fragment.playerSlidingLayout.findViewById<View>(R.id.playerPanel))

    }

    private var currentSongViewHolder: MediaEntryViewHolder? = null
    var currentSong = Song.emptySong

}

class LandscapeImpl(private val fragment: ClassicPlayerFragment) : BaseImpl(fragment) {
    override fun init() {

    }

    override fun updateCurrentSong(song: Song) {
        fragment.playerToolbar.title = song.title
        fragment.playerToolbar.subtitle = MusicUtil.getSongInfoString(song)
    }

    override fun animateColorChange(newColor: Int) {
        fragment.playerSlidingLayout.setBackgroundColor(fragment.lastColor)

        val animatorSet = createDefaultColorChangeAnimatorSet(newColor)
        animatorSet.play(ViewUtil.createBackgroundColorTransition(fragment.playerToolbar, fragment.lastColor, newColor)).with(ViewUtil.createBackgroundColorTransition(fragment.view?.findViewById(R.id.status_bar)!!, ColorUtil.darkenColor(fragment.lastColor), ColorUtil.darkenColor(newColor)))
        animatorSet.start()
    }

    override fun setUpPanelAndAlbumCoverHeight() {
        val panelHeight = fragment.playerSlidingLayout.height - fragment.classicPlaybackControlsFragment.view?.height!!
        fragment.playerSlidingLayout.panelHeight = panelHeight
        (fragment.activity as AbsSlidingMusicPanelActivity).setAntiDragView(fragment.playerSlidingLayout.findViewById(R.id.playerPanel))

    }
}

internal interface Impl {
    fun init()

    fun updateCurrentSong(song: Song)

    fun animateColorChange(newColor: Int)

    fun setUpPanelAndAlbumCoverHeight()
}