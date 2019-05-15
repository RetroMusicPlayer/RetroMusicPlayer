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

    @JvmField
    val DISCORD_LINK = "https://discord.gg/qTecXXn"

    @JvmField
    val RETRO_MUSIC_PACKAGE_NAME = "code.name.monkey.retromusic"
    @JvmField
    val MUSIC_PACKAGE_NAME = "com.android.music"
    @JvmField
    val ACTION_TOGGLE_PAUSE = "$RETRO_MUSIC_PACKAGE_NAME.togglepause"
    @JvmField
    val ACTION_PLAY = "$RETRO_MUSIC_PACKAGE_NAME.play"
    @JvmField
    val ACTION_PLAY_PLAYLIST = "$RETRO_MUSIC_PACKAGE_NAME.play.playlist"
    @JvmField
    val ACTION_PAUSE = "$RETRO_MUSIC_PACKAGE_NAME.pause"
    @JvmField
    val ACTION_STOP = "$RETRO_MUSIC_PACKAGE_NAME.stop"
    @JvmField
    val ACTION_SKIP = "$RETRO_MUSIC_PACKAGE_NAME.skip"
    @JvmField
    val ACTION_REWIND = "$RETRO_MUSIC_PACKAGE_NAME.rewind"
    @JvmField
    val ACTION_QUIT = "$RETRO_MUSIC_PACKAGE_NAME.quitservice"
    @JvmField
    val ACTION_PENDING_QUIT = "$RETRO_MUSIC_PACKAGE_NAME.pendingquitservice"
    @JvmField
    val INTENT_EXTRA_PLAYLIST = RETRO_MUSIC_PACKAGE_NAME + "intentextra.playlist"
    @JvmField
    val INTENT_EXTRA_SHUFFLE_MODE = "$RETRO_MUSIC_PACKAGE_NAME.intentextra.shufflemode"
    @JvmField
    val APP_WIDGET_UPDATE = "$RETRO_MUSIC_PACKAGE_NAME.appwidgetupdate"
    @JvmField
    val EXTRA_APP_WIDGET_NAME = RETRO_MUSIC_PACKAGE_NAME + "app_widget_name"

    @JvmField
    val META_CHANGED = "$RETRO_MUSIC_PACKAGE_NAME.metachanged"
    @JvmField
    val QUEUE_CHANGED = "$RETRO_MUSIC_PACKAGE_NAME.queuechanged"
    @JvmField
    val PLAY_STATE_CHANGED = "$RETRO_MUSIC_PACKAGE_NAME.playstatechanged"
    @JvmField
    val REPEAT_MODE_CHANGED = "$RETRO_MUSIC_PACKAGE_NAME.repeatmodechanged"
    @JvmField
    val SHUFFLE_MODE_CHANGED = "$RETRO_MUSIC_PACKAGE_NAME.shufflemodechanged"
    @JvmField
    val MEDIA_STORE_CHANGED = "$RETRO_MUSIC_PACKAGE_NAME.mediastorechanged"
    const val RATE_ON_GOOGLE_PLAY = "https://play.google.com/store/apps/details?id=code.name.monkey.retromusic"
    const val PAYPAL_ME_URL = "https://www.paypal.me/h4h14"
    const val TRANSLATE = "http://monkeycodeapp.oneskyapp.com/collaboration/project?id=238534"
    const val GITHUB_PROJECT = "https://github.com/h4h13/RetroMusicPlayer"
    const val BASE_API_URL_KUGOU = "http://lyrics.kugou.com/"
    const val TELEGRAM_CHANGE_LOG = "https://t.me/retromusiclog"
    const val USER_PROFILE = "profile.jpg"
    const val USER_BANNER = "banner.jpg"
    const val APP_INSTAGRAM_LINK = "https://www.instagram.com/retromusicapp/"
    const val APP_TELEGRAM_LINK = "https://t.me/retromusicapp/"
    const val APP_TWITTER_LINK = "https://twitter.com/retromusicapp"
    const val FAQ_LINK = "https://github.com/h4h13/RetroMusicPlayer/blob/master/FAQ.md"
    const val PINTEREST = "https://in.pinterest.com/retromusicapp/"

    const val BASE_SELECTION = MediaStore.Audio.AudioColumns.IS_MUSIC + "=1" + " AND " + MediaStore.Audio.AudioColumns.TITLE + " != ''"

    @JvmField
    val BASE_PROJECTION = arrayOf(BaseColumns._ID, // 0
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
            MediaStore.Audio.AudioColumns.COMPOSER)// 11
    const val NUMBER_OF_TOP_TRACKS = 99


}
