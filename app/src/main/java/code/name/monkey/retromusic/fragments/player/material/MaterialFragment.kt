package code.name.monkey.retromusic.fragments.player.material

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import code.name.monkey.appthemehelper.util.ATHUtil
import code.name.monkey.appthemehelper.util.ColorUtil
import code.name.monkey.appthemehelper.util.ToolbarContentTintHelper
import code.name.monkey.retromusic.CustomBottomSheetBehavior
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.activities.base.AbsSlidingMusicPanelActivity
import code.name.monkey.retromusic.adapter.song.PlayingQueueAdapter
import code.name.monkey.retromusic.fragments.base.AbsPlayerFragment
import code.name.monkey.retromusic.fragments.player.PlayerAlbumCoverFragment
import code.name.monkey.retromusic.fragments.player.normal.PlayerFragment
import code.name.monkey.retromusic.helper.MusicPlayerRemote
import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.util.color.MediaNotificationProcessor
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.fragment_material.*
import kotlinx.android.synthetic.main.status_bar.*

/**
 * @author Hemanth S (h4h13).
 */
class MaterialFragment : AbsPlayerFragment(), View.OnLayoutChangeListener {
    private lateinit var queueAdapter: PlayingQueueAdapter
    private val bottomSheetCallbackList = object : BottomSheetBehavior.BottomSheetCallback() {
        override fun onSlide(bottomSheet: View, slideOffset: Float) {
            (requireActivity() as AbsSlidingMusicPanelActivity).getBottomSheetBehavior()
                .setAllowDragging(false)

            sheetContent.setPadding(
                sheetContent.paddingLeft,
                (slideOffset * status_bar.height).toInt(),
                sheetContent.paddingRight,
                sheetContent.paddingBottom
            )

            playerControlsContainer.layoutParams.height = playerContainer.width
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

    override fun playerToolbar(): Toolbar {
        return playerToolbar
    }

    private var lastColor: Int = 0

    override val paletteColor: Int
        get() = lastColor

    private lateinit var playbackControlsFragment: MaterialControlsFragment

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
        return ATHUtil.resolveColor(requireContext(), R.attr.colorControlNormal)
    }

    override fun onColorChanged(color: MediaNotificationProcessor) {
        val darkColor = ColorUtil.darkenColorTheme(color.backgroundColor)
        playerContainer?.setBackgroundColor(ColorUtil.darkenColorTheme(color.backgroundColor))
        upComing?.setBackgroundColor(ColorUtil.darkenColorTheme(darkColor))
        playbackControlsFragment.setDark(color.backgroundColor)
        lastColor = color.backgroundColor
        callbacks?.onPaletteColorChanged()

        ToolbarContentTintHelper.colorizeToolbar(
            playerToolbar,
            ATHUtil.resolveColor(requireContext(), R.attr.colorControlNormal),
            requireActivity()
        )
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_material, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpSubFragments()
        setUpPlayerToolbar()
        setupPanel()
        setUpQueue()
        getQueuePanel().addBottomSheetCallback(bottomSheetCallbackList)

        playerQueueSheet.setOnTouchListener { _, _ ->
            (requireActivity() as AbsSlidingMusicPanelActivity).getBottomSheetBehavior()
                .setAllowDragging(false)
            getQueuePanel().setAllowDragging(true)
            return@setOnTouchListener false
        }
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

    private fun setupPanel() {
        if (!ViewCompat.isLaidOut(playerContainer) || playerContainer.isLayoutRequested) {
            playerContainer.addOnLayoutChangeListener(this)
            return
        }
        val height = playerContainer?.height ?: 0
        val width = playerContainer?.width ?: 0
        val finalHeight = height - (playerControlsContainer.height - width)
        val panel = getQueuePanel()
        panel.peekHeight = finalHeight
    }

    private fun setUpSubFragments() {
        playbackControlsFragment =
            childFragmentManager.findFragmentById(R.id.playbackControlsFragment) as MaterialControlsFragment
        val playerAlbumCoverFragment =
            childFragmentManager.findFragmentById(R.id.playerAlbumCoverFragment) as PlayerAlbumCoverFragment
        playerAlbumCoverFragment.setCallbacks(this)
    }

    private fun setUpPlayerToolbar() {
        playerToolbar.apply {
            inflateMenu(R.menu.menu_player)
            setNavigationOnClickListener { requireActivity().onBackPressed() }
            setOnMenuItemClickListener(this@MaterialFragment)
            ToolbarContentTintHelper.colorizeToolbar(
                this,
                ATHUtil.resolveColor(context, R.attr.colorControlNormal),
                requireActivity()
            )
        }
    }

    override fun onServiceConnected() {
        updateIsFavorite()
        queueAdapter.swapDataSet(MusicPlayerRemote.playingQueue)
    }

    override fun onPlayingMetaChanged() {
        updateIsFavorite()
    }

    companion object {

        fun newInstance(): PlayerFragment {
            return PlayerFragment()
        }
    }

    private fun getQueuePanel(): CustomBottomSheetBehavior<FrameLayout> {
        return CustomBottomSheetBehavior.from(playerQueueSheet) as CustomBottomSheetBehavior<FrameLayout>
    }

    override fun onDestroyView() {
        super.onDestroyView()
        getQueuePanel().removeBottomSheetCallback(bottomSheetCallbackList)
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
        val height = playerContainer?.height ?: 0
        val width = playerContainer?.width ?: 0
        val finalHeight = height - width
        playerControlsContainer.layoutParams.height = height - width
        val panel = getQueuePanel()
        panel.peekHeight = finalHeight
    }
}
