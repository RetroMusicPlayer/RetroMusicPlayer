package code.name.monkey.retromusic.fragments.player.tiny

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.appcompat.widget.Toolbar
import code.name.monkey.appthemehelper.ThemeStore
import code.name.monkey.appthemehelper.util.ColorUtil
import code.name.monkey.appthemehelper.util.MaterialValueHelper
import code.name.monkey.appthemehelper.util.ToolbarContentTintHelper
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.fragments.MiniPlayerFragment
import code.name.monkey.retromusic.fragments.base.AbsPlayerFragment
import code.name.monkey.retromusic.fragments.player.PlayerAlbumCoverFragment
import code.name.monkey.retromusic.helper.MusicPlayerRemote
import code.name.monkey.retromusic.helper.MusicProgressViewUpdateHelper
import code.name.monkey.retromusic.helper.PlayPauseButtonOnClickHandler
import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.util.MusicUtil
import code.name.monkey.retromusic.util.PreferenceUtil
import code.name.monkey.retromusic.util.ViewUtil
import kotlinx.android.synthetic.main.fragment_tiny_player.*

class TinyPlayerFragment : AbsPlayerFragment(), MusicProgressViewUpdateHelper.Callback {
    override fun onUpdateProgressViews(progress: Int, total: Int) {
        progressBar.max = total

        val animator = ObjectAnimator.ofInt(progressBar, "progress", progress)

        val animatorSet = AnimatorSet()
        animatorSet.playSequentially(animator)

        animatorSet.duration = 1500
        animatorSet.interpolator = LinearInterpolator()
        animatorSet.start()

        playerSongTotalTime.text = String.format("%s/%s", MusicUtil.getReadableDurationString(total.toLong()),
                MusicUtil.getReadableDurationString(progress.toLong()))
    }


    override fun playerToolbar(): Toolbar {
        return playerToolbar
    }

    override fun onShow() {

    }

    override fun onHide() {
    }

    override fun onBackPressed(): Boolean {
        return false
    }

    override fun toolbarIconColor(): Int {
        return textColorPrimary
    }

    private var lastColor: Int = 0
    override val paletteColor: Int
        get() = lastColor

    private var textColorPrimary = 0
    private var textColorPrimaryDisabled = 0

    override fun onColorChanged(color: Int) {

        val colorFinal = if (PreferenceUtil.getInstance(requireContext()).adaptiveColor) {
            color
        } else {
            ThemeStore.accentColor(requireContext())
        }

        if (ColorUtil.isColorLight(colorFinal)) {
            textColorPrimary = MaterialValueHelper.getSecondaryTextColor(requireContext(), true)
            textColorPrimaryDisabled = MaterialValueHelper.getSecondaryTextColor(requireContext(), true)
        } else {
            textColorPrimary = MaterialValueHelper.getPrimaryTextColor(requireContext(), false)
            textColorPrimaryDisabled = MaterialValueHelper.getSecondaryTextColor(requireContext(), false)
        }

        this.lastColor = colorFinal

        callbacks?.onPaletteColorChanged()

        tinyPlaybackControlsFragment.setDark(colorFinal)

        ViewUtil.setProgressDrawable(progressBar, colorFinal)

        songTitle.setTextColor(textColorPrimary)
        songText.setTextColor(textColorPrimaryDisabled)

        playerSongTotalTime.setTextColor(textColorPrimary)

        ToolbarContentTintHelper.colorizeToolbar(playerToolbar, textColorPrimary, requireActivity())
    }

    override fun onFavoriteToggled() {
        toggleFavorite(MusicPlayerRemote.currentSong)
    }

    private lateinit var tinyPlaybackControlsFragment: TinyPlaybackControlsFragment
    private lateinit var progressViewUpdateHelper: MusicProgressViewUpdateHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        progressViewUpdateHelper = MusicProgressViewUpdateHelper(this)
    }

    override fun onResume() {
        super.onResume()
        progressViewUpdateHelper.start()
    }

    override fun onPause() {
        super.onPause()
        progressViewUpdateHelper.stop()
    }

    private fun updateSong() {
        val song = MusicPlayerRemote.currentSong
        songTitle.text = song.title
        songText.text = String.format("%s \nby - %s", song.albumName, song.artistName)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tiny_player, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressBar.setOnClickListener(PlayPauseButtonOnClickHandler())
        progressBar.setOnTouchListener(MiniPlayerFragment.FlingPlayBackController(activity!!))

        setUpPlayerToolbar()
        setUpSubFragments()
    }

    private fun setUpSubFragments() {
        tinyPlaybackControlsFragment = childFragmentManager.findFragmentById(R.id.playbackControlsFragment) as TinyPlaybackControlsFragment
        val playerAlbumCoverFragment = childFragmentManager.findFragmentById(R.id.playerAlbumCoverFragment) as PlayerAlbumCoverFragment
        playerAlbumCoverFragment.setCallbacks(this)

    }

    private fun setUpPlayerToolbar() {
        playerToolbar.apply {
            inflateMenu(R.menu.menu_player)
            setNavigationOnClickListener { activity!!.onBackPressed() }
            setOnMenuItemClickListener(this@TinyPlayerFragment)
        }
    }

    override fun toggleFavorite(song: Song) {
        super.toggleFavorite(song)
        if (song.id == MusicPlayerRemote.currentSong.id) {
            updateIsFavorite()
        }
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        updateSong()
    }

    override fun onPlayingMetaChanged() {
        super.onPlayingMetaChanged()
        updateSong()
    }

}