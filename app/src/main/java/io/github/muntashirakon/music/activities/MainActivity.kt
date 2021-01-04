/*
 * Copyright (c) 2020 Hemanth Savarla.
 *
 * Licensed under the GNU General Public License v3
 *
 * This is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 */
package io.github.muntashirakon.music.activities

import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.ui.NavigationUI
import io.github.muntashirakon.music.ADAPTIVE_COLOR_APP
import io.github.muntashirakon.music.ALBUM_COVER_STYLE
import io.github.muntashirakon.music.ALBUM_COVER_TRANSFORM
import io.github.muntashirakon.music.BANNER_IMAGE_PATH
import io.github.muntashirakon.music.BLACK_THEME
import io.github.muntashirakon.music.CAROUSEL_EFFECT
import io.github.muntashirakon.music.CIRCULAR_ALBUM_ART
import io.github.muntashirakon.music.DESATURATED_COLOR
import io.github.muntashirakon.music.EXTRA_SONG_INFO
import io.github.muntashirakon.music.GENERAL_THEME
import io.github.muntashirakon.music.HOME_ARTIST_GRID_STYLE
import io.github.muntashirakon.music.KEEP_SCREEN_ON
import io.github.muntashirakon.music.LANGUAGE_NAME
import io.github.muntashirakon.music.LIBRARY_CATEGORIES
import io.github.muntashirakon.music.NOW_PLAYING_SCREEN_ID
import io.github.muntashirakon.music.PROFILE_IMAGE_PATH
import io.github.muntashirakon.music.R
import io.github.muntashirakon.music.ROUND_CORNERS
import io.github.muntashirakon.music.TAB_TEXT_MODE
import io.github.muntashirakon.music.TOGGLE_ADD_CONTROLS
import io.github.muntashirakon.music.TOGGLE_FULL_SCREEN
import io.github.muntashirakon.music.TOGGLE_GENRE
import io.github.muntashirakon.music.TOGGLE_HOME_BANNER
import io.github.muntashirakon.music.TOGGLE_SEPARATE_LINE
import io.github.muntashirakon.music.TOGGLE_VOLUME
import io.github.muntashirakon.music.USER_NAME
import io.github.muntashirakon.music.activities.base.AbsSlidingMusicPanelActivity
import io.github.muntashirakon.music.extensions.extra
import io.github.muntashirakon.music.extensions.findNavController
import io.github.muntashirakon.music.helper.MusicPlayerRemote
import io.github.muntashirakon.music.helper.SearchQueryHelper.getSongs
import io.github.muntashirakon.music.model.CategoryInfo
import io.github.muntashirakon.music.model.Song
import io.github.muntashirakon.music.repository.PlaylistSongsLoader
import io.github.muntashirakon.music.service.MusicService
import io.github.muntashirakon.music.util.PreferenceUtil
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get

class MainActivity : AbsSlidingMusicPanelActivity(), OnSharedPreferenceChangeListener {
    companion object {
        const val TAG = "MainActivity"
        const val EXPAND_PANEL = "expand_panel"
    }

    override fun createContentView(): View {
        return wrapSlidingMusicPanel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setDrawUnderStatusBar()
        super.onCreate(savedInstanceState)
        setStatusbarColorAuto()
        setNavigationbarColorAuto()
        setLightNavigationBar(true)
        setTaskDescriptionColorAuto()
        hideStatusBar()
        updateTabs()

        setupNavigationController()
        if (!hasPermissions()) {
            findNavController(R.id.fragment_container).navigate(R.id.permissionFragment)
        }
    }

    private fun setupNavigationController() {
        val navController = findNavController(R.id.fragment_container)
        val navInflater = navController.navInflater
        val navGraph = navInflater.inflate(R.navigation.main_graph)

        val categoryInfo: CategoryInfo = PreferenceUtil.libraryCategory.first { it.visible }
        if (categoryInfo.visible) {
            navGraph.startDestination = categoryInfo.category.id
        }
        navController.graph = navGraph
        NavigationUI.setupWithNavController(getBottomNavigationView(), navController)
    }

    override fun onSupportNavigateUp(): Boolean =
        findNavController(R.id.fragment_container).navigateUp()

