package code.name.monkey.retromusic.fragments

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import code.name.monkey.retromusic.*
import code.name.monkey.retromusic.adapter.album.AlbumAdapter
import code.name.monkey.retromusic.adapter.artist.ArtistAdapter
import code.name.monkey.retromusic.adapter.song.SongAdapter
import code.name.monkey.retromusic.db.toSong
import code.name.monkey.retromusic.fragments.albums.AlbumClickListener
import code.name.monkey.retromusic.fragments.artists.ArtistClickListener
import code.name.monkey.retromusic.fragments.base.AbsMainActivityFragment
import code.name.monkey.retromusic.model.Album
import code.name.monkey.retromusic.model.Artist
import code.name.monkey.retromusic.state.NowPlayingPanelState
import kotlinx.android.synthetic.main.fragment_playlist_detail.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class DetailListFragment : AbsMainActivityFragment(R.layout.fragment_playlist_detail),
    ArtistClickListener, AlbumClickListener {
    private val args by navArgs<DetailListFragmentArgs>()
    private val libraryViewModel by sharedViewModel<LibraryViewModel>()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        libraryViewModel.setPanelState(NowPlayingPanelState.COLLAPSED_WITHOUT)
        mainActivity.setSupportActionBar(toolbar)
        progressIndicator.hide()
        when (args.type) {
            TOP_ARTISTS -> {
                loadArtists(R.string.top_artists, TOP_ARTISTS)
            }
            RECENT_ARTISTS -> {
                loadArtists(R.string.recent_artists, RECENT_ARTISTS)
            }
            TOP_ALBUMS -> {
                loadAlbums(R.string.top_albums, TOP_ALBUMS)
            }
            RECENT_ALBUMS -> {
                loadAlbums(R.string.recent_albums, RECENT_ALBUMS)
            }
            FAVOURITES -> loadFavorite()
            HISTORY_PLAYLIST -> loadHistory()
            LAST_ADDED_PLAYLIST -> lastAddedSongs()
            TOP_PLAYED_PLAYLIST -> topPlayed()
        }
    }

    private fun lastAddedSongs() {
        toolbar.setTitle(R.string.last_added)
        val songAdapter = SongAdapter(
            requireActivity(),
            mutableListOf(),
            R.layout.item_list, null
        )
        recyclerView.apply {
            adapter = songAdapter
            layoutManager = linearLayoutManager()
        }
        libraryViewModel.recentSongs().observe(viewLifecycleOwner, Observer { songs ->
            songAdapter.swapDataSet(songs)
        })
    }

    private fun topPlayed() {
        toolbar.setTitle(R.string.my_top_tracks)
        val songAdapter = SongAdapter(
            requireActivity(),
            mutableListOf(),
            R.layout.item_list, null
        )
        recyclerView.apply {
            adapter = songAdapter
            layoutManager = linearLayoutManager()
        }
        libraryViewModel.playCountSongs().observe(viewLifecycleOwner, Observer { songs ->
            songAdapter.swapDataSet(songs)
        })

    }

    private fun loadHistory() {
        toolbar.setTitle(R.string.history)

        val songAdapter = SongAdapter(
            requireActivity(),
            mutableListOf(),
            R.layout.item_list, null
        )
        recyclerView.apply {
            adapter = songAdapter
            layoutManager = linearLayoutManager()
        }
        libraryViewModel.observableHistorySongs().observe(viewLifecycleOwner, Observer {
            val songs = it.map { historyEntity -> historyEntity.toSong() }
            songAdapter.swapDataSet(songs)
        })
    }

    private fun loadFavorite() {
        toolbar.setTitle(R.string.favorites)
        val songAdapter = SongAdapter(
            requireActivity(),
            mutableListOf(),
            R.layout.item_list, null
        )
        recyclerView.apply {
            adapter = songAdapter
            layoutManager = linearLayoutManager()
        }
        libraryViewModel.favorites().observe(viewLifecycleOwner, {
            val songs = it.map { songEntity -> songEntity.toSong() }
            songAdapter.swapDataSet(songs)
        })
    }

    private fun loadArtists(title: Int, type: Int) {
        toolbar.setTitle(title)
        libraryViewModel.artists(type).observe(viewLifecycleOwner, { artists ->
            recyclerView.apply {
                adapter = artistAdapter(artists)
                layoutManager = gridLayoutManager()
            }
        })
    }

    private fun loadAlbums(title: Int, type: Int) {
        toolbar.setTitle(title)
        libraryViewModel.albums(type).observe(viewLifecycleOwner, { albums ->
            recyclerView.apply {
                adapter = albumAdapter(albums)
                layoutManager = gridLayoutManager()
            }
        })
    }

    private fun artistAdapter(artists: List<Artist>): ArtistAdapter = ArtistAdapter(
        requireActivity(),
        artists,
        R.layout.item_grid_circle,
        null, this@DetailListFragment
    )

    private fun albumAdapter(albums: List<Album>): AlbumAdapter = AlbumAdapter(
        requireActivity(),
        albums,
        R.layout.item_grid,
        null, this@DetailListFragment
    )

    private fun linearLayoutManager(): LinearLayoutManager =
        LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

    private fun gridLayoutManager(): GridLayoutManager =
        GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)


    override fun onArtist(artistId: Long, imageView: ImageView) {
        findNavController().navigate(
            R.id.artistDetailsFragment,
            bundleOf(EXTRA_ARTIST_ID to artistId),
            null,
            FragmentNavigatorExtras(imageView to getString(R.string.transition_artist_image))
        )
    }

    override fun onAlbumClick(albumId: Long, view: View) {
        findNavController().navigate(
            R.id.albumDetailsFragment,
            bundleOf(EXTRA_ALBUM_ID to albumId),
            null,
            FragmentNavigatorExtras(view to getString(R.string.transition_album_art))
        )
    }
}