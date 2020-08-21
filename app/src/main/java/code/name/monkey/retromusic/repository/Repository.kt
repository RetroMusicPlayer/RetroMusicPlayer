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

package code.name.monkey.retromusic.repository

import android.content.Context
import androidx.lifecycle.LiveData
import code.name.monkey.retromusic.*
import code.name.monkey.retromusic.db.HistoryEntity
import code.name.monkey.retromusic.db.PlaylistEntity
import code.name.monkey.retromusic.db.PlaylistWithSongs
import code.name.monkey.retromusic.db.SongEntity
import code.name.monkey.retromusic.model.*
import code.name.monkey.retromusic.model.smartplaylist.NotPlayedPlaylist
import code.name.monkey.retromusic.network.LastFMService
import code.name.monkey.retromusic.network.model.LastFmAlbum
import code.name.monkey.retromusic.network.model.LastFmArtist
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow

interface Repository {

    fun songsFlow(): Flow<Result<List<Song>>>
    fun albumsFlow(): Flow<Result<List<Album>>>
    fun artistsFlow(): Flow<Result<List<Artist>>>
    fun playlistsFlow(): Flow<Result<List<Playlist>>>
    fun genresFlow(): Flow<Result<List<Genre>>>

    suspend fun allAlbums(): List<Album>
    suspend fun albumById(albumId: Int): Album
    suspend fun allSongs(): List<Song>
    suspend fun allArtists(): List<Artist>
    suspend fun albumArtists(): List<Artist>
    suspend fun allPlaylists(): List<Playlist>
    suspend fun allGenres(): List<Genre>
    suspend fun search(query: String?): MutableList<Any>
    suspend fun getPlaylistSongs(playlist: Playlist): List<Song>
    suspend fun getGenre(genreId: Int): List<Song>
    suspend fun artistInfo(name: String, lang: String?, cache: String?): LastFmArtist
    suspend fun albumInfo(artist: String, album: String): LastFmAlbum
    suspend fun artistById(artistId: Int): Artist
    suspend fun recentArtists(): List<Artist>
    suspend fun topArtists(): List<Artist>
    suspend fun topAlbums(): List<Album>
    suspend fun recentAlbums(): List<Album>
    suspend fun recentArtistsHome(): Home
    suspend fun topArtistsHome(): Home
    suspend fun topAlbumsHome(): Home
    suspend fun recentAlbumsHome(): Home
    suspend fun favoritePlaylistHome(): Home
    suspend fun suggestionsHome(): Home
    suspend fun genresHome(): Home
    suspend fun playlists(): Home
    suspend fun homeSections(): List<Home>
    suspend fun homeSectionsFlow(): Flow<Result<List<Home>>>
    suspend fun playlist(playlistId: Int): Playlist
    suspend fun playlistWithSongs(): List<PlaylistWithSongs>
    suspend fun playlistSongs(playlistWithSongs: PlaylistWithSongs): List<Song>
    suspend fun insertSongs(songs: List<SongEntity>)
    suspend fun checkPlaylistExists(playlistName: String): List<PlaylistEntity>
    suspend fun createPlaylist(playlistEntity: PlaylistEntity): Long
    suspend fun roomPlaylists(): List<PlaylistEntity>
    suspend fun deleteRoomPlaylist(playlists: List<PlaylistEntity>)
    suspend fun renameRoomPlaylist(playlistId: Int, name: String)
    suspend fun removeSongsFromPlaylist(songs: List<SongEntity>)
    suspend fun removeSongFromPlaylist(songEntity: SongEntity)
    suspend fun deleteSongsFromPlaylist(playlists: List<PlaylistEntity>)
    suspend fun favoritePlaylist(): List<PlaylistEntity>
    suspend fun isFavoriteSong(songEntity: SongEntity): List<SongEntity>
    suspend fun addSongToHistory(currentSong: Song)
    suspend fun songPresentInHistory(currentSong: Song): HistoryEntity?
    suspend fun updateHistorySong(currentSong: Song)
    fun historySong(): LiveData<List<HistoryEntity>>
}

