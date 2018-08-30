package code.name.monkey.retromusic.appwidgets;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.RemoteViews;
import code.name.monkey.appthemehelper.util.MaterialValueHelper;
import code.name.monkey.retromusic.Constants;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.appwidgets.base.BaseAppWidget;
import code.name.monkey.retromusic.glide.SongGlideRequest;
import code.name.monkey.retromusic.model.Song;
import code.name.monkey.retromusic.service.MusicService;
import code.name.monkey.retromusic.ui.activities.MainActivity;
import code.name.monkey.retromusic.util.RetroUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;

public class AppWidgetBig extends BaseAppWidget {

  public static final String NAME = "app_widget_big";

  private static AppWidgetBig mInstance;
  private Target<Bitmap> target; // for cancellation

  public static synchronized AppWidgetBig getInstance() {
    if (mInstance == null) {
      mInstance = new AppWidgetBig();
    }
    return mInstance;
  }

  /**
   * Initialize given widgets to default state, where we launch Music on default click and hide
   * actions if service not running.
   */
  protected void defaultAppWidget(final Context context, final int[] appWidgetIds) {
    final RemoteViews appWidgetView = new RemoteViews(context.getPackageName(),
        R.layout.app_widget_big);

    appWidgetView.setViewVisibility(R.id.media_titles, View.INVISIBLE);
    appWidgetView.setImageViewResource(R.id.image, R.drawable.default_album_art);
    appWidgetView.setImageViewBitmap(R.id.button_next, createBitmap(
        RetroUtil.getTintedVectorDrawable(context, R.drawable.ic_skip_next_white_24dp,
            MaterialValueHelper.getPrimaryTextColor(context, false)), 1f));
    appWidgetView.setImageViewBitmap(R.id.button_prev, createBitmap(
        RetroUtil.getTintedVectorDrawable(context, R.drawable.ic_skip_previous_white_24dp,
            MaterialValueHelper.getPrimaryTextColor(context, false)), 1f));
    appWidgetView.setImageViewBitmap(R.id.button_toggle_play_pause, createBitmap(
        RetroUtil.getTintedVectorDrawable(context, R.drawable.ic_play_arrow_white_24dp,
            MaterialValueHelper.getPrimaryTextColor(context, false)), 1f));

    linkButtons(context, appWidgetView);
    pushUpdate(context, appWidgetIds, appWidgetView);
  }

  /**
   * Update all active widget instances by pushing changes
   */
  public void performUpdate(final MusicService service, final int[] appWidgetIds) {
    final RemoteViews appWidgetView = new RemoteViews(service.getPackageName(),
        R.layout.app_widget_big);

    final boolean isPlaying = service.isPlaying();
    final Song song = service.getCurrentSong();

    // Set the titles and artwork
    if (TextUtils.isEmpty(song.title) && TextUtils.isEmpty(song.artistName)) {
      appWidgetView.setViewVisibility(R.id.media_titles, View.INVISIBLE);
    } else {
      appWidgetView.setViewVisibility(R.id.media_titles, View.VISIBLE);
      appWidgetView.setTextViewText(R.id.title, song.title);
      appWidgetView.setTextViewText(R.id.text, getSongArtistAndAlbum(song));
    }

    // Set correct drawable for pause state
    int playPauseRes =
        isPlaying ? R.drawable.ic_pause_white_24dp : R.drawable.ic_play_arrow_white_24dp;
    appWidgetView.setImageViewBitmap(R.id.button_toggle_play_pause, createBitmap(
        RetroUtil.getTintedVectorDrawable(service, playPauseRes,
            MaterialValueHelper.getPrimaryTextColor(service, false)), 1f));

    // Set prev/next button drawables
    appWidgetView.setImageViewBitmap(R.id.button_next, createBitmap(
        RetroUtil.getTintedVectorDrawable(service, R.drawable.ic_skip_next_white_24dp,
            MaterialValueHelper.getPrimaryTextColor(service, false)), 1f));
    appWidgetView.setImageViewBitmap(R.id.button_prev, createBitmap(
        RetroUtil.getTintedVectorDrawable(service, R.drawable.ic_skip_previous_white_24dp,
            MaterialValueHelper.getPrimaryTextColor(service, false)), 1f));

    // Link actions buttons to intents
    linkButtons(service, appWidgetView);

    // Load the album cover async and push the update on completion
    Point p = RetroUtil.getScreenSize(service);
    final int widgetImageSize = Math.min(p.x, p.y);
    final Context appContext = service.getApplicationContext();
    service.runOnUiThread(new Runnable() {
      @Override
      public void run() {
        if (target != null) {
          Glide.clear(target);
        }
        target = SongGlideRequest.Builder.from(Glide.with(appContext), song)
            .checkIgnoreMediaStore(appContext)
            .asBitmap().build()
            .into(new SimpleTarget<Bitmap>(widgetImageSize, widgetImageSize) {
              @Override
              public void onResourceReady(Bitmap resource,
                  GlideAnimation<? super Bitmap> glideAnimation) {
                update(resource);
              }

              @Override
              public void onLoadFailed(Exception e, Drawable errorDrawable) {
                super.onLoadFailed(e, errorDrawable);
                update(null);
              }

              private void update(@Nullable Bitmap bitmap) {
                if (bitmap == null) {
                  appWidgetView.setImageViewResource(R.id.image, R.drawable.default_album_art);
                } else {
                  appWidgetView.setImageViewBitmap(R.id.image, bitmap);
                }
                pushUpdate(appContext, appWidgetIds, appWidgetView);
              }
            });
      }
    });
  }

  /**
   * Link up various button actions using {@link PendingIntent}.
   */
  private void linkButtons(final Context context, final RemoteViews views) {
    Intent action;
    PendingIntent pendingIntent;

    final ComponentName serviceName = new ComponentName(context, MusicService.class);

    // Home
    action = new Intent(context, MainActivity.class);
    action.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
    pendingIntent = PendingIntent.getActivity(context, 0, action, 0);
    views.setOnClickPendingIntent(R.id.clickable_area, pendingIntent);

    // Previous track
    pendingIntent = buildPendingIntent(context, Constants.ACTION_REWIND, serviceName);
    views.setOnClickPendingIntent(R.id.button_prev, pendingIntent);

    // Play and pause
    pendingIntent = buildPendingIntent(context, Constants.ACTION_TOGGLE_PAUSE, serviceName);
    views.setOnClickPendingIntent(R.id.button_toggle_play_pause, pendingIntent);

    // Next track
    pendingIntent = buildPendingIntent(context, Constants.ACTION_SKIP, serviceName);
    views.setOnClickPendingIntent(R.id.button_next, pendingIntent);


  }
}
