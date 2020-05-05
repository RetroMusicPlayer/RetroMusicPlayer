package code.name.monkey.retromusic.fragments.player.color

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import code.name.monkey.appthemehelper.util.ATHUtil
import code.name.monkey.appthemehelper.util.ColorUtil
import code.name.monkey.appthemehelper.util.ToolbarContentTintHelper
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.fragments.base.AbsPlayerFragment
import code.name.monkey.retromusic.fragments.player.PlayerAlbumCoverFragment
import code.name.monkey.retromusic.helper.MusicPlayerRemote
import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.util.color.MediaNotificationProcessor
import kotlinx.android.synthetic.main.fragment_color_player.*

class ColorFragment : AbsPlayerFragment() {

    private var lastColor: Int = 0
    private var backgroundColor: Int = 0
    private lateinit var playbackControlsFragment: ColorPlaybackControlsFragment
    private var valueAnimator: ValueAnimator? = null

    override fun playerToolbar(): Toolbar {
        return playerToolbar
    }

    override val paletteColor: Int
        get() = backgroundColor

    override fun onColorChanged(color: MediaNotificationProcessor) {
        playbackControlsFragment.setDark(color)
        lastColor = color.backgroundColor
        callbacks?.onPaletteColorChanged()

        ToolbarContentTintHelper.colorizeToolbar(
            playerToolbar,
            ATHUtil.resolveColor(requireContext(), R.attr.colorControlNormal),
            requireActivity()
        )
    }

    override fun onFavoriteToggled() {
        //toggleFavorite(MusicPlayerRemote.currentSong)
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
        return lastColor
    }

    override fun toggleFavorite(song: Song) {
        super.toggleFavorite(song)
        if (song.id == MusicPlayerRemote.currentSong.id) {
            updateIsFavorite()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (valueAnimator != null) {
            valueAnimator!!.cancel()
            valueAnimator = null
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_color_player, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpSubFragments()
        setUpPlayerToolbar()
        val playerAlbumCoverFragment =
            childFragmentManager.findFragmentById(R.id.playerAlbumCoverFragment) as PlayerAlbumCoverFragment
        playerAlbumCoverFragment.setCallbacks(this)
    }

    private fun setUpSubFragments() {
        playbackControlsFragment =
            childFragmentManager.findFragmentById(R.id.playbackControlsFragment) as ColorPlaybackControlsFragment
    }

    private fun setUpPlayerToolbar() {
        playerToolbar.apply {
            inflateMenu(R.menu.menu_player)
            setNavigationOnClickListener { requireActivity().onBackPressed() }
            setOnMenuItemClickListener(this@ColorFragment)
            ToolbarContentTintHelper.colorizeToolbar(
                this,
                ATHUtil.resolveColor(requireContext(), R.attr.colorControlNormal),
                requireActivity()
            )
        }
    }

    private fun setColors(colors: MediaNotificationProcessor) {
        this.lastColor = colors.backgroundColor
        this.backgroundColor = colors.backgroundColor
        playbackControlsFragment.setDark(colors)
        colorGradientBackground?.setBackgroundColor(colors.backgroundColor)
        playerActivity?.setLightNavigationBar(ColorUtil.isColorLight(colors.backgroundColor))
        callbacks?.onPaletteColorChanged()
        ToolbarContentTintHelper.colorizeToolbar(
            playerToolbar,
            colors.secondaryTextColor,
            requireActivity()
        )
    }

    companion object {
        fun newInstance(): ColorFragment {
            val args = Bundle()
            val fragment = ColorFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
