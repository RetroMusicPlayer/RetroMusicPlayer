package code.name.monkey.retromusic.ui.fragments.player.blur

import android.graphics.Color
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import code.name.monkey.appthemehelper.util.ToolbarContentTintHelper
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.glide.BlurTransformation
import code.name.monkey.retromusic.glide.RetroMusicColoredTarget
import code.name.monkey.retromusic.glide.SongGlideRequest
import code.name.monkey.retromusic.helper.MusicPlayerRemote
import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.ui.fragments.base.AbsPlayerFragment
import code.name.monkey.retromusic.ui.fragments.player.PlayerAlbumCoverFragment
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_blur.*

class BlurPlayerFragment : AbsPlayerFragment() {
    override fun toolbarGet(): Toolbar {
        return playerToolbar
    }

    private lateinit var playbackControlsFragment: BlurPlaybackControlsFragment

    private var lastColor: Int = 0


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
        playbackControlsFragment = childFragmentManager.findFragmentById(R.id.playbackControlsFragment) as BlurPlaybackControlsFragment
        val playerAlbumCoverFragment = childFragmentManager.findFragmentById(R.id.playerAlbumCoverFragment) as PlayerAlbumCoverFragment
        playerAlbumCoverFragment.setCallbacks(this)
    }

    private fun setUpPlayerToolbar() {
        playerToolbar!!.apply {
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
        ToolbarContentTintHelper.colorizeToolbar(playerToolbar!!, Color.WHITE, activity)
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
    }

    override fun onPlayingMetaChanged() {
        updateIsFavorite()
        updateBlur()
    }
}

