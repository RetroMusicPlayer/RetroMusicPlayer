package code.name.monkey.retromusic.appwidgets

import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.view.View
import android.widget.RemoteViews
import code.name.monkey.appthemehelper.util.MaterialValueHelper
import code.name.monkey.retromusic.Constants
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.appwidgets.base.BaseAppWidget
import code.name.monkey.retromusic.glide.SongGlideRequest
import code.name.monkey.retromusic.glide.palette.BitmapPaletteWrapper
import code.name.monkey.retromusic.service.MusicService
import code.name.monkey.retromusic.ui.activities.MainActivity
import code.name.monkey.retromusic.util.RetroUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target

class AppWidgetSmall : BaseAppWidget() {
    private var target: Target<BitmapPaletteWrapper>? = null // for cancellation

    /**
     * Initialize given widgets to default state, where we launch Music on default click and hide
     * actions if service not running.
     */
    override fun defaultAppWidget(context: Context, appWidgetIds: IntArray) {
        val appWidgetView = RemoteViews(context.packageName, R.layout.app_widget_small)

        appWidgetView.setViewVisibility(R.id.media_titles, View.INVISIBLE)
        appWidgetView.setImageViewResource(R.id.image, R.drawable.default_album_art)
        appWidgetView.setImageViewBitmap(R.id.button_next, BaseAppWidget.createBitmap(RetroUtil.getTintedVectorDrawable(context, R.drawable.ic_skip_next_white_24dp, MaterialValueHelper.getSecondaryTextColor(context, true))!!, 1f))
        appWidgetView.setImageViewBitmap(R.id.button_prev, BaseAppWidget.createBitmap(RetroUtil.getTintedVectorDrawable(context, R.drawable.ic_skip_previous_white_24dp, MaterialValueHelper.getSecondaryTextColor(context, true))!!, 1f))
        appWidgetView.setImageViewBitmap(R.id.button_toggle_play_pause, BaseAppWidget.createBitmap(RetroUtil.getTintedVectorDrawable(context, R.drawable.ic_play_arrow_white_24dp, MaterialValueHelper.getSecondaryTextColor(context, true))!!, 1f))

        linkButtons(context, appWidgetView)
        pushUpdate(context, appWidgetIds, appWidgetView)
    }

    /**
     * Update all active widget instances by pushing changes
     */
    override fun performUpdate(service: MusicService, appWidgetIds: IntArray?) {
        val appWidgetView = RemoteViews(service.packageName,
                R.layout.app_widget_small)

        val isPlaying = service.isPlaying
        val song = service.currentSong

        // Set the titles and artwork
        if (TextUtils.isEmpty(song.title) && TextUtils.isEmpty(song.artistName)) {
            appWidgetView.setViewVisibility(R.id.media_titles, View.INVISIBLE)
        } else {
            if (TextUtils.isEmpty(song.title) || TextUtils.isEmpty(song.artistName)) {
                appWidgetView.setTextViewText(R.id.text_separator, "")
            } else {
                appWidgetView.setTextViewText(R.id.text_separator, "•")
            }

            appWidgetView.setViewVisibility(R.id.media_titles, View.VISIBLE)
            appWidgetView.setTextViewText(R.id.title, song.title)
            appWidgetView.setTextViewText(R.id.text, song.artistName)
        }

        // Link actions buttons to intents
        linkButtons(service, appWidgetView)

        if (imageSize == 0) {
            imageSize = service.resources.getDimensionPixelSize(R.dimen.app_widget_small_image_size)
        }
        if (cardRadius == 0f) {
            cardRadius = service.resources.getDimension(R.dimen.app_widget_card_radius)
        }

        // Load the album cover async and push the update on completion
        val appContext = service.applicationContext
        service.runOnUiThread {
            if (target != null) {
                Glide.clear(target!!)
            }
            target = SongGlideRequest.Builder.from(Glide.with(appContext), song)
                    .checkIgnoreMediaStore(appContext)
                    .generatePalette(service).build()
                    .centerCrop()
                    .into(object : SimpleTarget<BitmapPaletteWrapper>(imageSize, imageSize) {
                        override fun onResourceReady(resource: BitmapPaletteWrapper,
                                                     glideAnimation: GlideAnimation<in BitmapPaletteWrapper>) {
                            val palette = resource.palette
                            update(resource.bitmap, palette.getVibrantColor(palette
                                    .getMutedColor(MaterialValueHelper.getSecondaryTextColor(appContext, true))))
                        }

                        override fun onLoadFailed(e: Exception?, errorDrawable: Drawable?) {
                            super.onLoadFailed(e, errorDrawable)
                            update(null, MaterialValueHelper.getSecondaryTextColor(appContext, true))
                        }

                        private fun update(bitmap: Bitmap?, color: Int) {
                            // Set correct drawable for pause state
                            val playPauseRes = if (isPlaying)
                                R.drawable.ic_pause_white_24dp
                            else
                                R.drawable.ic_play_arrow_white_24dp
                            appWidgetView.setImageViewBitmap(R.id.button_toggle_play_pause, BaseAppWidget.Companion.createBitmap(RetroUtil.getTintedVectorDrawable(service, playPauseRes, color)!!, 1f))

                            // Set prev/next button drawables
                            appWidgetView.setImageViewBitmap(R.id.button_next, BaseAppWidget.Companion.createBitmap(RetroUtil.getTintedVectorDrawable(service, R.drawable.ic_skip_next_white_24dp, color)!!, 1f))
                            appWidgetView.setImageViewBitmap(R.id.button_prev, BaseAppWidget.Companion.createBitmap(RetroUtil.getTintedVectorDrawable(service, R.drawable.ic_skip_previous_white_24dp, color)!!, 1f))

                            val image = getAlbumArtDrawable(service.resources, bitmap)
                            val roundedBitmap = BaseAppWidget.createRoundedBitmap(image, imageSize, imageSize, cardRadius, 0f, 0f, 0f)
                            appWidgetView.setImageViewBitmap(R.id.image, roundedBitmap)

                            pushUpdate(appContext, appWidgetIds, appWidgetView)
                        }
                    })
        }
    }

    /**
     * Link up various button actions using [PendingIntent].
     */
    private fun linkButtons(context: Context, views: RemoteViews) {
        val action = Intent(context, MainActivity::class.java).putExtra("expand", true)
        var pendingIntent: PendingIntent

        val serviceName = ComponentName(context, MusicService::class.java)

        // Home
        action.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        pendingIntent = PendingIntent.getActivity(context, 0, action, 0)
        views.setOnClickPendingIntent(R.id.image, pendingIntent)
        views.setOnClickPendingIntent(R.id.media_titles, pendingIntent)

        // Previous track
        pendingIntent = buildPendingIntent(context, Constants.ACTION_REWIND, serviceName)
        views.setOnClickPendingIntent(R.id.button_prev, pendingIntent)

        // Play and pause
        pendingIntent = buildPendingIntent(context, Constants.ACTION_TOGGLE_PAUSE, serviceName)
        views.setOnClickPendingIntent(R.id.button_toggle_play_pause, pendingIntent)

        // Next track
        pendingIntent = buildPendingIntent(context, Constants.ACTION_SKIP, serviceName)
        views.setOnClickPendingIntent(R.id.button_next, pendingIntent)
    }

    companion object {

        const val NAME:String = "app_widget_small"

        private var mInstance: AppWidgetSmall? = null
        private var imageSize = 0
        private var cardRadius = 0f

        val instance: AppWidgetSmall
            @Synchronized get() {
                if (mInstance == null) {
                    mInstance = AppWidgetSmall()
                }
                return mInstance!!
            }
    }
}
