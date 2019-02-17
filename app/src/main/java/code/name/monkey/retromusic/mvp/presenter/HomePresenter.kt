package code.name.monkey.retromusic.mvp.presenter

import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.model.*
import code.name.monkey.retromusic.mvp.Presenter
import code.name.monkey.retromusic.mvp.contract.HomeContract
import code.name.monkey.retromusic.ui.adapter.HomeAdapter.Companion.GENRES
import code.name.monkey.retromusic.ui.adapter.HomeAdapter.Companion.PLAYLISTS
import code.name.monkey.retromusic.ui.adapter.HomeAdapter.Companion.RECENT_ALBUMS
import code.name.monkey.retromusic.ui.adapter.HomeAdapter.Companion.RECENT_ARTISTS
import code.name.monkey.retromusic.ui.adapter.HomeAdapter.Companion.SUGGESTIONS
import code.name.monkey.retromusic.ui.adapter.HomeAdapter.Companion.TOP_ALBUMS
import code.name.monkey.retromusic.ui.adapter.HomeAdapter.Companion.TOP_ARTISTS
import code.name.monkey.retromusic.util.PreferenceUtil
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function7

operator fun CompositeDisposable.plusAssign(disposable: Disposable) {
    add(disposable)
}

class HomePresenter(private val view: HomeContract.HomeView) : Presenter(), HomeContract.HomePresenter {
    override fun homeSections() {
        disposable += Observable.combineLatest(repository.suggestionSongs, repository.recentAlbums,
                repository.topAlbums, repository.recentArtists, repository.topArtists,
                repository.allGenres, repository.favoritePlaylist,
                Function7<ArrayList<Song>, ArrayList<Album>, ArrayList<Album>, ArrayList<Artist>,
                        ArrayList<Artist>, ArrayList<Genre>, ArrayList<Playlist>, List<Home>>
                { suggestions: ArrayList<Song>, recentAlbums: ArrayList<Album>,
                  topAlbums: ArrayList<Album>, recentArtists: ArrayList<Artist>,
                  topArtists: ArrayList<Artist>, genres: ArrayList<Genre>,
                  favoritePlaylist: ArrayList<Playlist> ->
                    val homes: ArrayList<Home> = ArrayList()
                    if (suggestions.isNotEmpty()) homes.add(Home(R.string.suggestion_songs, 0, suggestions, SUGGESTIONS))
                    if (recentArtists.isNotEmpty()) homes.add(Home(R.string.recent_artists, 0, recentArtists, RECENT_ARTISTS))
                    if (recentAlbums.isNotEmpty()) homes.add(Home(R.string.recent_albums, 0, recentAlbums, RECENT_ALBUMS))
                    if (topArtists.isNotEmpty()) homes.add(Home(R.string.top_artists, 0, topArtists, TOP_ARTISTS))
                    if (topAlbums.isNotEmpty()) homes.add(Home(R.string.top_albums, 0, topAlbums, TOP_ALBUMS))
                    if (favoritePlaylist.isNotEmpty()) homes.add(Home(R.string.favorites, 0, favoritePlaylist, PLAYLISTS))
                    if (genres.isNotEmpty() && PreferenceUtil.getInstance().isGenreShown) homes.add(Home(R.string.genres, 0, genres, GENRES))
                    homes
                }).subscribe({ homes ->
            if (homes.isNotEmpty()) {
                view.showData(homes as ArrayList<Home>)
            }
        }, {
            view.showEmpty()
        }, { })
    }

    override fun subscribe() {
        homeSections()
    }

    override fun unsubscribe() {
        disposable.dispose()
    }
}