    override fun onResume() {
        super.onResume()
        PreferenceUtil.registerOnSharedPreferenceChangedListener(this)
        val expand = extra<Boolean>(EXPAND_PANEL).value ?: false
        if (expand && PreferenceUtil.isExpandPanel) {
            expandPanel()
            intent.removeExtra(EXPAND_PANEL)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        PreferenceUtil.unregisterOnSharedPreferenceChangedListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == GENERAL_THEME || key == BLACK_THEME || key == ADAPTIVE_COLOR_APP || key == USER_NAME || key == TOGGLE_FULL_SCREEN || key == TOGGLE_VOLUME || key == ROUND_CORNERS || key == CAROUSEL_EFFECT || key == NOW_PLAYING_SCREEN_ID || key == TOGGLE_GENRE || key == BANNER_IMAGE_PATH || key == PROFILE_IMAGE_PATH || key == CIRCULAR_ALBUM_ART || key == KEEP_SCREEN_ON || key == TOGGLE_SEPARATE_LINE || key == TOGGLE_HOME_BANNER || key == TOGGLE_ADD_CONTROLS || key == ALBUM_COVER_STYLE || key == HOME_ARTIST_GRID_STYLE || key == ALBUM_COVER_TRANSFORM || key == DESATURATED_COLOR || key == EXTRA_SONG_INFO || key == TAB_TEXT_MODE || key == LANGUAGE_NAME || key == LIBRARY_CATEGORIES) {
            postRecreate()
        }
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        intent ?: return
        handlePlaybackIntent(intent)
    }

    private fun handlePlaybackIntent(intent: Intent) {
        lifecycleScope.launch(IO) {
            val uri: Uri? = intent.data
            val mimeType: String? = intent.type
            var handled = false
            if (intent.action != null &&
                intent.action == MediaStore.INTENT_ACTION_MEDIA_PLAY_FROM_SEARCH
            ) {
                val songs: List<Song> = getSongs(intent.extras!!)
                if (MusicPlayerRemote.shuffleMode == MusicService.SHUFFLE_MODE_SHUFFLE) {
                    MusicPlayerRemote.openAndShuffleQueue(songs, true)
                } else {
                    MusicPlayerRemote.openQueue(songs, 0, true)
                }
                handled = true
            }
            if (uri != null && uri.toString().isNotEmpty()) {
                MusicPlayerRemote.playFromUri(uri)
                handled = true
            } else if (MediaStore.Audio.Playlists.CONTENT_TYPE == mimeType) {
                val id = parseLongFromIntent(intent, "playlistId", "playlist")
                if (id >= 0L) {
                    val position: Int = intent.getIntExtra("position", 0)
                    val songs: List<Song> = PlaylistSongsLoader.getPlaylistSongList(get(), id)
                    MusicPlayerRemote.openQueue(songs, position, true)
                    handled = true
                }
            } else if (MediaStore.Audio.Albums.CONTENT_TYPE == mimeType) {
                val id = parseLongFromIntent(intent, "albumId", "album")
                if (id >= 0L) {
                    val position: Int = intent.getIntExtra("position", 0)
                    val songs = libraryViewModel.albumById(id).songs
                    MusicPlayerRemote.openQueue(
                        songs,
                        position,
                        true
                    )
                    handled = true
                }
            } else if (MediaStore.Audio.Artists.CONTENT_TYPE == mimeType) {
                val id = parseLongFromIntent(intent, "artistId", "artist")
                if (id >= 0L) {
                    val position: Int = intent.getIntExtra("position", 0)
                    val songs: List<Song> = libraryViewModel.artistById(id).songs
                    MusicPlayerRemote.openQueue(
                        songs,
                        position,
                        true
                    )
                    handled = true
                }
            }
            if (handled) {
                setIntent(Intent())
            }
        }
    }

    private fun parseLongFromIntent(
        intent: Intent,
        longKey: String,
        stringKey: String
    ): Long {
        var id = intent.getLongExtra(longKey, -1)
        if (id < 0) {
            val idString = intent.getStringExtra(stringKey)
            if (idString != null) {
                try {
                    id = idString.toLong()
                } catch (e: NumberFormatException) {
                    println(e.message)
                }
            }
        }
        return id
    }
}
