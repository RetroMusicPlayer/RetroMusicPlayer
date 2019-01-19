package code.name.monkey.retromusic.ui.fragments.player.normal

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import code.name.monkey.appthemehelper.util.ATHUtil
import code.name.monkey.appthemehelper.util.ToolbarContentTintHelper
import code.name.monkey.retromusic.helper.MusicPlayerRemote
import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.ui.fragments.base.AbsPlayerFragment
import code.name.monkey.retromusic.ui.fragments.player.PlayerAlbumCoverFragment
import code.name.monkey.retromusic.util.PreferenceUtil
import code.name.monkey.retromusic.util.ViewUtil
import code.name.monkey.retromusic.views.DrawableGradient
import kotlinx.android.synthetic.main.fragment_player.*


class PlayerFragment : AbsPlayerFragment(), PlayerAlbumCoverFragment.Callbacks {

    private var lastColor: Int = 0
    override val paletteColor: Int
        get() = lastColor

    private lateinit var playbackControlsFragment: PlayerPlaybackControlsFragment
    private var valueAnimator: ValueAnimator? = null


    private fun colorize(i: Int) {
        if (valueAnimator != null) {
            valueAnimator!!.cancel()
        }

        valueAnimator = ValueAnimator.ofObject(ArgbEvaluator(), android.R.color.transparent, i)
        valueAnimator!!.addUpdateListener { animation ->
            val drawable = DrawableGradient(GradientDrawable.Orientation.TOP_BOTTOM, intArrayOf(animation.animatedValue as Int, android.R.color.transparent), 0)
            colorGradientBackground?.background = drawable
        }
        valueAnimator!!.setDuration(ViewUtil.RETRO_MUSIC_ANIM_TIME.toLong()).start()
    }

    override fun onShow() {
        playbackControlsFragment.show()
    }

    override fun onHide() {
        playbackControlsFragment.hide()
        onBackPressed()
    }

    override fun onBackPressed(): Boolean {
        return false
    }

    override fun toolbarIconColor(): Int {
        return ATHUtil.resolveColor(context, code.name.monkey.retromusic.R.attr.iconColor)
    }

    override fun onColorChanged(color: Int) {
        playbackControlsFragment.setDark(color)
        lastColor = color
        callbacks!!.onPaletteColorChanged()

        ToolbarContentTintHelper.colorizeToolbar(playerToolbar, ATHUtil.resolveColor(context, code.name.monkey.retromusic.R.attr.iconColor), activity)

        if (PreferenceUtil.getInstance().adaptiveColor) {
            colorize(color)
        }
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


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(code.name.monkey.retromusic.R.layout.fragment_player, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpSubFragments()
        setUpPlayerToolbar()
        snowfall.visibility = if (PreferenceUtil.getInstance().isSnowFall) View.VISIBLE else View.GONE
    }

    private fun setUpSubFragments() {
        playbackControlsFragment = childFragmentManager.findFragmentById(code.name.monkey.retromusic.R.id.playbackControlsFragment) as PlayerPlaybackControlsFragment
        val playerAlbumCoverFragment = childFragmentManager.findFragmentById(code.name.monkey.retromusic.R.id.playerAlbumCoverFragment) as PlayerAlbumCoverFragment
        playerAlbumCoverFragment.setCallbacks(this)
    }

    private fun setUpPlayerToolbar() {
        playerToolbar.inflateMenu(code.name.monkey.retromusic.R.menu.menu_player)
        playerToolbar.setNavigationOnClickListener { activity!!.onBackPressed() }
        playerToolbar.setOnMenuItemClickListener(this)

        ToolbarContentTintHelper.colorizeToolbar(playerToolbar, ATHUtil.resolveColor(context, code.name.monkey.retromusic.R.attr.iconColor), activity)
    }

    override fun onServiceConnected() {
        updateIsFavorite()

    }

    override fun onPlayingMetaChanged() {
        updateIsFavorite()
    }

    override fun toolbarGet(): Toolbar {
        return playerToolbar
    }

    companion object {

        fun newInstance(): PlayerFragment {
            return PlayerFragment()
        }
    }
}

