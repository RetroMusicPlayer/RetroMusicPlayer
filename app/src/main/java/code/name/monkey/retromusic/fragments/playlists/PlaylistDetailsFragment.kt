package code.name.monkey.retromusic.fragments.playlists

import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.adapter.song.OrderablePlaylistSongAdapter
import code.name.monkey.retromusic.databinding.FragmentPlaylistDetailBinding
import code.name.monkey.retromusic.db.PlaylistWithSongs
import code.name.monkey.retromusic.db.toSongs
import code.name.monkey.retromusic.extensions.surfaceColor
import code.name.monkey.retromusic.fragments.base.AbsMainActivityFragment
import code.name.monkey.retromusic.helper.menu.PlaylistMenuHelper
import code.name.monkey.retromusic.interfaces.ICabCallback
import code.name.monkey.retromusic.interfaces.ICabHolder
import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.util.MusicUtil
import code.name.monkey.retromusic.util.RetroColorUtil
import code.name.monkey.retromusic.util.ThemedFastScroller
import com.afollestad.materialcab.attached.AttachedCab
import com.afollestad.materialcab.attached.destroy
import com.afollestad.materialcab.attached.isActive
import com.afollestad.materialcab.createCab
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.transition.MaterialArcMotion
import com.google.android.material.transition.MaterialContainerTransform
import com.google.android.material.transition.MaterialSharedAxis
import com.h6ah4i.android.widget.advrecyclerview.animator.DraggableItemAnimator
import com.h6ah4i.android.widget.advrecyclerview.animator.GeneralItemAnimator
import com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class PlaylistDetailsFragment : AbsMainActivityFragment(R.layout.fragment_playlist_detail),
    ICabHolder {
    private val arguments by navArgs<PlaylistDetailsFragmentArgs>()
    private val viewModel by viewModel<PlaylistDetailsViewModel> {
        parametersOf(arguments.extraPlaylist)
    }

    private var _binding: FragmentPlaylistDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var playlist: PlaylistWithSongs
    private lateinit var playlistSongAdapter: OrderablePlaylistSongAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform(requireContext(), true).apply {
            drawingViewId = R.id.fragment_container
            scrimColor = Color.TRANSPARENT
            setAllContainerColors(surfaceColor())
            setPathMotion(MaterialArcMotion())
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentPlaylistDetailBinding.bind(view)
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true).addTarget(view)
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false)
        mainActivity.setSupportActionBar(binding.toolbar)
        binding.container.transitionName = "playlist"
        playlist = arguments.extraPlaylist
        binding.toolbar.title = playlist.playlistEntity.playlistName
        binding.toolbar.subtitle =
            MusicUtil.getPlaylistInfoString(requireContext(), playlist.songs.toSongs())
        setUpRecyclerView()
        viewModel.getSongs().observe(viewLifecycleOwner) {
            songs(it.toSongs())
        }
        viewModel.playlistExists().observe(viewLifecycleOwner) {
            if (!it) {
                findNavController().navigateUp()
            }
        }
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }
        binding.appBarLayout.statusBarForeground =
            MaterialShapeDrawable.createWithElevationOverlay(requireContext())
    }

    private fun setUpRecyclerView() {
        playlistSongAdapter = OrderablePlaylistSongAdapter(
            playlist.playlistEntity,
            requireActivity(),
            ArrayList(),
            R.layout.item_queue,
            this
        )

        val dragDropManager = RecyclerViewDragDropManager()

        val wrappedAdapter: RecyclerView.Adapter<*> =
            dragDropManager.createWrappedAdapter(playlistSongAdapter)


        val animator: GeneralItemAnimator = DraggableItemAnimator()
        binding.recyclerView.itemAnimator = animator

        dragDropManager.attachRecyclerView(binding.recyclerView)

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            binding.recyclerView.adapter = wrappedAdapter
            ThemedFastScroller.create(this)
        }
        playlistSongAdapter.registerAdapterDataObserver(object :
            RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                super.onChanged()
                checkIsEmpty()
            }
        })
    }

    override fun onCreateMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_playlist_detail, menu)
    }

    override fun onMenuItemSelected(item: MenuItem): Boolean {
        return PlaylistMenuHelper.handleMenuClick(requireActivity(), playlist, item)
    }

    private fun checkIsEmpty() {
        binding.empty.isVisible = playlistSongAdapter.itemCount == 0
        binding.emptyText.isVisible = playlistSongAdapter.itemCount == 0
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onPause() {
        playlistSongAdapter.saveSongs(playlist.playlistEntity)
        super.onPause()
    }

    private fun showEmptyView() {
        binding.empty.isVisible = true
        binding.emptyText.isVisible = true
    }

    fun songs(songs: List<Song>) {
        binding.progressIndicator.hide()
        if (songs.isNotEmpty()) {
            playlistSongAdapter.swapDataSet(songs)
        } else {
            showEmptyView()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private var cab: AttachedCab? = null

    override fun openCab(menuRes: Int, callback: ICabCallback): AttachedCab {
        cab?.let {
            if (it.isActive()) {
                it.destroy()
            }
        }
        cab = createCab(R.id.toolbar_container) {
            menu(menuRes)
            closeDrawable(R.drawable.ic_close)
            backgroundColor(literal = RetroColorUtil.shiftBackgroundColor(surfaceColor()))
            slideDown()
            onCreate { cab, menu -> callback.onCabCreated(cab, menu) }
            onSelection {
                callback.onCabItemClicked(it)
            }
            onDestroy { callback.onCabFinished(it) }
        }
        return cab as AttachedCab
    }
}