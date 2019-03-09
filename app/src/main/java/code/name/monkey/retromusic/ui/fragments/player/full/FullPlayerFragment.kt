package code.name.monkey.retromusic.ui.fragments.player.full

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import code.name.monkey.appthemehelper.util.ToolbarContentTintHelper
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.extensions.hide
import code.name.monkey.retromusic.extensions.show
import code.name.monkey.retromusic.glide.GlideApp
import code.name.monkey.retromusic.glide.RetroGlideExtension
import code.name.monkey.retromusic.glide.RetroMusicColoredTarget
import code.name.monkey.retromusic.helper.MusicPlayerRemote
import code.name.monkey.retromusic.loaders.ArtistLoader
import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.ui.fragments.base.AbsPlayerFragment
import code.name.monkey.retromusic.ui.fragments.player.PlayerAlbumCoverFragment
import code.name.monkey.retromusic.util.NavigationUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_full.*

class FullPlayerFragment : AbsPlayerFragment(), PlayerAlbumCoverFragment.Callbacks {
    override fun playerToolbar(): Toolbar {
        return playerToolbar
    }

    private var lastColor: Int = 0
    override val paletteColor: Int
        get() = lastColor
    private lateinit var fullPlaybackControlsFragment: FullPlaybackControlsFragment

    private fun setUpPlayerToolbar() {
        playerToolbar.apply {
            setNavigationIcon(R.drawable.ic_close_white_24dp)
            setNavigationOnClickListener { activity!!.onBackPressed() }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_full, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpSubFragments()
        setUpPlayerToolbar()
        setupArtist()

        nextSong.isSelected = true
    }

    private fun setupArtist() {
        artistImage.setOnClickListener {
            NavigationUtil.goToArtist(activity!!, MusicPlayerRemote.currentSong.artistId)
        }
    }

    private fun setUpSubFragments() {
        fullPlaybackControlsFragment = childFragmentManager.findFragmentById(R.id.playbackControlsFragment) as FullPlaybackControlsFragment

        val playerAlbumCoverFragment = childFragmentManager.findFragmentById(R.id.playerAlbumCoverFragment) as PlayerAlbumCoverFragment
        playerAlbumCoverFragment.setCallbacks(this)
        playerAlbumCoverFragment.removeSlideEffect()
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

    override fun onColorChanged(color: Int) {
        lastColor = color
        fullPlaybackControlsFragment.setDark(color)
        callbacks!!.onPaletteColorChanged()
        ToolbarContentTintHelper.colorizeToolbar(playerToolbar, Color.WHITE, activity)
    }

    override fun onFavoriteToggled() {
        toggleFavorite(MusicPlayerRemote.currentSong)
        fullPlaybackControlsFragment.onFavoriteToggled()
    }

    override fun toggleFavorite(song: Song) {
        super.toggleFavorite(song)
        if (song.id == MusicPlayerRemote.currentSong.id) {
            updateIsFavorite()
        }
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        updateArtistImage()
        updateLabel()
    }

    override fun onPlayingMetaChanged() {
        super.onPlayingMetaChanged()
        updateArtistImage()
        updateLabel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable.dispose()
    }

    private val compositeDisposable = CompositeDisposable()

    private fun updateArtistImage() {
        compositeDisposable.addAll(ArtistLoader.getArtist(context!!, MusicPlayerRemote.currentSong.artistId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    GlideApp.with(activity!!)
                            .asBitmapPalette()
                            .load(RetroGlideExtension.getArtistModel(it))
                            .transition(RetroGlideExtension.getDefaultTransition())
                            .artistOptions(it)
                            .dontAnimate()
                            .into(object : RetroMusicColoredTarget(artistImage) {
                                override fun onColorReady(color: Int) {

                                }
                            })
                })
    }

    override fun onQueueChanged() {
        super.onQueueChanged()
        if (MusicPlayerRemote.playingQueue.isNotEmpty()) updateLabel()
    }

    private fun updateLabel() {
        (MusicPlayerRemote.playingQueue.size - 1).apply {
            println("Log Position $this ${MusicPlayerRemote.position}")
            if (this == (MusicPlayerRemote.position)) {
                nextSongLabel.setText(R.string.last_song)
                nextSong.hide()
            } else {
                val title = MusicPlayerRemote.playingQueue[MusicPlayerRemote.position + 1].title
                nextSongLabel.setText(R.string.next_song)
                nextSong.apply {
                    text = title
                    show()
                }
            }
        }
    }
}
