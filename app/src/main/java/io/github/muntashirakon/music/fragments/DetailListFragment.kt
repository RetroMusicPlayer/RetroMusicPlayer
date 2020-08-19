package io.github.muntashirakon.music.fragments

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.muntashirakon.music.*
import io.github.muntashirakon.music.adapter.album.AlbumAdapter
import io.github.muntashirakon.music.adapter.artist.ArtistAdapter
import io.github.muntashirakon.music.adapter.song.SongAdapter
import io.github.muntashirakon.music.fragments.albums.AlbumClickListener
import io.github.muntashirakon.music.fragments.artists.ArtistClickListener
import io.github.muntashirakon.music.fragments.base.AbsMainActivityFragment
import io.github.muntashirakon.music.model.Album
import io.github.muntashirakon.music.model.Artist
import io.github.muntashirakon.music.model.Song
import io.github.muntashirakon.music.repository.RealRepository
import kotlinx.android.synthetic.main.fragment_playlist_detail.*
import kotlinx.coroutines.CoroutineScope
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
            FAVOURITES -> {
                loadFavorite()
            }
        }
    }

    private fun loadFavorite() {
        toolbar.setTitle(R.string.favorites)
        CoroutineScope(IO).launch {
            val songs = repository.favoritePlaylistHome()
            withContext(Main) {
                recyclerView.apply {
                    adapter = SongAdapter(
                        requireActivity(),
                        songs.arrayList as MutableList<Song>,
                        R.layout.item_list, null
                    )
                    layoutManager = linearLayoutManager()
                }
            }
        }
    }

    private fun loadArtists(title: Int, type: Int) {
        toolbar.setTitle(title)
        CoroutineScope(IO).launch {
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
        CoroutineScope(IO).launch {
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


    override fun onArtist(artistId: Int, imageView: ImageView) {

    }

    override fun onAlbumClick(albumId: Int, view: View) {

    }
}