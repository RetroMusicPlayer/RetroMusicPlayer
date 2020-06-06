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

package code.name.monkey.retromusic

import android.provider.BaseColumns
import android.provider.MediaStore

object Constants {

    const val RATE_ON_GOOGLE_PLAY =
        "https://play.google.com/store/apps/details?id=code.name.monkey.retromusic"
    const val TRANSLATE = "http://monkeycodeapp.oneskyapp.com/collaboration/project?id=238534"
    const val GITHUB_PROJECT = "https://github.com/h4h13/RetroMusicPlayer"
    const val TELEGRAM_CHANGE_LOG = "https://t.me/retromusiclog"
    const val USER_PROFILE = "profile.jpg"
    const val USER_BANNER = "banner.jpg"
    const val APP_INSTAGRAM_LINK = "https://www.instagram.com/retromusicapp/"
    const val APP_TELEGRAM_LINK = "https://t.me/retromusicapp/"
    const val APP_TWITTER_LINK = "https://twitter.com/retromusicapp"
    const val FAQ_LINK = "https://github.com/h4h13/RetroMusicPlayer/blob/master/FAQ.md"
    const val PINTEREST = "https://in.pinterest.com/retromusicapp/"

    const val IS_MUSIC =
        MediaStore.Audio.AudioColumns.IS_MUSIC + "=1" + " AND " + MediaStore.Audio.AudioColumns.TITLE + " != ''"

    val baseProjection = arrayOf(
        BaseColumns._ID, // 0
        MediaStore.Audio.AudioColumns.TITLE, // 1
        MediaStore.Audio.AudioColumns.TRACK, // 2
        MediaStore.Audio.AudioColumns.YEAR, // 3
        MediaStore.Audio.AudioColumns.DURATION, // 4
        MediaStore.Audio.AudioColumns.DATA, // 5
        MediaStore.Audio.AudioColumns.DATE_MODIFIED, // 6
        MediaStore.Audio.AudioColumns.ALBUM_ID, // 7
        MediaStore.Audio.AudioColumns.ALBUM, // 8
        MediaStore.Audio.AudioColumns.ARTIST_ID, // 9
        MediaStore.Audio.AudioColumns.ARTIST,// 10
        MediaStore.Audio.AudioColumns.COMPOSER// 11
    )
    const val NUMBER_OF_TOP_TRACKS = 99
}

const val EXTRA_SONG = "extra_songs"
const val EXTRA_PLAYLIST = "extra_list"
const val LIBRARY_CATEGORIES = "library_categories"
const val EXTRA_SONG_INFO = "extra_song_info"
const val DESATURATED_COLOR = "desaturated_color"
const val BLACK_THEME = "black_theme"
const val KEEP_SCREEN_ON = "keep_screen_on"
const val TOGGLE_HOME_BANNER = "toggle_home_banner"
const val NOW_PLAYING_SCREEN_ID = "now_playing_screen_id"
const val CAROUSEL_EFFECT = "carousel_effect"
const val COLORED_NOTIFICATION = "colored_notification"
const val CLASSIC_NOTIFICATION = "classic_notification"
const val GAPLESS_PLAYBACK = "gapless_playback"
const val ALBUM_ART_ON_LOCKSCREEN = "album_art_on_lockscreen"
const val BLURRED_ALBUM_ART = "blurred_album_art"
const val NEW_BLUR_AMOUNT = "new_blur_amount"
const val TOGGLE_HEADSET = "toggle_headset"
const val GENERAL_THEME = "general_theme"
const val CIRCULAR_ALBUM_ART = "circular_album_art"
const val USER_NAME = "user_name"
const val TOGGLE_FULL_SCREEN = "toggle_full_screen"
const val TOGGLE_VOLUME = "toggle_volume"
const val ROUND_CORNERS = "corner_window"
const val TOGGLE_GENRE = "toggle_genre"
const val PROFILE_IMAGE_PATH = "profile_image_path"
const val BANNER_IMAGE_PATH = "banner_image_path"
const val ADAPTIVE_COLOR_APP = "adaptive_color_app"
const val TOGGLE_SEPARATE_LINE = "toggle_separate_line"
const val HOME_ARTIST_GRID_STYLE = "home_artist_grid_style"
const val TOGGLE_ADD_CONTROLS = "toggle_add_controls"
const val ALBUM_COVER_STYLE = "album_cover_style_id"
const val ALBUM_COVER_TRANSFORM = "album_cover_transform"
const val TAB_TEXT_MODE = "tab_text_mode"
const val LANGUAGE_NAME = "language_name"
const val DIALOG_CORNER = "dialog_corner"
const val SLEEP_TIMER_FINISH_SONG = "sleep_timer_finish_song"
const val ALBUM_GRID_STYLE = "album_grid_style_home"
const val ARTIST_GRID_STYLE = "artist_grid_style_home"
const val SAF_SDCARD_URI = "saf_sdcard_uri"
const val SONG_SORT_ORDER = "song_sort_order"
const val SONG_GRID_SIZE = "song_grid_size"
const val GENRE_SORT_ORDER = "genre_sort_order"
const val LAST_PAGE = "last_start_page"
const val BLUETOOTH_PLAYBACK = "bluetooth_playback"
const val INITIALIZED_BLACKLIST = "initialized_blacklist"
const val ARTIST_SORT_ORDER = "artist_sort_order"
const val ARTIST_ALBUM_SORT_ORDER = "artist_album_sort_order"
const val ALBUM_SORT_ORDER = "album_sort_order"
const val ALBUM_SONG_SORT_ORDER = "album_song_sort_order"
const val ARTIST_SONG_SORT_ORDER = "artist_song_sort_order"
const val ALBUM_GRID_SIZE = "album_grid_size"
const val ALBUM_GRID_SIZE_LAND = "album_grid_size_land"
const val SONG_GRID_SIZE_LAND = "song_grid_size_land"
const val ARTIST_GRID_SIZE = "artist_grid_size"
const val ARTIST_GRID_SIZE_LAND = "artist_grid_size_land"
const val COLORED_APP_SHORTCUTS = "colored_app_shortcuts"
const val AUDIO_DUCKING = "audio_ducking"
const val LAST_ADDED_CUTOFF = "last_added_interval"
const val LAST_SLEEP_TIMER_VALUE = "last_sleep_timer_value"
const val NEXT_SLEEP_TIMER_ELAPSED_REALTIME = "next_sleep_timer_elapsed_real_time"
const val IGNORE_MEDIA_STORE_ARTWORK = "ignore_media_store_artwork"
const val LAST_CHANGELOG_VERSION = "last_changelog_version"
const val AUTO_DOWNLOAD_IMAGES_POLICY = "auto_download_images_policy"
const val START_DIRECTORY = "start_directory"
const val LOCK_SCREEN = "lock_screen"
const val ALBUM_DETAIL_SONG_SORT_ORDER = "album_detail_song_sort_order"
const val LYRICS_OPTIONS = "lyrics_tab_position"
const val CHOOSE_EQUALIZER = "choose_equalizer"
const val TOGGLE_SHUFFLE = "toggle_shuffle"
const val SONG_GRID_STYLE = "song_grid_style"
const val PAUSE_ON_ZERO_VOLUME = "pause_on_zero_volume"
const val FILTER_SONG = "filter_song"
const val EXPAND_NOW_PLAYING_PANEL = "expand_now_playing_panel"