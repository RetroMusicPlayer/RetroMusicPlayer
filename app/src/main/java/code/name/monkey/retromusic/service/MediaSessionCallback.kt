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

package code.name.monkey.retromusic.service

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.media.session.MediaSessionCompat
import code.name.monkey.retromusic.auto.AutoMediaIDHelper
import code.name.monkey.retromusic.helper.MusicPlayerRemote
import code.name.monkey.retromusic.helper.MusicPlayerRemote.cycleRepeatMode
import code.name.monkey.retromusic.helper.ShuffleHelper.makeShuffleList
import code.name.monkey.retromusic.model.Album
import code.name.monkey.retromusic.model.Artist
import code.name.monkey.retromusic.model.Playlist
import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.repository.*
import code.name.monkey.retromusic.service.MusicService.*
import code.name.monkey.retromusic.util.MusicUtil
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.util.*


/**
 * Created by hemanths on 2019-08-01.
 */

class MediaSessionCallback(
    private val context: Context,
    private val musicService: MusicService
) : MediaSessionCompat.Callback(), KoinComponent {

    private val songRepository by inject<SongRepository>()
    private val albumRepository by inject<AlbumRepository>()
    private val artistRepository by inject<ArtistRepository>()
    private val genreRepository by inject<GenreRepository>()
    private val playlistRepository by inject<PlaylistRepository>()
    private val topPlayedRepository by inject<TopPlayedRepository>()

    override fun onPlayFromMediaId(mediaId: String?, extras: Bundle?) {
        super.onPlayFromMediaId(mediaId, extras)
        val musicId = AutoMediaIDHelper.extractMusicID(mediaId!!)
        println(musicId)
        val itemId = musicId?.toLong() ?: -1
        val songs: ArrayList<Song> = ArrayList()
        val category = AutoMediaIDHelper.extractCategory(mediaId)
        when (category) {
            AutoMediaIDHelper.MEDIA_ID_MUSICS_BY_ALBUM -> {
                val album: Album = albumRepository.album(itemId)
                songs.addAll(album.songs)
                musicService.openQueue(songs, 0, true)
            }
            AutoMediaIDHelper.MEDIA_ID_MUSICS_BY_ARTIST -> {
                val artist: Artist = artistRepository.artist(itemId)
                songs.addAll(artist.songs)
                musicService.openQueue(songs, 0, true)
            }
            AutoMediaIDHelper.MEDIA_ID_MUSICS_BY_ALBUM_ARTIST -> {
                val artist: Artist =
                    artistRepository.albumArtist(albumRepository.album(itemId).albumArtist!!)
                songs.addAll(artist.songs)
                musicService.openQueue(songs, 0, true)
            }
            AutoMediaIDHelper.MEDIA_ID_MUSICS_BY_PLAYLIST -> {
                val playlist: Playlist = playlistRepository.playlist(itemId)
                songs.addAll(playlist.getSongs())
                musicService.openQueue(songs, 0, true)
            }
            AutoMediaIDHelper.MEDIA_ID_MUSICS_BY_GENRE -> {
                songs.addAll(genreRepository.songs(itemId))
                musicService.openQueue(songs, 0, true)
            }
            AutoMediaIDHelper.MEDIA_ID_MUSICS_BY_SHUFFLE -> {
                val allSongs: ArrayList<Song> = songRepository.songs() as ArrayList<Song>
                makeShuffleList(allSongs, -1)
                musicService.openQueue(allSongs, 0, true)
            }
            AutoMediaIDHelper.MEDIA_ID_MUSICS_BY_HISTORY,
            AutoMediaIDHelper.MEDIA_ID_MUSICS_BY_SUGGESTIONS,
            AutoMediaIDHelper.MEDIA_ID_MUSICS_BY_TOP_TRACKS,
            AutoMediaIDHelper.MEDIA_ID_MUSICS_BY_QUEUE -> {
                val tracks: List<Song> = when (category) {
                    AutoMediaIDHelper.MEDIA_ID_MUSICS_BY_HISTORY -> topPlayedRepository.recentlyPlayedTracks()
                    AutoMediaIDHelper.MEDIA_ID_MUSICS_BY_SUGGESTIONS -> topPlayedRepository.recentlyPlayedTracks()
                    AutoMediaIDHelper.MEDIA_ID_MUSICS_BY_TOP_TRACKS -> topPlayedRepository.recentlyPlayedTracks()
                    else -> musicService.playingQueue as List<Song>
                }
                songs.addAll(tracks)
                var songIndex = MusicUtil.indexOfSongInList(tracks, itemId)
                if (songIndex == -1) {
                    songIndex = 0
                }
                musicService.openQueue(songs, songIndex, true)
            }
            else -> {
            }
        }
        musicService.play()
    }


    override fun onPlay() {
        super.onPlay()
        musicService.play()
    }

    override fun onPause() {
        super.onPause()
        musicService.pause()
    }

    override fun onSkipToNext() {
        super.onSkipToNext()
        musicService.playNextSong(true)
    }

    override fun onSkipToPrevious() {
        super.onSkipToPrevious()
        musicService.back(true)
    }

    override fun onStop() {
        super.onStop()
        musicService.quit()
    }

    override fun onSeekTo(pos: Long) {
        super.onSeekTo(pos)
        musicService.seek(pos.toInt())
    }

    override fun onMediaButtonEvent(mediaButtonIntent: Intent): Boolean {
        return MediaButtonIntentReceiver.handleIntent(context, mediaButtonIntent)
    }

    override fun onCustomAction(action: String, extras: Bundle?) {
        when (action) {
            CYCLE_REPEAT -> {
                cycleRepeatMode()
                musicService.updateMediaSessionPlaybackState()
            }

            TOGGLE_SHUFFLE -> {
                musicService.toggleShuffle()
                musicService.updateMediaSessionPlaybackState()
            }
            TOGGLE_FAVORITE -> {
                MusicUtil.toggleFavorite(context, MusicPlayerRemote.currentSong)
                musicService.updateMediaSessionPlaybackState()
            }
            else -> {
                println("Unsupported action: $action")
            }
        }
    }

    private fun checkAndStartPlaying(songs: ArrayList<Song>, itemId: Long) {
        var songIndex = MusicUtil.indexOfSongInList(songs, itemId)
        if (songIndex == -1) {
            songIndex = 0
        }
        openQueue(songs, songIndex)
    }

    private fun openQueue(songs: ArrayList<Song>, index: Int, startPlaying: Boolean = true) {
        MusicPlayerRemote.openQueue(songs, index, startPlaying)
    }
}