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
import code.name.monkey.retromusic.model.Home
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
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

operator fun CompositeDisposable.plusAssign(disposable: Disposable) {
    add(disposable)
}

class HomePresenter(private val view: HomeContract.HomeView) : Presenter(), HomeContract.HomePresenter {
    private val hashSet: HashSet<Home> = HashSet()

    override fun homeSections() {
        loadSuggestions()
        loadRecentArtists()
        loadRecentAlbums()
        loadTopArtists()
        loadATopAlbums()
        loadFavorite()
        if (PreferenceUtil.getInstance().isGenreShown) loadGenre()
    }

    override fun subscribe() {
        homeSections()
    }

    override fun unsubscribe() {
        disposable.dispose()
    }

    private fun loadSuggestions() {
        disposable += repository.suggestionSongs
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.isNotEmpty()) hashSet.add(Home(5, R.string.suggestion_songs, 0, it, SUGGESTIONS, R.drawable.ic_audiotrack_black_24dp))
                    view.showData(ArrayList(hashSet))
                }, {
                    view.showEmpty()
                })
    }

    private fun loadRecentArtists() {
        disposable += repository.recentArtists
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.isNotEmpty()) hashSet.add(Home(0, R.string.recent_artists, 0, it, RECENT_ARTISTS, R.drawable.ic_artist_white_24dp))
                    view.showData(ArrayList(hashSet))
                }, {
                    view.showEmpty()
                })
    }

    private fun loadRecentAlbums() {
        disposable += repository.recentAlbums
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.isNotEmpty()) hashSet.add(Home(1, R.string.recent_albums, 0, it, RECENT_ALBUMS, R.drawable.ic_album_white_24dp))
                    view.showData(ArrayList(hashSet))
                }, {
                    view.showEmpty()
                })
    }

    private fun loadATopAlbums() {
        disposable += repository.topAlbums
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.isNotEmpty()) hashSet.add(Home(2, R.string.top_albums, 0, it, TOP_ALBUMS, R.drawable.ic_album_white_24dp))
                    view.showData(ArrayList(hashSet))
                }, {
                    view.showEmpty()
                })
    }

    private fun loadTopArtists() {
        disposable += repository.topArtists
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.isNotEmpty()) hashSet.add(Home(3, R.string.top_artists, 0, it, TOP_ARTISTS, R.drawable.ic_artist_white_24dp))
                    view.showData(ArrayList(hashSet))
                }, {
                    view.showEmpty()
                })
    }

    private fun loadFavorite() {
        disposable += repository.favoritePlaylist
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.isNotEmpty()) hashSet.add(Home(4, R.string.favorites, 0, it, PLAYLISTS, R.drawable.ic_favorite_white_24dp))
                    view.showData(ArrayList(hashSet))
                }, {
                    view.showEmpty()
                })
    }

    private fun loadGenre() {
        disposable += repository.allGenres
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.isNotEmpty()) hashSet.add(Home(6, R.string.genres, 0, it, GENRES, R.drawable.ic_guitar_acoustic_white_24dp))
                    view.showData(ArrayList(hashSet))
                }, {
                    view.showEmpty()
                })
    }
}
