package io.github.muntashirakon.music.fragments

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.muntashirakon.music.*
import io.github.muntashirakon.music.adapter.album.AlbumAdapter
import io.github.muntashirakon.music.adapter.artist.ArtistAdapter
import io.github.muntashirakon.music.adapter.song.SongAdapter
import io.github.muntashirakon.music.db.toSong
import io.github.muntashirakon.music.fragments.albums.AlbumClickListener
import io.github.muntashirakon.music.fragments.artists.ArtistClickListener
import io.github.muntashirakon.music.fragments.base.AbsMainActivityFragment
import io.github.muntashirakon.music.model.Album
import io.github.muntashirakon.music.model.Artist
import io.github.muntashirakon.music.repository.RealRepository
import kotlinx.android.synthetic.main.fragment_playlist_detail.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject

class DetailListFragment : AbsMainActivityFragment(R.layout.fragment_playlist_detail),
    ArtistClickListener, AlbumClickListener {
    private val args by navArgs<DetailListFragmentArgs>()
    private val repository by inject<RealRepository>()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mainActivity.setSupportActionBar(toolbar)
        mainActivity.hideBottomBarVisibility(false)
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
        lifecycleScope.launch(IO) {
            val songs = repository.recentSongs()
            withContext(Main) { songAdapter.swapDataSet(songs) }
        }
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
        lifecycleScope.launch(IO) {
            val songs = repository.playCountSongs().map {
                it.toSong()
            }
            withContext(Main) { songAdapter.swapDataSet(songs) }
        }
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
        repository.observableHistorySongs().observe(viewLifecycleOwner, Observer {
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
        repository.favorites().observe(viewLifecycleOwner, Observer {
            println(it.size)
            val songs = it.map { songEntity -> songEntity.toSong() }
            songAdapter.swapDataSet(songs)
        })
    }

    private fun loadArtists(title: Int, type: Int) {
        toolbar.setTitle(title)
        lifecycleScope.launch(IO) {
            val artists =
                if (type == TOP_ARTISTS) repository.topArtists() else repository.recentArtists()
            withContext(Main) {
                recyclerView.apply {
                    adapter = artistAdapter(artists)
                    layoutManager = gridLayoutManager()
                }
            }
        }
    }

    private fun loadAlbums(title: Int, type: Int) {
        toolbar.setTitle(title)
        lifecycleScope.launch(IO) {
            val albums =
                if (type == TOP_ALBUMS) repository.topAlbums() else repository.recentAlbums()
            withContext(Main) {
                recyclerView.apply {
                    adapter = albumAdapter(albums)
                    layoutManager = gridLayoutManager()

                }
            }
        }
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