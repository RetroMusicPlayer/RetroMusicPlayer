package code.name.monkey.retromusic.ui.fragments.player.blur

import android.graphics.Color
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import code.name.monkey.appthemehelper.util.ToolbarContentTintHelper
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.glide.BlurTransformation
import code.name.monkey.retromusic.glide.RetroMusicColoredTarget
import code.name.monkey.retromusic.glide.SongGlideRequest
import code.name.monkey.retromusic.helper.MusicPlayerRemote
import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.ui.adapter.song.PlayingQueueAdapter
import code.name.monkey.retromusic.ui.fragments.base.AbsPlayerFragment
import code.name.monkey.retromusic.ui.fragments.player.PlayerAlbumCoverFragment
import com.bumptech.glide.Glide
import com.h6ah4i.android.widget.advrecyclerview.animator.RefactoredDefaultItemAnimator

class BlurPlayerFragment : AbsPlayerFragment() {
    lateinit var playbackControlsFragment: BlurPlaybackControlsFragment

    private var colorBackground: ImageView? = null
    private var lastColor: Int = 0
    private var playingQueueAdapter: PlayingQueueAdapter? = null
    private var layoutManager: LinearLayoutManager? = null
    private var recyclerView: RecyclerView? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_blur, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpSubFragments()
        setUpPlayerToolbar()
    }

    private fun setUpSubFragments() {
        playbackControlsFragment = childFragmentManager.findFragmentById(R.id.playback_controls_fragment) as BlurPlaybackControlsFragment
        val playerAlbumCoverFragment = childFragmentManager.findFragmentById(R.id.player_album_cover_fragment) as PlayerAlbumCoverFragment
        playerAlbumCoverFragment.setCallbacks(this)
    }

    private fun setUpPlayerToolbar() {
        toolbar!!.apply {
            inflateMenu(R.menu.menu_player)
            setNavigationOnClickListener { activity!!.onBackPressed() }
            ToolbarContentTintHelper.colorizeToolbar(this, Color.WHITE, activity)
        }.setOnMenuItemClickListener(this)
    }

    override fun onFavoriteToggled() {
        toggleFavorite(MusicPlayerRemote.currentSong)
    }

    override fun onColorChanged(color: Int) {
        playbackControlsFragment.setDark(color)
        lastColor = color
        callbacks!!.onPaletteColorChanged()
        ToolbarContentTintHelper.colorizeToolbar(toolbar!!, Color.WHITE, activity)
    }

    override fun toggleFavorite(song: Song) {
        super.toggleFavorite(song)
        if (song.id == MusicPlayerRemote.currentSong.id) {
            updateIsFavorite()
        }
    }

    override fun onShow() {

    }

    override fun onHide() {

    }

    override fun onBackPressed(): Boolean {
        return false
    }

    override fun toolbarIconColor(): Int {
        return Color.WHITE
    }

    override val paletteColor: Int
        get() = lastColor

    override fun onDestroyView() {
        recyclerView!!.apply {
            itemAnimator = null
            adapter = null
        }
        playingQueueAdapter = null
        layoutManager = null
        super.onDestroyView()
    }

    private fun updateBlur() {
        val activity = activity ?: return

        val blurAmount = PreferenceManager.getDefaultSharedPreferences(context).getInt("new_blur_amount", 25)

        colorBackground!!.clearColorFilter()

        SongGlideRequest.Builder.from(Glide.with(activity), MusicPlayerRemote.currentSong)
                .checkIgnoreMediaStore(activity)
                .generatePalette(activity)
                .build()
                .override(320, 480)
                .transform(BlurTransformation.Builder(getActivity()!!).blurRadius(blurAmount.toFloat()).build())
                .into(object : RetroMusicColoredTarget(colorBackground!!) {
                    override fun onColorReady(color: Int) {
                        if (color == defaultFooterColor) {
                            colorBackground!!.setColorFilter(color)
                        }
                    }
                })
    }

    override fun onServiceConnected() {
        updateIsFavorite()
        updateBlur()
        setUpRecyclerView()
    }

    override fun onPlayingMetaChanged() {
        updateIsFavorite()
        updateBlur()
        updateQueuePosition()
    }

    private fun setUpRecyclerView() {
        if (recyclerView != null) {
            val animator = RefactoredDefaultItemAnimator()

            playingQueueAdapter = PlayingQueueAdapter((activity as AppCompatActivity),
                    MusicPlayerRemote.playingQueue,
                    MusicPlayerRemote.position,
                    R.layout.item_song,
                    Color.WHITE)
            layoutManager = LinearLayoutManager(context)

            recyclerView!!.apply {
                layoutManager = layoutManager
                adapter = playingQueueAdapter
                itemAnimator = animator
            }
            layoutManager!!.scrollToPositionWithOffset(MusicPlayerRemote.position + 1, 0)
        }
    }

    override fun onQueueChanged() {
        updateQueue()
        updateCurrentSong()
    }

    override fun onMediaStoreChanged() {
        updateQueue()
        updateCurrentSong()
    }

    private fun updateCurrentSong() {}

    private fun updateQueuePosition() {

        playingQueueAdapter!!.apply {
            setCurrent(MusicPlayerRemote.position)
            resetToCurrentPosition()
        }

    }

    private fun updateQueue() {
        playingQueueAdapter!!.apply {
            swapDataSet(MusicPlayerRemote.playingQueue, MusicPlayerRemote.position)
            resetToCurrentPosition()
        }

    }

    private fun resetToCurrentPosition() {
        recyclerView!!.apply {
            stopScroll()
        }
        layoutManager!!.scrollToPositionWithOffset(MusicPlayerRemote.position + 1, 0)
    }
}

