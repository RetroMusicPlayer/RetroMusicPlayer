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
                    if (suggestions.isNotEmpty()) homes.add(Home(R.string.suggestion_songs, 0, suggestions, SUGGESTIONS, R.drawable.ic_audiotrack_black_24dp))
                    if (recentArtists.isNotEmpty()) homes.add(Home(R.string.recent_artists, 0, recentArtists, RECENT_ARTISTS, R.drawable.ic_artist_white_24dp))
                    if (recentAlbums.isNotEmpty()) homes.add(Home(R.string.recent_albums, 0, recentAlbums, RECENT_ALBUMS, R.drawable.ic_album_white_24dp))
                    if (topArtists.isNotEmpty()) homes.add(Home(R.string.top_artists, 0, topArtists, TOP_ARTISTS, R.drawable.ic_artist_white_24dp))
                    if (topAlbums.isNotEmpty()) homes.add(Home(R.string.top_albums, 0, topAlbums, TOP_ALBUMS, R.drawable.ic_album_white_24dp))
                    if (favoritePlaylist.isNotEmpty()) homes.add(Home(R.string.favorites, 0, favoritePlaylist, PLAYLISTS, R.drawable.ic_favorite_white_24dp))
                    if (genres.isNotEmpty() && PreferenceUtil.getInstance().isGenreShown) homes.add(Home(R.string.genres, 0, genres, GENRES, R.drawable.ic_guitar_acoustic_white_24dp))
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
