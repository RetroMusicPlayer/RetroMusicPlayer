package io.github.muntashirakon.music.fragments.playlists

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import code.name.monkey.appthemehelper.util.ATHUtil
import io.github.muntashirakon.music.R
import io.github.muntashirakon.music.adapter.song.PlaylistSongAdapter
import io.github.muntashirakon.music.db.PlaylistWithSongs
import io.github.muntashirakon.music.db.toSongs
import io.github.muntashirakon.music.extensions.dipToPix
import io.github.muntashirakon.music.fragments.base.AbsMainActivityFragment
import io.github.muntashirakon.music.helper.menu.PlaylistMenuHelper
import io.github.muntashirakon.music.model.Song
import io.github.muntashirakon.music.state.NowPlayingPanelState
import com.google.android.material.transition.MaterialContainerTransform
import kotlinx.android.synthetic.main.fragment_playlist_detail.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlaylistDetailsFragment : AbsMainActivityFragment(R.layout.fragment_playlist_detail) {
    private val arguments by navArgs<PlaylistDetailsFragmentArgs>()
    private val viewModel by viewModel<PlaylistDetailsViewModel> {
        parametersOf(arguments.extraPlaylist)
    }

    private lateinit var playlist: PlaylistWithSongs
    private lateinit var playlistSongAdapter: PlaylistSongAdapter

    private fun setUpTransitions() {
        val transform = MaterialContainerTransform()
        transform.setAllContainerColors(ATHUtil.resolveColor(requireContext(), R.attr.colorSurface))
        sharedElementEnterTransition = transform
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpTransitions()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        mainActivity.setBottomBarVisibility(View.GONE)
        mainActivity.addMusicServiceEventListener(viewModel)
        mainActivity.setSupportActionBar(toolbar)
        ViewCompat.setTransitionName(container, "playlist")
        playlist = arguments.extraPlaylist
        toolbar.title = playlist.playlistEntity.playlistName
        setUpRecyclerView()
        viewModel.getSongs().observe(viewLifecycleOwner, {
            songs(it.toSongs())
        })
    }

    private fun setUpRecyclerView() {
        playlistSongAdapter = PlaylistSongAdapter(
            playlist.playlistEntity,
            requireActivity(),
            ArrayList(),
            R.layout.item_list,
            null,
        )
        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = playlistSongAdapter
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
        recyclerView.setPadding(0, 0, 0, height.toInt())
    }

    private fun checkIsEmpty() {
        checkForPadding()
        empty.isVisible = playlistSongAdapter.itemCount == 0
        emptyText.isVisible = playlistSongAdapter.itemCount == 0
    }

    override fun onDestroy() {
        recyclerView?.itemAnimator = null
        recyclerView?.adapter = null
        super.onDestroy()
    }

    private fun showEmptyView() {
        empty.visibility = View.VISIBLE
        emptyText.visibility = View.VISIBLE
    }

    fun songs(songs: List<Song>) {
        progressIndicator.hide()
        if (songs.isNotEmpty()) {
            playlistSongAdapter.swapDataSet(songs)
        } else {
            showEmptyView()
        }
    }
}