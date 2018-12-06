package code.name.monkey.retromusic.mvp.presenter

import code.name.monkey.retromusic.mvp.Presenter
import code.name.monkey.retromusic.mvp.contract.HomeContract
import code.name.monkey.retromusic.util.PreferenceUtil

class HomePresenter(private val view: HomeContract.HomeView) : Presenter(), HomeContract.HomePresenter {

    override fun subscribe() {

        loadRecentAlbums()
        loadRecentArtists()
        loadTopAlbums()
        loadTopArtists()
        loadSuggestions()

        if (PreferenceUtil.getInstance().isGenreShown) loadGenres()

    }

    override fun unsubscribe() {
        disposable.clear()
    }

    fun loadPlaylists() {
        disposable.add(repository.allPlaylists
                .observeOn(schedulerProvider.ui())
                .subscribeOn(schedulerProvider.io())
                .subscribe({ playlist ->
                    if (!playlist.isEmpty()) {
                        view.playlists(playlist)
                    }
                }, { view.showEmptyView() }, { view.completed() }))
    }

    override fun loadRecentAlbums() {
        disposable.add(repository.recentAlbums
                .observeOn(schedulerProvider.ui())
                .subscribeOn(schedulerProvider.io())
                .doOnSubscribe { view.loading() }
                .subscribe({ artists ->
                    if (!artists.isEmpty()) {
                        view.recentAlbum(artists)
                    }
                }, { view.showEmptyView() }, { view.completed() }))
    }

    override fun loadTopAlbums() {
        disposable.add(repository.topAlbums
                .observeOn(schedulerProvider.ui())
                .subscribeOn(schedulerProvider.io())
                .doOnSubscribe { view.loading() }
                .subscribe({ artists ->
                    if (!artists.isEmpty()) {
                        view.topAlbums(artists)
                    }
                }, { view.showEmptyView() }, { view.completed() }))
    }

    override fun loadRecentArtists() {
        disposable.add(repository.recentArtists
                .observeOn(schedulerProvider.ui())
                .subscribeOn(schedulerProvider.io())
                .doOnSubscribe { view.loading() }
                .subscribe({ artists ->
                    if (!artists.isEmpty()) {
                        view.recentArtist(artists)
                    }
                }, { view.showEmptyView() }, { view.completed() }))
    }

    override fun loadTopArtists() {
        disposable.add(repository.topArtists
                .observeOn(schedulerProvider.ui())
                .subscribeOn(schedulerProvider.io())
                .doOnSubscribe { view.loading() }
                .subscribe({ artists ->
                    if (!artists.isEmpty()) {
                        view.topArtists(artists)
                    }
                }, { view.showEmptyView() }, { view.completed() }))

    }

    override fun loadSuggestions() {
        disposable.add(repository.suggestionSongs
                .observeOn(schedulerProvider.ui())
                .subscribeOn(schedulerProvider.io())
                .doOnSubscribe { view.loading() }
                .subscribe({ songs -> view.suggestions(songs) }, { view.showEmptyView() }, { view.completed() }))
    }

    override fun loadGenres() {
        disposable.add(repository.allGenres
                .observeOn(schedulerProvider.ui())
                .subscribeOn(schedulerProvider.io())
                .doOnSubscribe { view.loading() }
                .subscribe({ genres ->
                    if (!genres.isEmpty()) {
                        view.geners(genres)
                    }
                },
                        { view.showEmptyView() }, { view.completed() }))
    }
}
