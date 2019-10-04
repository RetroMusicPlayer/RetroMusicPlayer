/*
 * Copyright (c) 2019 Hemanth Savarala.
 *
 * Licensed under the GNU General Public License v3
 *
 * This is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by
 *  the Free Software Foundation either version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 */

package code.name.monkey.retromusic.mvp.presenter

import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.adapter.HomeAdapter.Companion.PLAYLISTS
import code.name.monkey.retromusic.adapter.HomeAdapter.Companion.RECENT_ALBUMS
import code.name.monkey.retromusic.adapter.HomeAdapter.Companion.RECENT_ARTISTS
import code.name.monkey.retromusic.adapter.HomeAdapter.Companion.TOP_ALBUMS
import code.name.monkey.retromusic.adapter.HomeAdapter.Companion.TOP_ARTISTS
import code.name.monkey.retromusic.model.Home
import code.name.monkey.retromusic.mvp.BaseView
import code.name.monkey.retromusic.mvp.Presenter
import code.name.monkey.retromusic.mvp.PresenterImpl
import code.name.monkey.retromusic.providers.interfaces.Repository
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import javax.inject.Inject

operator fun CompositeDisposable.plusAssign(disposable: Disposable) {
    add(disposable)
}

interface HomeView : BaseView {
    fun sections(sections: ArrayList<Home>)
}

interface HomePresenter : Presenter<HomeView> {
    fun loadSections()


    class HomePresenterImpl @Inject constructor(
            private val repository: Repository
    ) : PresenterImpl<HomeView>(), HomePresenter {

        private val hashSet: HashSet<Home> = HashSet()

        override fun loadSections() {
            loadRecentArtists()
            loadRecentAlbums()
            loadTopArtists()
            loadATopAlbums()
            loadFavorite()
            //loadHomeSection()
        }

        private var disposable: CompositeDisposable = CompositeDisposable()

        private fun showData(sections: ArrayList<Home>) {
            if (sections.isEmpty()) {
                view.showEmptyView()
            } else {
                view.sections(sections)
            }
        }

        private fun loadRecentArtists() {
            disposable += repository.recentArtistsFlowable
                    .subscribe {
                        if (it.isNotEmpty()) hashSet.add(
                                Home(0,
                                        R.string.recent_artists,
                                        R.string.recent_added_artists,
                                        it,
                                        RECENT_ARTISTS,
                                        R.drawable.ic_artist_white_24dp
                                ))
                        showData(ArrayList(hashSet))
                    }
        }

        private fun loadRecentAlbums() {
            disposable += repository.recentAlbumsFlowable
                    .subscribe {
                        if (it.isNotEmpty()) hashSet.add(
                                Home(1,
                                        R.string.recent_albums,
                                        R.string.recent_added_albums,
                                        it,
                                        RECENT_ALBUMS,
                                        R.drawable.ic_album_white_24dp
                                ))
                        showData(ArrayList(hashSet))
                    }
        }

        private fun loadTopArtists() {
            disposable += repository.topArtistsFlowable
                    .subscribe {
                        if (it.isNotEmpty()) hashSet.add(
                                Home(2,
                                        R.string.top_artists,
                                        R.string.most_played_artists,
                                        it,
                                        TOP_ARTISTS,
                                        R.drawable.ic_artist_white_24dp
                                ))
                        showData(ArrayList(hashSet))
                    }
        }

        private fun loadATopAlbums() {
            disposable += repository.topAlbumsFlowable
                    .subscribe {
                        if (it.isNotEmpty()) hashSet.add(
                                Home(3,
                                        R.string.top_albums,
                                        R.string.most_played_albums,
                                        it,
                                        TOP_ALBUMS,
                                        R.drawable.ic_album_white_24dp
                                ))
                        showData(ArrayList(hashSet))
                    }
        }

        private fun loadFavorite() {
            disposable += repository.favoritePlaylistFlowable
                    .subscribe {
                        if (it.isNotEmpty()) hashSet.add(
                                Home(4,
                                        R.string.favorites,
                                        R.string.favorites_songs,
                                        it,
                                        PLAYLISTS,
                                        R.drawable.ic_favorite_white_24dp
                                ))
                        showData(ArrayList(hashSet))
                    }
        }

        /*private fun loadHomeSection() {
            val ob = listOf(repository.recentArtistsFlowable,
                    repository.recentAlbumsFlowable,
                    repository.topArtistsFlowable,
                    repository.topAlbumsFlowable,
                    repository.favoritePlaylistFlowable)

            disposable += Observable.combineLatest(ob) {
                val hashSet: HashSet<Home> = HashSet()
                val recentArtist = it[0] as ArrayList<Artist>
                if (recentArtist.isNotEmpty()) hashSet.add(
                        Home(0,
                                R.string.recent_artists,
                                0,
                                recentArtist,
                                RECENT_ARTISTS,
                                R.drawable.ic_artist_white_24dp
                        ))
                val recentAlbums = it[1] as ArrayList<Album>
                if (recentAlbums.isNotEmpty()) hashSet.add(
                        Home(1,
                                R.string.recent_albums,
                                0,
                                recentAlbums,
                                RECENT_ALBUMS,
                                R.drawable.ic_album_white_24dp
                        ))
                val topArtists = it[2] as ArrayList<Artist>
                if (topArtists.isNotEmpty()) hashSet.add(
                        Home(2,
                                R.string.top_artists,
                                0,
                                topArtists,
                                TOP_ARTISTS,
                                R.drawable.ic_artist_white_24dp
                        ))
                val topAlbums = it[3] as ArrayList<Album>
                if (topAlbums.isNotEmpty()) hashSet.add(
                        Home(3,
                                R.string.top_albums,
                                0,
                                topAlbums,
                                TOP_ALBUMS,
                                R.drawable.ic_album_white_24dp
                        ))
                val playlists = it[4] as ArrayList<Playlist>
                if (playlists.isNotEmpty()) hashSet.add(
                        Home(4,
                                R.string.favorites,
                                0,
                                playlists,
                                PLAYLISTS,
                                R.drawable.ic_favorite_white_24dp
                        ))
                return@combineLatest hashSet
            }.subscribe {
                view.sections(ArrayList(it))
            }
        }*/
    }
}

