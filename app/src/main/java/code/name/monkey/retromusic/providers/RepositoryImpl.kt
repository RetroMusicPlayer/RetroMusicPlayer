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

package code.name.monkey.retromusic.providers

import android.content.Context
import code.name.monkey.retromusic.App
import code.name.monkey.retromusic.loaders.*
import code.name.monkey.retromusic.model.*
import code.name.monkey.retromusic.providers.interfaces.Repository
import code.name.monkey.retromusic.rest.LastFMRestClient
import code.name.monkey.retromusic.rest.model.LastFmArtist
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class RepositoryImpl(private val context: Context) : Repository {

    override fun artistInfoFloable(
            name: String,
            lang: String?,
            cache: String?
    ): Observable<LastFmArtist> {
        return LastFMRestClient(context).apiService.getArtistInfoFloable(name, lang, cache)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun search(query: String?): MutableList<Any> {
        return SearchLoader.searchAll(context, query)
    }

    override fun allAlbums(): ArrayList<Album> {
        return AlbumLoader.getAllAlbums(context)
    }

    override fun recentAlbums(): ArrayList<Album> {
        return LastAddedSongsLoader.getLastAddedAlbums(context)
    }

    override fun topAlbums(): ArrayList<Album> {
        return TopAndRecentlyPlayedTracksLoader.getTopAlbums(context)
    }

    override fun allArtists(): ArrayList<Artist> {
        return ArtistLoader.getAllArtists(context)
    }

    override fun recentArtists(): ArrayList<Artist> {
        return LastAddedSongsLoader.getLastAddedArtists(context)
    }

    override fun topArtists(): ArrayList<Artist> {
        return TopAndRecentlyPlayedTracksLoader.getTopArtists(context)
    }

    override fun allPlaylists(): ArrayList<Playlist> {
        return PlaylistLoader.getAllPlaylists(context)
    }

    override fun allGenres(): ArrayList<Genre> {
        return GenreLoader.getAllGenres(context)
    }

    override fun getSongFlowable(id: Int): Observable<Song> {
        return SongLoader.getSongFlowable(context, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getAlbumFlowable(albumId: Int): Observable<Album> {
        return AlbumLoader.getAlbumFlowable(context, albumId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getArtistByIdFlowable(artistId: Int): Observable<Artist> {
        return ArtistLoader.getArtistFlowable(context, artistId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }


    override fun getPlaylistSongsFlowable(playlist: Playlist): Observable<ArrayList<Song>> {
        return PlaylistSongsLoader.getPlaylistSongListFlowable(context, playlist)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getGenreFlowable(genreId: Int): Observable<ArrayList<Song>> {
        return GenreLoader.getSongsFlowable(context, genreId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    override val favoritePlaylist: ArrayList<Playlist>
        get() = PlaylistLoader.getFavoritePlaylist(context)

    override fun allSongs(): ArrayList<Song> {
        return SongLoader.getAllSongs(context)
    }

    override val favoritePlaylistFlowable: Observable<ArrayList<Playlist>>
        get() = PlaylistLoader.getFavoritePlaylistFlowable(context)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())


    override val allSongsFlowable: Observable<ArrayList<Song>>
        get() = SongLoader.getAllSongsFlowable(context)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

    override val suggestionSongsFlowable: Observable<ArrayList<Song>>
        get() = SongLoader.suggestSongs(context)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

    override val allAlbumsFlowable: Observable<ArrayList<Album>>
        get() = AlbumLoader.getAllAlbumsFlowable(context)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

    override val recentAlbumsFlowable: Observable<ArrayList<Album>>
        get() = LastAddedSongsLoader.getLastAddedAlbumsFlowable(context)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

    override val topAlbumsFlowable: Observable<ArrayList<Album>>
        get() = TopAndRecentlyPlayedTracksLoader.getTopAlbumsFlowable(context)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

    override val allArtistsFlowable: Observable<ArrayList<Artist>>
        get() = ArtistLoader.getAllArtistsFlowable(context)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

    override val recentArtistsFlowable: Observable<ArrayList<Artist>>
        get() = LastAddedSongsLoader.getLastAddedArtistsFlowable(context)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

    override val topArtistsFlowable: Observable<ArrayList<Artist>>
        get() = TopAndRecentlyPlayedTracksLoader.getTopArtistsFlowable(context)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

    override val allPlaylistsFlowable: Observable<ArrayList<Playlist>>
        get() = PlaylistLoader.getAllPlaylistsFlowoable(context)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())


    override val allGenresFlowable: Observable<ArrayList<Genre>>
        get() = GenreLoader.getAllGenresFlowable(context)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

    override fun getSong(id: Int): Song {
        return SongLoader.getSong(context, id)
    }

    override fun getAlbum(albumId: Int): Album {
        return AlbumLoader.getAlbum(context, albumId)
    }

    override fun getArtistById(artistId: Long): Artist {
        return ArtistLoader.getArtist(context, artistId.toInt())
    }

    override fun getPlaylistSongs(playlist: Playlist): ArrayList<Song> {
        return PlaylistSongsLoader.getPlaylistSongList(context, playlist)

    }

    override fun getGenre(genreId: Int): ArrayList<Song> {
        return GenreLoader.getSongs(context, genreId)

    }
}
