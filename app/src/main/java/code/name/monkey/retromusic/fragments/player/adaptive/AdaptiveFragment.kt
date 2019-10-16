package code.name.monkey.retromusic.fragments.player.adaptive

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import code.name.monkey.appthemehelper.ThemeStore
import code.name.monkey.appthemehelper.util.ATHUtil
import code.name.monkey.appthemehelper.util.ToolbarContentTintHelper
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.fragments.base.AbsPlayerFragment
import code.name.monkey.retromusic.fragments.player.PlayerAlbumCoverFragment
import code.name.monkey.retromusic.helper.MusicPlayerRemote
import code.name.monkey.retromusic.helper.MusicProgressViewUpdateHelper
import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.model.lyrics.AbsSynchronizedLyrics
import code.name.monkey.retromusic.model.lyrics.Lyrics
import kotlinx.android.synthetic.main.fragment_adaptive_player.*

class AdaptiveFragment : AbsPlayerFragment(), MusicProgressViewUpdateHelper.Callback {

    private lateinit var lyricsLayout: FrameLayout
    private lateinit var lyricsLine1: TextView
    private lateinit var lyricsLine2: TextView

    private var lyrics: Lyrics? = null
    private lateinit var progressViewUpdateHelper: MusicProgressViewUpdateHelper

    override fun onUpdateProgressViews(progress: Int, total: Int) {
        if (!isLyricsLayoutBound()) return

        if (!isLyricsLayoutVisible()) {
            hideLyricsLayout()
            return
        }

        if (lyrics !is AbsSynchronizedLyrics) return
        val synchronizedLyrics = lyrics as AbsSynchronizedLyrics

        lyricsLayout.visibility = View.VISIBLE
        lyricsLayout.alpha = 1f

        val oldLine = lyricsLine2.text.toString()
        val line = synchronizedLyrics.getLine(progress)

        if (oldLine != line || oldLine.isEmpty()) {
            lyricsLine1.text = oldLine
            lyricsLine2.text = line

            lyricsLine1.visibility = View.VISIBLE
            lyricsLine2.visibility = View.VISIBLE

            lyricsLine2.measure(View.MeasureSpec.makeMeasureSpec(lyricsLine2.measuredWidth, View.MeasureSpec.EXACTLY), View.MeasureSpec.UNSPECIFIED)
            val h: Float = lyricsLine2.measuredHeight.toFloat()

            lyricsLine1.alpha = 1f
            lyricsLine1.translationY = 0f
            lyricsLine1.animate().alpha(0f).translationY(-h).duration = VISIBILITY_ANIM_DURATION

            lyricsLine2.alpha = 0f
            lyricsLine2.translationY = h
            lyricsLine2.animate().alpha(1f).translationY(0f).duration = VISIBILITY_ANIM_DURATION
        }
    }

    private fun isLyricsLayoutVisible(): Boolean {
        return lyrics != null && lyrics!!.isSynchronized && lyrics!!.isValid
    }

    private fun isLyricsLayoutBound(): Boolean {
        return lyricsLayout != null && lyricsLine1 != null && lyricsLine2 != null
    }

    private fun hideLyricsLayout() {
        lyricsLayout.animate().alpha(0f).setDuration(VISIBILITY_ANIM_DURATION).withEndAction(Runnable {
            if (!isLyricsLayoutBound()) return@Runnable
            lyricsLayout.visibility = View.GONE
            lyricsLine1.text = null
            lyricsLine2.text = null
        })
    }

    override fun setLyrics(l: Lyrics?) {
        lyrics = l

        if (!isLyricsLayoutBound()) return

        if (!isLyricsLayoutVisible()) {
            hideLyricsLayout()
            return
        }

        lyricsLine1.text = null
        lyricsLine2.text = null

        lyricsLayout.visibility = View.VISIBLE
        lyricsLayout.animate().alpha(1f).duration = VISIBILITY_ANIM_DURATION
    }

    override fun onDestroyView() {
        super.onDestroyView()
        progressViewUpdateHelper.stop()
    }

    override fun playerToolbar(): Toolbar {
        return playerToolbar
    }

    private var lastColor: Int = 0
    private lateinit var playbackControlsFragment: AdaptivePlaybackControlsFragment

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_adaptive_player, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lyricsLayout = view.findViewById(R.id.player_lyrics)
        lyricsLine1 = view.findViewById(R.id.player_lyrics_line1)
        lyricsLine2 = view.findViewById(R.id.player_lyrics_line2)

        setUpSubFragments()
        setUpPlayerToolbar()

        progressViewUpdateHelper = MusicProgressViewUpdateHelper(this, 500, 1000)
        progressViewUpdateHelper.start()
    }

    private fun setUpSubFragments() {
        playbackControlsFragment = childFragmentManager.findFragmentById(R.id.playbackControlsFragment) as AdaptivePlaybackControlsFragment
        val playerAlbumCoverFragment = childFragmentManager.findFragmentById(R.id.playerAlbumCoverFragment) as PlayerAlbumCoverFragment
        playerAlbumCoverFragment.apply {
            removeSlideEffect()
            setCallbacks(this@AdaptiveFragment)
        }
    }

    private fun setUpPlayerToolbar() {
        val primaryColor = ATHUtil.resolveColor(requireContext(), R.attr.colorPrimary)
        playerToolbar.apply {
            inflateMenu(R.menu.menu_player)
            setNavigationOnClickListener { requireActivity().onBackPressed() }
            ToolbarContentTintHelper.colorizeToolbar(this, primaryColor, activity)
            setTitleTextColor(primaryColor)
            setSubtitleTextColor(ThemeStore.textColorSecondary(context!!))
            setOnMenuItemClickListener(this@AdaptiveFragment)
        }
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        updateIsFavorite()
        updateSong()
    }

    override fun onPlayingMetaChanged() {
        updateIsFavorite()
        updateSong()
    }

    private fun updateSong() {
        val song = MusicPlayerRemote.currentSong
        playerToolbar.apply {
            title = song.title
            subtitle = song.artistName
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

    override fun onColorChanged(color: Int) {
        playbackControlsFragment.setDark(color)
        lastColor = color
        callbacks!!.onPaletteColorChanged()
        ToolbarContentTintHelper.colorizeToolbar(playerToolbar, ATHUtil.resolveColor(context, R.attr.iconColor), activity)
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
        return ATHUtil.resolveColor(context, R.attr.iconColor)
    }

    override val paletteColor: Int
        get() = lastColor

}