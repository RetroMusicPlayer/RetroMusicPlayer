package code.name.monkey.retromusic.fragments.player.simple

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import code.name.monkey.appthemehelper.util.ATHUtil
import code.name.monkey.appthemehelper.util.ToolbarContentTintHelper
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.helper.MusicPlayerRemote
import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.fragments.base.AbsPlayerFragment
import code.name.monkey.retromusic.fragments.player.PlayerAlbumCoverFragment
import kotlinx.android.synthetic.main.fragment_simple_player.*

/**
 * @author Hemanth S (h4h13).
 */

class SimplePlayerFragment : AbsPlayerFragment()  {
    override fun playerToolbar(): Toolbar {
        return playerToolbar
    }

    private var lastColor: Int = 0
    override val paletteColor: Int
        get() = lastColor

    private lateinit var simplePlaybackControlsFragment: SimplePlaybackControlsFragment

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_simple_player, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpSubFragments()
        setUpPlayerToolbar()
    }

    private fun setUpSubFragments() {
        val playerAlbumCoverFragment = childFragmentManager.findFragmentById(R.id.playerAlbumCoverFragment) as PlayerAlbumCoverFragment
        playerAlbumCoverFragment.setCallbacks(this)
        simplePlaybackControlsFragment = childFragmentManager.findFragmentById(R.id.playbackControlsFragment) as SimplePlaybackControlsFragment

    }

    override fun onShow() {
        simplePlaybackControlsFragment.show()
    }

    override fun onHide() {
        simplePlaybackControlsFragment.hide()
    }

    override fun onBackPressed(): Boolean {
        return false
    }

    override fun toolbarIconColor(): Int {
        return ATHUtil.resolveColor(context!!, R.attr.iconColor)
    }

    override fun onColorChanged(color: Int) {
        lastColor = color
        callbacks!!.onPaletteColorChanged()
        simplePlaybackControlsFragment.setDark(color)
        ToolbarContentTintHelper.colorizeToolbar(playerToolbar, ATHUtil.resolveColor(context!!, R.attr.iconColor), activity)

    }

    override fun onFavoriteToggled() {
        toggleFavorite(MusicPlayerRemote.currentSong)
    }

    override fun toggleFavorite(song: Song) {
        super.toggleFavorite(song)
        if (song.id == MusicPlayerRemote.currentSong.id) {
            updateIsFavorite()
        }
    }

    private fun setUpPlayerToolbar() {
        playerToolbar.apply {
            inflateMenu(R.menu.menu_player)
            setNavigationOnClickListener { activity!!.onBackPressed() }
            setOnMenuItemClickListener(this@SimplePlayerFragment)
            ToolbarContentTintHelper.colorizeToolbar(this, ATHUtil.resolveColor(context, R.attr.iconColor), activity)
        }
    }
}
