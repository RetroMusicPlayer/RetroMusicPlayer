package code.name.monkey.retromusic.service.notification;

import static code.name.monkey.retromusic.Constants.ACTION_QUIT;
import static code.name.monkey.retromusic.Constants.ACTION_REWIND;
import static code.name.monkey.retromusic.Constants.ACTION_SKIP;
import static code.name.monkey.retromusic.Constants.ACTION_TOGGLE_PAUSE;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import androidx.media.app.NotificationCompat.MediaStyle;
import android.text.Html;
import code.name.monkey.retromusic.Constants;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.RetroApplication;
import code.name.monkey.retromusic.glide.SongGlideRequest;
import code.name.monkey.retromusic.glide.palette.BitmapPaletteWrapper;
import code.name.monkey.retromusic.model.Song;
import code.name.monkey.retromusic.service.MusicService;
import code.name.monkey.retromusic.ui.activities.MainActivity;
import code.name.monkey.retromusic.util.PreferenceUtil;
import code.name.monkey.retromusic.util.RetroColorUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

public class PlayingNotificationImpl24 extends PlayingNotification {

  @Override
  public synchronized void update() {
    stopped = false;

    final Song song = service.getCurrentSong();
    final boolean isPlaying = service.isPlaying();

    final int playButtonResId = isPlaying ? R.drawable.ic_pause_white_24dp :
        R.drawable.ic_play_arrow_white_24dp;

    Intent action = new Intent(service, MainActivity.class);
    action.putExtra("expand", true);
    action.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
    final PendingIntent clickIntent = PendingIntent
        .getActivity(service, 0, action, PendingIntent.FLAG_UPDATE_CURRENT);

    final ComponentName serviceName = new ComponentName(service, MusicService.class);
    Intent intent = new Intent(Constants.ACTION_QUIT);
    intent.setComponent(serviceName);
    final PendingIntent deleteIntent = PendingIntent.getService(service, 0, intent, 0);

    final int bigNotificationImageSize = service.getResources()
        .getDimensionPixelSize(R.dimen.notification_big_image_size);
    service.runOnUiThread(() -> SongGlideRequest.Builder.from(Glide.with(service), song)
        .checkIgnoreMediaStore(service)
        .generatePalette(service).build()
        .into(new SimpleTarget<BitmapPaletteWrapper>(bigNotificationImageSize,
            bigNotificationImageSize) {
          @Override
          public void onResourceReady(BitmapPaletteWrapper resource,
              GlideAnimation<? super BitmapPaletteWrapper> glideAnimation) {
            update(resource.getBitmap(),
                PreferenceUtil.getInstance(RetroApplication.getInstance()).isDominantColor() ?
                    RetroColorUtil.getDominantColor(resource.getBitmap(), Color.TRANSPARENT) :
                    RetroColorUtil.getColor(resource.getPalette(), Color.TRANSPARENT));
          }

          @Override
          public void onLoadFailed(Exception e, Drawable errorDrawable) {
            update(null, Color.TRANSPARENT);
          }

          void update(Bitmap bitmap, int color) {
            if (bitmap == null) {
              bitmap = BitmapFactory
                  .decodeResource(service.getResources(), R.drawable.default_album_art);
            }
            NotificationCompat.Action playPauseAction = new NotificationCompat.Action(
                playButtonResId,
                service.getString(R.string.action_play_pause),
                retrievePlaybackAction(ACTION_TOGGLE_PAUSE));

            NotificationCompat.Action closeAction = new NotificationCompat.Action(
                R.drawable.ic_close_white_24dp,
                service.getString(R.string.close_notification),
                retrievePlaybackAction(ACTION_QUIT));

            NotificationCompat.Action previousAction = new NotificationCompat.Action(
                R.drawable.ic_skip_previous_white_24dp,
                service.getString(R.string.action_previous),
                retrievePlaybackAction(ACTION_REWIND));

            NotificationCompat.Action nextAction = new NotificationCompat.Action(
                R.drawable.ic_skip_next_white_24dp,
                service.getString(R.string.action_next),
                retrievePlaybackAction(ACTION_SKIP));

            NotificationCompat.Builder builder = new NotificationCompat.Builder(service,
                NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setLargeIcon(bitmap)
                .setContentIntent(clickIntent)
                .setDeleteIntent(deleteIntent)
                .setContentTitle(Html.fromHtml("<b>" + song.title + "</b>"))
                .setContentText(song.artistName)
                .setSubText(Html.fromHtml("<b>" + song.albumName + "</b>"))
                .setOngoing(isPlaying)
                .setShowWhen(false)
                .addAction(previousAction)
                .addAction(playPauseAction)
                .addAction(nextAction)
                .addAction(closeAction);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
              builder.setStyle(new MediaStyle()
                  .setMediaSession(service.getMediaSession().getSessionToken())
                  .setShowActionsInCompactView(0, 1, 2, 3, 4))
                  .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
              if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O && PreferenceUtil
                  .getInstance(service).coloredNotification()) {
                builder.setColor(color);
              }
            }

            if (stopped) {
              return; // notification has been stopped before loading was finished
            }
            updateNotifyModeAndPostNotification(builder.build());
          }
        }));
  }

  private PendingIntent retrievePlaybackAction(final String action) {
    final ComponentName serviceName = new ComponentName(service, MusicService.class);
    Intent intent = new Intent(action);
    intent.setComponent(serviceName);
    return PendingIntent.getService(service, 0, intent, 0);
  }
}