class RealRepository(
    private val context: Context,
    private val lastFMService: LastFMService,
    private val songRepository: SongRepository,
    private val albumRepository: AlbumRepository,
    private val artistRepository: ArtistRepository,
    private val genreRepository: GenreRepository,
    private val lastAddedRepository: LastAddedRepository,
    private val playlistRepository: PlaylistRepository,
    private val searchRepository: RealSearchRepository,
    private val playedTracksRepository: TopPlayedRepository,
    private val roomRepository: RoomPlaylistRepository
) : Repository {

    override suspend fun allAlbums(): List<Album> = albumRepository.albums()

    override suspend fun albumById(albumId: Int): Album = albumRepository.album(albumId)

    override suspend fun allArtists(): List<Artist> = artistRepository.artists()

    override suspend fun albumArtists(): List<Artist> = artistRepository.albumArtists()

    override suspend fun artistById(artistId: Int): Artist = artistRepository.artist(artistId)

    override suspend fun recentArtists(): List<Artist> = lastAddedRepository.recentArtists()

    override suspend fun recentAlbums(): List<Album> = lastAddedRepository.recentAlbums()

    override suspend fun topArtists(): List<Artist> = playedTracksRepository.topArtists()

    override suspend fun topAlbums(): List<Album> = playedTracksRepository.topAlbums()

    override suspend fun allPlaylists(): List<Playlist> = playlistRepository.playlists()

    override suspend fun allGenres(): List<Genre> = genreRepository.genres()

    override suspend fun allSongs(): List<Song> = songRepository.songs()


    override suspend fun search(query: String?): MutableList<Any> =
        searchRepository.searchAll(context, query)

    override suspend fun getPlaylistSongs(playlist: Playlist): List<Song> {
        return if (playlist is AbsCustomPlaylist) {
            playlist.songs()
        } else {
            PlaylistSongsLoader.getPlaylistSongList(context, playlist.id)
        }
    }

    override suspend fun getGenre(genreId: Int): List<Song> = genreRepository.songs(genreId)


    override suspend fun artistInfo(
        name: String,
        lang: String?,
        cache: String?
    ): LastFmArtist = lastFMService.artistInfo(name, lang, cache)


    override suspend fun albumInfo(
        artist: String,
        album: String
    ): LastFmAlbum = lastFMService.albumInfo(artist, album)

    @ExperimentalCoroutinesApi
    override suspend fun homeSectionsFlow(): Flow<Result<List<Home>>> {
        val homes = MutableStateFlow<Result<List<Home>>>(value = Result.Loading)
        println("homeSections:Loading")
        val homeSections = mutableListOf<Home>()
        val sections = listOf(
            topArtistsHome(),
            topAlbumsHome(),
            recentArtistsHome(),
            recentAlbumsHome(),
            suggestionsHome(),
            favoritePlaylistHome(),
            genresHome()
        )
        for (section in sections) {
            if (section.arrayList.isNotEmpty()) {
                println("${section.homeSection} -> ${section.arrayList.size}")
                homeSections.add(section)
            }
        }
        if (homeSections.isEmpty()) {
            homes.value = Result.Error
        } else {
            homes.value = Result.Success(homeSections)
        }
        return homes
    }

    override suspend fun homeSections(): List<Home> {
        val homeSections = mutableListOf<Home>()
        val sections = listOf(
            suggestionsHome(),
            topArtistsHome(),
            topAlbumsHome(),
            recentArtistsHome(),
            recentAlbumsHome(),
            favoritePlaylistHome()
        )
        for (section in sections) {
            if (section.arrayList.isNotEmpty()) {
                homeSections.add(section)
            }
        }
        return homeSections
    }


    override suspend fun playlist(playlistId: Int) =
        playlistRepository.playlist(playlistId)

    override suspend fun playlistWithSongs(): List<PlaylistWithSongs> =
        roomRepository.playlistWithSongs()

    override suspend fun playlistSongs(playlistWithSongs: PlaylistWithSongs): List<Song> {
        return playlistWithSongs.songs.map {
            it.toSong()
        }
    }

    override suspend fun insertSongs(songs: List<SongEntity>) =
        roomRepository.insertSongs(songs)

    override suspend fun checkPlaylistExists(playlistName: String): List<PlaylistEntity> =
        roomRepository.checkPlaylistExists(playlistName)

    override suspend fun createPlaylist(playlistEntity: PlaylistEntity): Long =
        roomRepository.createPlaylist(playlistEntity)

    override suspend fun roomPlaylists(): List<PlaylistEntity> = roomRepository.playlists()

    override suspend fun deleteRoomPlaylist(playlists: List<PlaylistEntity>) =
        roomRepository.deletePlaylistEntities(playlists)

    override suspend fun renameRoomPlaylist(playlistId: Int, name: String) =
        roomRepository.renamePlaylistEntity(playlistId, name)

    override suspend fun removeSongsFromPlaylist(songs: List<SongEntity>) =
        roomRepository.removeSongsFromPlaylist(songs)

    override suspend fun removeSongFromPlaylist(songEntity: SongEntity) =
        roomRepository.removeSongFromPlaylist(songEntity)


    override suspend fun deleteSongsFromPlaylist(playlists: List<PlaylistEntity>) =
        roomRepository.deleteSongsFromPlaylist(playlists)

    override suspend fun favoritePlaylist(): List<PlaylistEntity> =
        roomRepository.favoritePlaylist(context.getString(R.string.favorites))

    override suspend fun isFavoriteSong(songEntity: SongEntity): List<SongEntity> =
        roomRepository.isFavoriteSong(songEntity)

    override suspend fun addSongToHistory(currentSong: Song) =
        roomRepository.addSongToHistory(currentSong)

    override suspend fun songPresentInHistory(currentSong: Song): HistoryEntity? =
        roomRepository.songPresentInHistory(currentSong)

    override suspend fun updateHistorySong(currentSong: Song) =
        roomRepository.updateHistorySong(currentSong)

    override fun historySong(): LiveData<List<HistoryEntity>> =
        roomRepository.historySongs()

    override suspend fun suggestionsHome(): Home {
        val songs =
            NotPlayedPlaylist().songs().shuffled().takeIf {
                it.size > 9
            } ?: emptyList()
        return Home(songs, SUGGESTIONS, R.string.suggestion_songs)
    }

    override suspend fun genresHome(): Home {
        val genres = genreRepository.genres().shuffled()
        return Home(genres, GENRES, R.string.genres)
    }

    override suspend fun playlists(): Home {
        val playlist = playlistRepository.playlists()
        return Home(playlist, PLAYLISTS, R.string.playlists)
    }

    override suspend fun recentArtistsHome(): Home {
        val artists = lastAddedRepository.recentArtists().take(5)
        return Home(artists, RECENT_ARTISTS, R.string.recent_artists)
    }

    override suspend fun recentAlbumsHome(): Home {
        val albums = lastAddedRepository.recentAlbums().take(5)
        return Home(albums, RECENT_ALBUMS, R.string.recent_albums)
    }

    override suspend fun topAlbumsHome(): Home {
        val albums = playedTracksRepository.topAlbums().take(5)
        return Home(albums, TOP_ALBUMS, R.string.top_albums)
    }

    override suspend fun topArtistsHome(): Home {
        val artists = playedTracksRepository.topArtists().take(5)
        return Home(artists, TOP_ARTISTS, R.string.top_artists)
    }

    override suspend fun favoritePlaylistHome(): Home {
        val playlists =
            playlistRepository.favoritePlaylist(context.getString(R.string.favorites)).take(5)
        val songs = if (playlists.isNotEmpty())
            PlaylistSongsLoader.getPlaylistSongList(context, playlists[0])
        else emptyList()

        return Home(songs, FAVOURITES, R.string.favorites)
    }

    override fun songsFlow(): Flow<Result<List<Song>>> = flow {
        emit(Result.Loading)
        val data = songRepository.songs()
        if (data.isEmpty()) {
            emit(Result.Error)
        } else {
            emit(Result.Success(data))
        }
    }

    override fun albumsFlow(): Flow<Result<List<Album>>> = flow {
        emit(Result.Loading)
        val data = albumRepository.albums()
        if (data.isEmpty()) {
            emit(Result.Error)
        } else {
            emit(Result.Success(data))
        }
    }

    override fun artistsFlow(): Flow<Result<List<Artist>>> = flow {
        emit(Result.Loading)
        val data = artistRepository.artists()
        if (data.isEmpty()) {
            emit(Result.Error)
        } else {
            emit(Result.Success(data))
        }
    }

    override fun playlistsFlow(): Flow<Result<List<Playlist>>> = flow {
        emit(Result.Loading)
        val data = playlistRepository.playlists()
        if (data.isEmpty()) {
            emit(Result.Error)
        } else {
            emit(Result.Success(data))
        }
    }

    override fun genresFlow(): Flow<Result<List<Genre>>> = flow {
        emit(Result.Loading)
        val data = genreRepository.genres()
        if (data.isEmpty()) {
            emit(Result.Error)
        } else {
            emit(Result.Success(data))
        }
    }
}