/*class HomePresenter(
        private val view: HomeContract.HomeView,
        private val repositoryImpl: RepositoryImpl
) : Presenter(), HomeContract.HomePresenter {
    private val hashSet: HashSet<Home> = HashSet()

    override fun homeSections() {
        loadRecentArtists()
        loadRecentAlbums()
        loadTopArtists()
        loadATopAlbums()
        loadFavorite()
    }

    override fun subscribe() {
        homeSections()
    }

    override fun unsubscribe() {
        disposable.dispose()
    }

    private fun loadRecentArtists() {
        disposable += repositoryImpl.recentArtistsFlowable
                .subscribe({
                    if (it.isNotEmpty()) hashSet.add(Home(0, R.string.recent_artists, 0, it, RECENT_ARTISTS, R.drawable.ic_artist_white_24dp))
                    view.showData(ArrayList(hashSet))
                }, {
                    view.showEmptyView()
                })
    }

    private fun loadRecentAlbums() {
        disposable += repositoryImpl.recentAlbumsFlowable
                .subscribe({
                    if (it.isNotEmpty()) hashSet.add(Home(1, R.string.recent_albums, 0, it, RECENT_ALBUMS, R.drawable.ic_album_white_24dp))
                    view.showData(ArrayList(hashSet))
                }, {
                    view.showEmptyView()
                })
    }

    private fun loadATopAlbums() {
        disposable += repositoryImpl.topAlbumsFlowable
                .subscribe({
                    if (it.isNotEmpty()) hashSet.add(Home(3, R.string.top_albums, 0, it, TOP_ALBUMS, R.drawable.ic_album_white_24dp))
                    view.showData(ArrayList(hashSet))
                }, {
                    view.showEmptyView()
                })
    }

    private fun loadTopArtists() {
        disposable += repositoryImpl.topArtistsFlowable
                .subscribe({
                    if (it.isNotEmpty()) hashSet.add(Home(2, R.string.top_artists, 0, it, TOP_ARTISTS, R.drawable.ic_artist_white_24dp))
                    view.showData(ArrayList(hashSet))
                }, {
                    view.showEmptyView()
                })
    }

    private fun loadFavorite() {
        disposable += repositoryImpl.favoritePlaylistFlowable
                .subscribe({
                    if (it.isNotEmpty()) hashSet.add(Home(4, R.string.favorites, 0, it, PLAYLISTS, R.drawable.ic_favorite_white_24dp))
                    view.showData(ArrayList(hashSet))
                }, {
                    view.showEmptyView()
                })
    }
}*/
