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

package code.name.monkey.retromusic.service.notification

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.NotificationCompat
import androidx.core.text.HtmlCompat
import androidx.media.app.NotificationCompat.MediaStyle
import code.name.monkey.appthemehelper.util.VersionUtils
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.activities.MainActivity
import code.name.monkey.retromusic.db.PlaylistEntity
import code.name.monkey.retromusic.db.toSongEntity
import code.name.monkey.retromusic.glide.GlideApp
import code.name.monkey.retromusic.glide.RetroGlideExtension
import code.name.monkey.retromusic.glide.palette.BitmapPaletteWrapper
import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.service.MusicService
import code.name.monkey.retromusic.service.MusicService.*
import code.name.monkey.retromusic.util.MusicUtil
import code.name.monkey.retromusic.util.PreferenceUtil
import code.name.monkey.retromusic.util.RetroColorUtil
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@SuppressLint("RestrictedApi")
class PlayingNotificationImpl(
    val context: Context,
    mediaSessionToken: MediaSessionCompat.Token
) : PlayingNotification(context) {

    init {
        val action = Intent(context, MainActivity::class.java)
        action.putExtra(MainActivity.EXPAND_PANEL, PreferenceUtil.isExpandPanel)
        action.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        val clickIntent =
            PendingIntent.getActivity(
                context,
                0,
                action,
                PendingIntent.FLAG_UPDATE_CURRENT or if (VersionUtils.hasMarshmallow())
                    PendingIntent.FLAG_IMMUTABLE
                else 0
            )

        val serviceName = ComponentName(context, MusicService::class.java)
        val intent = Intent(ACTION_QUIT)
        intent.component = serviceName
        val deleteIntent = PendingIntent.getService(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or (if (VersionUtils.hasMarshmallow())
                PendingIntent.FLAG_IMMUTABLE
            else 0)
        )
        val toggleFavorite = buildFavoriteAction(false)
        val playPauseAction = buildPlayAction(true)
        val previousAction = NotificationCompat.Action(
            R.drawable.ic_skip_previous_round_white_32dp,
            context.getString(R.string.action_previous),
            retrievePlaybackAction(ACTION_REWIND)
        )
        val nextAction = NotificationCompat.Action(
            R.drawable.ic_skip_next_round_white_32dp,
            context.getString(R.string.action_next),
            retrievePlaybackAction(ACTION_SKIP)
        )
        val dismissAction = NotificationCompat.Action(
            R.drawable.ic_close,
            context.getString(R.string.customactivityoncrash_error_activity_error_details_close),
            retrievePlaybackAction(ACTION_QUIT)
        )
        setSmallIcon(R.drawable.ic_notification)
        setContentIntent(clickIntent)
        setDeleteIntent(deleteIntent)
        setShowWhen(false)
        addAction(toggleFavorite)
        addAction(previousAction)
        addAction(playPauseAction)
        addAction(nextAction)
        addAction(dismissAction)

        setStyle(
            MediaStyle()
                .setMediaSession(mediaSessionToken)
                .setShowActionsInCompactView(1, 2, 3)
        )
        setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        if (Build.VERSION.SDK_INT <=
            Build.VERSION_CODES.O && PreferenceUtil.isColoredNotification
        ) {
            this.color = color
        }
    }

    override fun updateMetadata(song: Song, onUpdate: () -> Unit) {
        setContentTitle(song.title)
        setContentText(
            HtmlCompat.fromHtml(
                "<b>" + song.albumName + "</b>",
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
        )
        val bigNotificationImageSize = context.resources
            .getDimensionPixelSize(R.dimen.notification_big_image_size)
        GlideApp.with(context).asBitmapPalette().songCoverOptions(song)
            .load(RetroGlideExtension.getSongModel(song))
            //.checkIgnoreMediaStore()
            .centerCrop()
            .into(object : CustomTarget<BitmapPaletteWrapper>(
                bigNotificationImageSize,
                bigNotificationImageSize
            ) {
                override fun onResourceReady(
                    resource: BitmapPaletteWrapper,
                    transition: Transition<in BitmapPaletteWrapper>?
                ) {
                    setLargeIcon(
                        resource.bitmap
                    )
                    if (Build.VERSION.SDK_INT <=
                        Build.VERSION_CODES.O && PreferenceUtil.isColoredNotification
                    ) {
                        color = RetroColorUtil.getColor(resource.palette, Color.TRANSPARENT)
                    }
                    onUpdate()
                }

                override fun onLoadFailed(errorDrawable: Drawable?) {
                    super.onLoadFailed(errorDrawable)
                    setLargeIcon(null)
                    onUpdate()
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    setLargeIcon(null)
                    onUpdate()
                }
            })
        updateFavorite(song, onUpdate)
    }

    private fun buildPlayAction(isPlaying: Boolean): NotificationCompat.Action {
        val playButtonResId =
            if (isPlaying) R.drawable.ic_pause_white_48dp else R.drawable.ic_play_arrow_white_48dp
        return NotificationCompat.Action.Builder(
            playButtonResId,
            context.getString(R.string.action_play_pause),
            retrievePlaybackAction(ACTION_TOGGLE_PAUSE)
        ).build()
    }

    private fun buildFavoriteAction(isFavorite: Boolean): NotificationCompat.Action {
        val favoriteResId =
            if (isFavorite) R.drawable.ic_favorite else R.drawable.ic_favorite_border
        return NotificationCompat.Action.Builder(
            favoriteResId,
            context.getString(R.string.action_toggle_favorite),
            retrievePlaybackAction(TOGGLE_FAVORITE)
        ).build()
    }

    private fun buildDismissAction(): NotificationCompat.Action {
        return NotificationCompat.Action.Builder(
            R.drawable.ic_close,
            context.getString(R.string.customactivityoncrash_error_activity_error_details_close),
            retrievePlaybackAction(ACTION_QUIT)
        ).build()
    }

    override fun setPlaying(isPlaying: Boolean) {
        mActions[2] = buildPlayAction(isPlaying)
        // Show dismiss action if we are not playing but only for A12+, as we can't call stopForeground(false)
        // on A12 which would result in crashes when we call startForeground after that
        if (!isPlaying) {
            addAction(buildDismissAction())
        } else {
            if (mActions.size == 5) mActions.removeAt(4)
        }
    }

    override fun updateFavorite(song: Song, onUpdate: () -> Unit) {
        GlobalScope.launch(Dispatchers.IO) {
            val playlist: PlaylistEntity = MusicUtil.repository.favoritePlaylist()
            val isFavorite = if (playlist != null) {
                val songEntity = song.toSongEntity(playlist.playListId)
                MusicUtil.repository.isFavoriteSong(songEntity).isNotEmpty()
            } else false
            withContext(Dispatchers.Main) {
                mActions[0] = buildFavoriteAction(isFavorite)
                onUpdate()
            }
        }
    }

    private fun retrievePlaybackAction(action: String): PendingIntent {
        val serviceName = ComponentName(context, MusicService::class.java)
        val intent = Intent(action)
        intent.component = serviceName
        return PendingIntent.getService(
            context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or
                    if (VersionUtils.hasMarshmallow()) PendingIntent.FLAG_IMMUTABLE
                    else 0
        )
    }

    companion object {

        fun from(
            context: Context,
            notificationManager: NotificationManager,
            mediaSession: MediaSessionCompat
        ): PlayingNotification {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNotificationChannel(context, notificationManager)
            }
            return PlayingNotificationImpl(context, mediaSession.sessionToken)
        }
    }
}