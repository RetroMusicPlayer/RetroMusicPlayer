package code.name.monkey.retromusic.service.notification;

import static code.name.monkey.retromusic.Constants.ACTION_QUIT;
import static code.name.monkey.retromusic.Constants.ACTION_REWIND;
import static code.name.monkey.retromusic.Constants.ACTION_SKIP;
import static code.name.monkey.retromusic.Constants.ACTION_TOGGLE_PAUSE;
import static code.name.monkey.retromusic.util.RetroUtil.createBitmap;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import android.widget.RemoteViews;
import code.name.monkey.appthemehelper.util.ColorUtil;
import code.name.monkey.appthemehelper.util.MaterialValueHelper;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.glide.SongGlideRequest;
import code.name.monkey.retromusic.glide.palette.BitmapPaletteWrapper;
import code.name.monkey.retromusic.model.Song;
import code.name.monkey.retromusic.service.MusicService;
import code.name.monkey.retromusic.ui.activities.MainActivity;
import code.name.monkey.retromusic.util.PreferenceUtil;
import code.name.monkey.retromusic.util.RetroUtil;
import code.name.monkey.retromusic.util.color.MediaNotificationProcessor;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;

/**
 * @author Hemanth S (h4h13).
 */
public class PlayingNotificationOreo extends PlayingNotification {

  private Target<BitmapPaletteWrapper> target;

  private RemoteViews getCombinedRemoteViews(boolean collapsed, Song song) {
    RemoteViews remoteViews = new RemoteViews(service.getPackageName(),
        collapsed ? R.layout.layout_notification_collapsed : R.layout.layout_notification_expanded);

    remoteViews.setTextViewText(R.id.appName,
        service.getString(R.string.app_name) + " • " + song.albumName);
    remoteViews.setTextViewText(R.id.title, song.title);
    remoteViews.setTextViewText(R.id.subtitle, song.artistName);

    TypedArray typedArray = service
        .obtainStyledAttributes(new int[]{android.R.attr.selectableItemBackground});
    int selectableItemBackground = typedArray.getResourceId(0, 0);
    typedArray.recycle();

    remoteViews.setInt(R.id.content, "setBackgroundResource", selectableItemBackground);

    linkButtons(remoteViews);

    //setNotificationContent(remoteViews, ColorUtil.isColorLight(backgroundColor));
    return remoteViews;
  }

