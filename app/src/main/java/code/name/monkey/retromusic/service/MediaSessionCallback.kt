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
import android.provider.MediaStore
import android.support.v4.media.session.MediaSessionCompat
import android.text.TextUtils
import code.name.monkey.retromusic.auto.AutoMediaIDHelper
import code.name.monkey.retromusic.helper.MusicPlayerRemote
import code.name.monkey.retromusic.helper.MusicPlayerRemote.cycleRepeatMode
import code.name.monkey.retromusic.helper.ShuffleHelper
import code.name.monkey.retromusic.loaders.*
import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.providers.MusicPlaybackQueueStore
import code.name.monkey.retromusic.service.MusicService.*
import code.name.monkey.retromusic.util.MusicUtil
import java.util.*


/**
 * Created by hemanths on 2019-08-01.
 */

class MediaSessionCallback(private val context: Context,
                           private val musicService: MusicService
) : MediaSessionCompat.Callback() {

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

    override fun onPlayFromMediaId(mediaId: String?, extras: Bundle?) {
        super.onPlayFromMediaId(mediaId, extras)

        val musicId = mediaId?.let { AutoMediaIDHelper.extractMusicID(it) }
        val itemId = musicId?.toInt() ?: -1
        val songs = arrayListOf<Song>()
        when (val category = mediaId?.let { AutoMediaIDHelper.extractCategory(it) }) {
            AutoMediaIDHelper.MEDIA_ID_MUSICS_BY_ALBUM -> {
                val album = AlbumLoader.getAlbum(context, itemId)
                album.songs?.let { songs.addAll(it) }
                openQueue(songs, 0, true)
            }
            AutoMediaIDHelper.MEDIA_ID_MUSICS_BY_ARTIST -> {
                val artist = ArtistLoader.getArtist(context, itemId)
                songs.addAll(artist.songs)
                openQueue(songs, 0, true)
            }
            AutoMediaIDHelper.MEDIA_ID_MUSICS_BY_PLAYLIST -> {
                val playlist = PlaylistLoader.getPlaylist(context, itemId)
                songs.addAll(playlist.getSongs(context))
                openQueue(songs, 0, true)
            }
            AutoMediaIDHelper.MEDIA_ID_MUSICS_BY_GENRE -> {
                songs.addAll(GenreLoader.getSongs(context, itemId))
                openQueue(songs, 0, true)
            }
            AutoMediaIDHelper.MEDIA_ID_MUSICS_BY_HISTORY,
            AutoMediaIDHelper.MEDIA_ID_MUSICS_BY_TOP_TRACKS,
            AutoMediaIDHelper.MEDIA_ID_MUSICS_BY_QUEUE -> {
                val tracks: List<Song>
                tracks = when (category) {
                    AutoMediaIDHelper.MEDIA_ID_MUSICS_BY_HISTORY -> TopAndRecentlyPlayedTracksLoader.getRecentlyPlayedTracks(context)
                    AutoMediaIDHelper.MEDIA_ID_MUSICS_BY_TOP_TRACKS -> TopAndRecentlyPlayedTracksLoader.getTopTracks(context)
                    else -> MusicPlaybackQueueStore.getInstance(context).savedOriginalPlayingQueue
                }
                songs.addAll(tracks)
                var songIndex = MusicUtil.indexOfSongInList(tracks, itemId)
                if (songIndex == -1) {
                    songIndex = 0
                }
                openQueue(songs, songIndex, true)
            }
            AutoMediaIDHelper.MEDIA_ID_MUSICS_BY_SHUFFLE -> {
                val allSongs = SongLoader.getAllSongs(context)
                ShuffleHelper.makeShuffleList(allSongs, -1)
                openQueue(allSongs, 0, true)
            }
        }

    }

    override fun onPlayFromSearch(query: String?, extras: Bundle?) {
        print("query: $query -> extras: ${extras.toString()}")
        if (TextUtils.isEmpty(query)) {
            if (MusicPlayerRemote.playingQueue.isEmpty()) {
                openQueue(SongLoader.getAllSongs(context), 0, true);
            } else {
                MusicPlayerRemote.resumePlaying()
            }
        } else {
            handlePlayFromSearch(query, extras)

        }
    }

    private fun handlePlayFromSearch(query: String?, extras: Bundle?) {
        val mediaFocus: String? = extras?.getString(MediaStore.EXTRA_MEDIA_FOCUS)
        println(mediaFocus)
        when (mediaFocus) {
            MediaStore.Audio.Artists.ENTRY_CONTENT_TYPE -> {
                extras.getString(MediaStore.EXTRA_MEDIA_ARTIST)?.let { artist ->
                    println("Artist name $artist")
                }
            }
            MediaStore.Audio.Albums.ENTRY_CONTENT_TYPE -> {
                extras.getString(MediaStore.EXTRA_MEDIA_ALBUM)?.let { album ->
                    println("Artist name $album")
                }
            }
            MediaStore.Audio.Genres.ENTRY_CONTENT_TYPE -> {
                extras.getString(MediaStore.EXTRA_MEDIA_GENRE)?.let { genre ->
                    println("Artist name $genre")
                }
            }
        }
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

    private fun checkAndStartPlaying(songs: ArrayList<Song>, itemId: Int) {
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