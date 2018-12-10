package code.name.monkey.retromusic.service.notification


import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.view.View
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import code.name.monkey.appthemehelper.util.ColorUtil
import code.name.monkey.appthemehelper.util.MaterialValueHelper
import code.name.monkey.retromusic.Constants.ACTION_QUIT
import code.name.monkey.retromusic.Constants.ACTION_REWIND
import code.name.monkey.retromusic.Constants.ACTION_SKIP
import code.name.monkey.retromusic.Constants.ACTION_TOGGLE_PAUSE
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.glide.SongGlideRequest
import code.name.monkey.retromusic.glide.palette.BitmapPaletteWrapper
import code.name.monkey.retromusic.service.MusicService
import code.name.monkey.retromusic.ui.activities.MainActivity
import code.name.monkey.retromusic.util.PreferenceUtil
import code.name.monkey.retromusic.util.RetroColorUtil
import code.name.monkey.retromusic.util.RetroUtil
import code.name.monkey.retromusic.util.RetroUtil.createBitmap
import com.bumptech.glide.Glide
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target

class PlayingNotificationImpl : PlayingNotification() {

    private var target: Target<BitmapPaletteWrapper>? = null


    @Synchronized
    override fun update() {
        stopped = false

        val song = service.currentSong

        val isPlaying = service.isPlaying

        val notificationLayout = RemoteViews(service.packageName, R.layout.notification)
        val notificationLayoutBig = RemoteViews(service.packageName, R.layout.notification_big)

        if (TextUtils.isEmpty(song.title) && TextUtils.isEmpty(song.artistName)) {
            notificationLayout.setViewVisibility(R.id.media_titles, View.INVISIBLE)
        } else {
            notificationLayout.setViewVisibility(R.id.media_titles, View.VISIBLE)
            notificationLayout.setTextViewText(R.id.title, song.title)
            notificationLayout.setTextViewText(R.id.text, song.artistName)
        }

        if (TextUtils.isEmpty(song.title) && TextUtils.isEmpty(song.artistName) && TextUtils.isEmpty(song.albumName)) {
            notificationLayoutBig.setViewVisibility(R.id.media_titles, View.INVISIBLE)
        } else {
            notificationLayoutBig.setViewVisibility(R.id.media_titles, View.VISIBLE)
            notificationLayoutBig.setTextViewText(R.id.title, song.title)
            notificationLayoutBig.setTextViewText(R.id.text, song.artistName)
            notificationLayoutBig.setTextViewText(R.id.text2, song.albumName)
        }

        linkButtons(notificationLayout, notificationLayoutBig)

        val action = Intent(service, MainActivity::class.java)
        action.putExtra("expand", true)
        action.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP

        val clickIntent = PendingIntent
                .getActivity(service, 0, action, PendingIntent.FLAG_UPDATE_CURRENT)
        val deleteIntent = buildPendingIntent(service, ACTION_QUIT, null)

        val notification = NotificationCompat.Builder(service,
                PlayingNotification.NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentIntent(clickIntent)
                .setDeleteIntent(deleteIntent)
                .setCategory(NotificationCompat.CATEGORY_SERVICE)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setContent(notificationLayout)
                .setCustomBigContentView(notificationLayoutBig)
                .setOngoing(isPlaying)
                .build()

        val bigNotificationImageSize = service.resources
                .getDimensionPixelSize(R.dimen.notification_big_image_size)
        service.runOnUiThread {
            if (target != null) {
                Glide.clear(target!!)
            }
            target = SongGlideRequest.Builder.from(Glide.with(service), song)
                    .checkIgnoreMediaStore(service)
                    .generatePalette(service).build()
                    .into(object : SimpleTarget<BitmapPaletteWrapper>(bigNotificationImageSize,
                            bigNotificationImageSize) {
                        override fun onResourceReady(resource: BitmapPaletteWrapper,
                                                     glideAnimation: GlideAnimation<in BitmapPaletteWrapper>) {
                            update(resource.bitmap,
                                    if (PreferenceUtil.getInstance().isDominantColor)
                                        RetroColorUtil.getDominantColor(resource.bitmap, Color.TRANSPARENT)
                                    else
                                        RetroColorUtil.getColor(resource.palette, Color.TRANSPARENT))
                        }

                        override fun onLoadFailed(e: Exception?, errorDrawable: Drawable?) {
                            super.onLoadFailed(e, errorDrawable)
                            update(null, Color.WHITE)
                        }

                        private fun update(bitmap: Bitmap?, bgColor: Int) {
                            var bgColorFinal = bgColor
                            if (bitmap != null) {
                                notificationLayout.setImageViewBitmap(R.id.image, bitmap)
                                notificationLayoutBig.setImageViewBitmap(R.id.image, bitmap)
                            } else {
                                notificationLayout.setImageViewResource(R.id.image, R.drawable.default_album_art)
                                notificationLayoutBig
                                        .setImageViewResource(R.id.image, R.drawable.default_album_art)
                            }

                            if (!PreferenceUtil.getInstance().coloredNotification()) {
                                bgColorFinal = Color.WHITE
                            }
                            setBackgroundColor(bgColorFinal)
                            setNotificationContent(ColorUtil.isColorLight(bgColorFinal))

                            if (stopped) {
                                return  // notification has been stopped before loading was finished
                            }
                            updateNotifyModeAndPostNotification(notification)
                        }

                        private fun setBackgroundColor(color: Int) {
                            notificationLayout.setInt(R.id.root, "setBackgroundColor", color)
                            notificationLayoutBig.setInt(R.id.root, "setBackgroundColor", color)
                        }

                        private fun setNotificationContent(dark: Boolean) {
                            val primary = MaterialValueHelper.getPrimaryTextColor(service, dark)
                            val secondary = MaterialValueHelper.getSecondaryTextColor(service, dark)

                            val close = createBitmap(
                                    RetroUtil.getTintedVectorDrawable(service, R.drawable.ic_close_white_24dp, primary)!!,
                                    1.5f)
                            val prev = createBitmap(
                                    RetroUtil.getTintedVectorDrawable(service, R.drawable.ic_skip_previous_white_24dp,
                                            primary)!!, 1.5f)
                            val next = createBitmap(
                                    RetroUtil.getTintedVectorDrawable(service, R.drawable.ic_skip_next_white_24dp,
                                            primary)!!, 1.5f)
                            val playPause = createBitmap(RetroUtil.getTintedVectorDrawable(service,
                                    if (isPlaying)
                                        R.drawable.ic_pause_white_24dp
                                    else
                                        R.drawable.ic_play_arrow_white_24dp, primary)!!, 1.5f)

                            notificationLayout.setTextColor(R.id.title, primary)
                            notificationLayout.setTextColor(R.id.text, secondary)
                            notificationLayout.setImageViewBitmap(R.id.action_prev, prev)
                            notificationLayout.setImageViewBitmap(R.id.action_next, next)
                            notificationLayout.setImageViewBitmap(R.id.action_play_pause, playPause)

                            notificationLayoutBig.setTextColor(R.id.title, primary)
                            notificationLayoutBig.setTextColor(R.id.text, secondary)
                            notificationLayoutBig.setTextColor(R.id.text2, secondary)

                            notificationLayoutBig.setImageViewBitmap(R.id.action_quit, close)
                            notificationLayoutBig.setImageViewBitmap(R.id.action_prev, prev)
                            notificationLayoutBig.setImageViewBitmap(R.id.action_next, next)
                            notificationLayoutBig.setImageViewBitmap(R.id.action_play_pause, playPause)

                        }
                    })
        }
    }


    private fun linkButtons(notificationLayout: RemoteViews,
                            notificationLayoutBig: RemoteViews) {
        var pendingIntent: PendingIntent

        val serviceName = ComponentName(service, MusicService::class.java)

        // Previous track
        pendingIntent = buildPendingIntent(service, ACTION_REWIND, serviceName)
        notificationLayout.setOnClickPendingIntent(R.id.action_prev, pendingIntent)
        notificationLayoutBig.setOnClickPendingIntent(R.id.action_prev, pendingIntent)

        // Play and pause
        pendingIntent = buildPendingIntent(service, ACTION_TOGGLE_PAUSE, serviceName)
        notificationLayout.setOnClickPendingIntent(R.id.action_play_pause, pendingIntent)
        notificationLayoutBig.setOnClickPendingIntent(R.id.action_play_pause, pendingIntent)

        // Next track
        pendingIntent = buildPendingIntent(service, ACTION_SKIP, serviceName)
        notificationLayout.setOnClickPendingIntent(R.id.action_next, pendingIntent)
        notificationLayoutBig.setOnClickPendingIntent(R.id.action_next, pendingIntent)

        // Close
        pendingIntent = buildPendingIntent(service, ACTION_QUIT, serviceName)
        notificationLayoutBig.setOnClickPendingIntent(R.id.action_quit, pendingIntent)
    }

    private fun buildPendingIntent(context: Context, action: String,
                                   serviceName: ComponentName?): PendingIntent {
        val intent = Intent(action)
        intent.component = serviceName
        return PendingIntent.getService(context, 0, intent, 0)
    }
}