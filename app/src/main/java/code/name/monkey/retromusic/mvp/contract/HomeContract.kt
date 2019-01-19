package code.name.monkey.retromusic.mvp.contract

import code.name.monkey.retromusic.model.*
import code.name.monkey.retromusic.mvp.BasePresenter
import code.name.monkey.retromusic.mvp.BaseView

interface HomeContract {

    interface HomeView : BaseView<ArrayList<Any>> {

        /*fun recentArtist(artists: ArrayList<Artist>)

        fun recentAlbum(albums: ArrayList<Album>)

        fun topArtists(artists: ArrayList<Artist>)

        fun topAlbums(albums: ArrayList<Album>)

        fun suggestions(songs: ArrayList<Song>)

        fun playlists(playlists: ArrayList<Playlist>)

        fun geners(songs: ArrayList<Genre>)*/

        fun loadHomes(homes: ArrayList<Home>)
    }

    interface HomePresenter : BasePresenter<HomeView> {

        /*fun loadRecentAlbums()

        fun loadTopAlbums()

        fun loadRecentArtists()

        fun loadTopArtists()

        fun loadSuggestions()

        fun loadGenres()*/
        fun loadAll();
    }
}