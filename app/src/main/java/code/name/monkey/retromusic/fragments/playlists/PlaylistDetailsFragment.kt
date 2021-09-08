package code.name.monkey.retromusic.fragments.playlists

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.activity.addCallback
import androidx.core.view.ViewCompat
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.adapter.song.OrderablePlaylistSongAdapter
import code.name.monkey.retromusic.databinding.FragmentPlaylistDetailBinding
import code.name.monkey.retromusic.db.PlaylistWithSongs
import code.name.monkey.retromusic.db.toSongs
import code.name.monkey.retromusic.extensions.dipToPix
import code.name.monkey.retromusic.extensions.surfaceColor
import code.name.monkey.retromusic.fragments.base.AbsMainActivityFragment
import code.name.monkey.retromusic.helper.menu.PlaylistMenuHelper
import code.name.monkey.retromusic.interfaces.ICabHolder
import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.util.RetroColorUtil
import com.afollestad.materialcab.MaterialCab
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentPlaylistDetailBinding.bind(view)
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true).addTarget(view)
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false)
        setHasOptionsMenu(true)
        mainActivity.addMusicServiceEventListener(viewModel)
        mainActivity.setSupportActionBar(binding.toolbar)
        ViewCompat.setTransitionName(binding.container, "playlist")
        playlist = arguments.extraPlaylist
        binding.toolbar.title = playlist.playlistEntity.playlistName
        setUpRecyclerView()
        viewModel.getSongs().observe(viewLifecycleOwner, {
            songs(it.toSongs())
        })
        postponeEnterTransition()
        requireView().doOnPreDraw { startPostponedEnterTransition() }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if (!handleBackPress()) {
                remove()
                requireActivity().onBackPressed()
            }
        }
    }

    private fun setUpRecyclerView() {
        playlistSongAdapter = OrderablePlaylistSongAdapter(
            playlist.playlistEntity,
            requireActivity(),
            ArrayList(),
            R.layout.item_list,
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
        }
        playlistSongAdapter.registerAdapterDataObserver(object :
            RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                super.onChanged()
                checkIsEmpty()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_playlist_detail, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return PlaylistMenuHelper.handleMenuClick(requireActivity(), playlist, item)
    }

    private fun checkForPadding() {
        val height = dipToPix(52f)
        binding.recyclerView.setPadding(0, 0, 0, height.toInt())
    }

    private fun checkIsEmpty() {
        checkForPadding()
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
        binding.empty.visibility = View.VISIBLE
        binding.emptyText.visibility = View.VISIBLE
    }

    fun songs(songs: List<Song>) {
        binding.progressIndicator.hide()
        if (songs.isNotEmpty()) {
            Log.i("Updated", songs[0].title)
            playlistSongAdapter.swapDataSet(songs)
        } else {
            showEmptyView()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun handleBackPress(): Boolean {
        cab?.let {
            if (it.isActive) {
                it.finish()
                return true
            }
        }
        return false
    }

    private var cab: MaterialCab? = null

    override fun openCab(menuRes: Int, callback: MaterialCab.Callback): MaterialCab {
        cab?.let {
            println("Cab")
            if (it.isActive) {
                it.finish()
            }
        }
        cab = MaterialCab(mainActivity, R.id.cab_stub)
            .setMenu(menuRes)
            .setCloseDrawableRes(R.drawable.ic_close)
            .setBackgroundColor(RetroColorUtil.shiftBackgroundColorForLightText(surfaceColor()))
            .start(callback)
        return cab as MaterialCab
    }

}