  @Override
  public void update() {
    stopped = false;
    final Song song = service.getCurrentSong();
    final boolean isPlaying = service.isPlaying();

    final RemoteViews notificationLayout = getCombinedRemoteViews(true, song);
    final RemoteViews notificationLayoutBig = getCombinedRemoteViews(false, song);

    Intent action = new Intent(service, MainActivity.class);
    action.putExtra("expand", true);
    action.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

    final PendingIntent clickIntent = PendingIntent
        .getActivity(service, 0, action, PendingIntent.FLAG_UPDATE_CURRENT);
    final PendingIntent deleteIntent = buildPendingIntent(service, ACTION_QUIT, null);

    final NotificationCompat.Builder builder = new NotificationCompat.Builder(service,
        NOTIFICATION_CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_notification)
        .setContentIntent(clickIntent)
        .setDeleteIntent(deleteIntent)
        .setCategory(NotificationCompat.CATEGORY_SERVICE)
        .setPriority(NotificationCompat.PRIORITY_MAX)
        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        .setCustomContentView(notificationLayout)
        .setCustomBigContentView(notificationLayoutBig)
        .setOngoing(isPlaying);

    final int bigNotificationImageSize = service.getResources()
        .getDimensionPixelSize(R.dimen.notification_big_image_size);
    service.runOnUiThread(new Runnable() {
      @Override
      public void run() {
        if (target != null) {
          Glide.clear(target);
        }
        target = SongGlideRequest.Builder.from(Glide.with(service), song)
            .checkIgnoreMediaStore(service)
            .generatePalette(service).build()
            .into(new SimpleTarget<BitmapPaletteWrapper>(bigNotificationImageSize,
                bigNotificationImageSize) {
              @Override
              public void onResourceReady(BitmapPaletteWrapper resource,
                  GlideAnimation<? super BitmapPaletteWrapper> glideAnimation) {

                MediaNotificationProcessor mediaNotificationProcessor = new MediaNotificationProcessor(
                    service, service, (i, i2) -> update(resource.getBitmap(), i, i2));
                mediaNotificationProcessor.processNotification(resource.getBitmap());

              }

              @Override
              public void onLoadFailed(Exception e, Drawable errorDrawable) {
                super.onLoadFailed(e, errorDrawable);
                update(null, Color.WHITE, Color.BLACK);
              }

              private void update(@Nullable Bitmap bitmap, int bgColor, int textColor) {
                if (bitmap != null) {
                  notificationLayout.setImageViewBitmap(R.id.largeIcon, bitmap);
                  notificationLayoutBig.setImageViewBitmap(R.id.largeIcon, bitmap);
                } else {
                  notificationLayout
                      .setImageViewResource(R.id.largeIcon, R.drawable.default_album_art);
                  notificationLayoutBig
                      .setImageViewResource(R.id.largeIcon, R.drawable.default_album_art);
                }

                if (!PreferenceUtil.getInstance(service).coloredNotification()) {
                  bgColor = Color.WHITE;
                }
                setBackgroundColor(bgColor);
                setNotificationContent(ColorUtil.isColorLight(bgColor));

                if (stopped) {
                  return; // notification has been stopped before loading was finished
                }
                updateNotifyModeAndPostNotification(builder.build());
              }

              private void setBackgroundColor(int color) {

                notificationLayout.setInt(R.id.image, "setBackgroundColor", color);
                notificationLayoutBig.setInt(R.id.image, "setBackgroundColor", color);

                notificationLayout.setInt(R.id.foregroundImage, "setColorFilter", color);
                notificationLayoutBig.setInt(R.id.foregroundImage, "setColorFilter", color);
              }

              private void setNotificationContent(boolean dark) {
                int primary = MaterialValueHelper.getPrimaryTextColor(service, dark);
                int secondary = MaterialValueHelper.getSecondaryTextColor(service, dark);

                Bitmap close = createBitmap(
                    RetroUtil
                        .getTintedVectorDrawable(service, R.drawable.ic_close_white_24dp, primary),
                    NOTIFICATION_CONTROLS_SIZE_MULTIPLIER);
                Bitmap prev = createBitmap(
                    RetroUtil
                        .getTintedVectorDrawable(service, R.drawable.ic_skip_previous_white_24dp,
                            primary), NOTIFICATION_CONTROLS_SIZE_MULTIPLIER);
                Bitmap next = createBitmap(
                    RetroUtil.getTintedVectorDrawable(service, R.drawable.ic_skip_next_white_24dp,
                        primary), NOTIFICATION_CONTROLS_SIZE_MULTIPLIER);
                Bitmap playPause = createBitmap(RetroUtil.getTintedVectorDrawable(service,
                    isPlaying ? R.drawable.ic_pause_white_24dp
                        : R.drawable.ic_play_arrow_white_24dp, primary),
                    NOTIFICATION_CONTROLS_SIZE_MULTIPLIER);

                notificationLayout.setTextColor(R.id.title, primary);
                notificationLayout.setTextColor(R.id.subtitle, secondary);
                notificationLayout.setTextColor(R.id.appName, secondary);

                notificationLayout.setImageViewBitmap(R.id.action_prev, prev);
                notificationLayout.setImageViewBitmap(R.id.action_next, next);
                notificationLayout.setImageViewBitmap(R.id.action_play_pause, playPause);

                notificationLayoutBig.setTextColor(R.id.title, primary);
                notificationLayoutBig.setTextColor(R.id.subtitle, secondary);
                notificationLayoutBig.setTextColor(R.id.appName, secondary);

                notificationLayoutBig.setImageViewBitmap(R.id.action_quit, close);
                notificationLayoutBig.setImageViewBitmap(R.id.action_prev, prev);
                notificationLayoutBig.setImageViewBitmap(R.id.action_next, next);
                notificationLayoutBig.setImageViewBitmap(R.id.action_play_pause, playPause);

                notificationLayout.setImageViewBitmap(R.id.smallIcon,
                    createBitmap(RetroUtil
                            .getTintedVectorDrawable(service, R.drawable.ic_notification, secondary),
                        0.6f));
                notificationLayoutBig.setImageViewBitmap(R.id.smallIcon,
                    createBitmap(RetroUtil
                            .getTintedVectorDrawable(service, R.drawable.ic_notification, secondary),
                        0.6f));

                notificationLayout.setInt(R.id.arrow, "setColorFilter", secondary);
                notificationLayoutBig.setInt(R.id.arrow, "setColorFilter", secondary);

              }
            });
      }
    });

    if (stopped) {
      return; // notification has been stopped before loading was finished
    }
    updateNotifyModeAndPostNotification(builder.build());
  }


  private PendingIntent buildPendingIntent(Context context, final String action,
      final ComponentName serviceName) {
    Intent intent = new Intent(action);
    intent.setComponent(serviceName);
    return PendingIntent.getService(context, 0, intent, 0);
  }


  private void linkButtons(final RemoteViews notificationLayout) {
    PendingIntent pendingIntent;

    final ComponentName serviceName = new ComponentName(service, MusicService.class);

    // Previous track
    pendingIntent = buildPendingIntent(service, ACTION_REWIND, serviceName);
    notificationLayout.setOnClickPendingIntent(R.id.action_prev, pendingIntent);

    // Play and pause
    pendingIntent = buildPendingIntent(service, ACTION_TOGGLE_PAUSE, serviceName);
    notificationLayout.setOnClickPendingIntent(R.id.action_play_pause, pendingIntent);

    // Next track
    pendingIntent = buildPendingIntent(service, ACTION_SKIP, serviceName);
    notificationLayout.setOnClickPendingIntent(R.id.action_next, pendingIntent);

    // Close
    pendingIntent = buildPendingIntent(service, ACTION_QUIT, serviceName);
    notificationLayout.setOnClickPendingIntent(R.id.action_quit, pendingIntent);
  }

}
