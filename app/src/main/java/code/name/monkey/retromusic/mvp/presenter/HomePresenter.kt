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
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function7

operator fun CompositeDisposable.plusAssign(disposable: Disposable) {
    add(disposable)
}

class HomePresenter(private val view: HomeContract.HomeView) : Presenter(), HomeContract.HomePresenter {
    override fun loadAll() {
        disposable += Observable.combineLatest(repository.suggestionSongs, repository.recentAlbums,
                repository.topAlbums, repository.recentArtists, repository.topArtists,
                repository.allGenres, repository.allPlaylists,
                Function7<ArrayList<Song>, ArrayList<Album>, ArrayList<Album>, ArrayList<Artist>,
                        ArrayList<Artist>, ArrayList<Genre>, ArrayList<Playlist>, List<Home>>
                { suggestions: ArrayList<Song>, recentAlbums: ArrayList<Album>,
                  topAlbums: ArrayList<Album>, recentArtists: ArrayList<Artist>,
                  topArtists: ArrayList<Artist>, genres: ArrayList<Genre>,
                  playlists: ArrayList<Playlist> ->
                    val homes: ArrayList<Home> = ArrayList()
                    if (suggestions.isNotEmpty()) homes.add(Home(R.string.suggestion_songs, 0, suggestions, SUGGESTIONS))
                    if (recentArtists.isNotEmpty()) homes.add(Home(R.string.recent_artists, 0, recentArtists, RECENT_ARTISTS))
                    if (recentAlbums.isNotEmpty()) homes.add(Home(R.string.recent_albums, 0, recentAlbums, RECENT_ALBUMS))
                    if (topArtists.isNotEmpty()) homes.add(Home(R.string.top_artists, 0, topArtists, TOP_ARTISTS))
                    if (topAlbums.isNotEmpty()) homes.add(Home(R.string.top_albums, 0, topAlbums, TOP_ALBUMS))
                    if (genres.isNotEmpty()) homes.add(Home(R.string.genres, 0, genres, GENRES))
                    if (playlists.isNotEmpty()) homes.add(Home(R.string.playlists, 0, playlists, PLAYLISTS))
                    homes
                }).subscribe { homes ->
            if (homes.isNotEmpty()) {
                view.loadHomes(homes as ArrayList<Home>)
            }
        }
    }

    override fun subscribe() {
        loadAll()
        /*loadRecentAlbums()
        loadRecentArtists()
        loadTopAlbums()
        loadTopArtists()
        loadSuggestions()*/

        /*if (PreferenceUtil.getInstance().isGenreShown) loadGenres()*/

    }

    override fun unsubscribe() {
        if (true) {
            disposable.dispose()
        }
    }

    /*fun loadPlaylists() {
        disposable += repository.allPlaylists
                .observeOn(schedulerProvider.ui())
                .subscribeOn(schedulerProvider.io())
                .subscribe({ playlist ->
                    if (!playlist.isEmpty()) {
                        view.playlists(playlist)
                    }
                }, { view.showEmptyView() }, { view.completed() })
    }

    override fun loadRecentAlbums() {
        disposable += repository.recentAlbums
                .observeOn(schedulerProvider.ui())
                .subscribeOn(schedulerProvider.io())
                .doOnSubscribe { view.loading() }
                .subscribe({ artists ->
                    if (!artists.isEmpty()) {
                        view.recentAlbum(artists)
                    }
                }, { view.showEmptyView() }, { view.completed() })
    }

    override fun loadTopAlbums() {
        disposable += repository.topAlbums
                .observeOn(schedulerProvider.ui())
                .subscribeOn(schedulerProvider.io())
                .doOnSubscribe { view.loading() }
                .subscribe({ artists ->
                    if (!artists.isEmpty()) {
                        view.topAlbums(artists)
                    }
                }, { view.showEmptyView() }, { view.completed() })
    }

    override fun loadRecentArtists() {
        disposable += repository.recentArtists
                .observeOn(schedulerProvider.ui())
                .subscribeOn(schedulerProvider.io())
                .doOnSubscribe { view.loading() }
                .subscribe({ artists ->
                    if (!artists.isEmpty()) {
                        view.recentArtist(artists)
                    }
                }, { view.showEmptyView() }, { view.completed() })
    }

    override fun loadTopArtists() {
        disposable += repository.topArtists
                .observeOn(schedulerProvider.ui())
                .subscribeOn(schedulerProvider.io())
                .doOnSubscribe { view.loading() }
                .subscribe({ artists ->
                    if (!artists.isEmpty()) {
                        view.topArtists(artists)
                    }
                }, { view.showEmptyView() }, { view.completed() })

    }

    override fun loadSuggestions() {
        disposable += repository.suggestionSongs
                .observeOn(schedulerProvider.ui())
                .subscribeOn(schedulerProvider.io())
                .doOnSubscribe { view.loading() }
                .subscribe({ songs -> view.suggestions(songs) },
                        { view.showEmptyView() }, { view.completed() })
    }

    override fun loadGenres() {
        disposable += repository.allGenres
                .observeOn(schedulerProvider.ui())
                .subscribeOn(schedulerProvider.io())
                .doOnSubscribe { view.loading() }
                .subscribe({ genres ->
                    if (!genres.isEmpty()) {
                        view.geners(genres)
                    }
                }, { view.showEmptyView() }, { view.completed() })
    }*/
}
