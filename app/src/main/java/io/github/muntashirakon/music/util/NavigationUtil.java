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

package io.github.muntashirakon.music.util;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.media.audiofx.AudioEffect;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import org.jetbrains.annotations.NotNull;

import io.github.muntashirakon.music.R;
import io.github.muntashirakon.music.activities.DriveModeActivity;
import io.github.muntashirakon.music.activities.LicenseActivity;
import io.github.muntashirakon.music.activities.LyricsActivity;
import io.github.muntashirakon.music.activities.PlayingQueueActivity;
import io.github.muntashirakon.music.activities.UserInfoActivity;
import io.github.muntashirakon.music.activities.WhatsNewActivity;
import io.github.muntashirakon.music.activities.bugreport.BugReportActivity;
import io.github.muntashirakon.music.helper.MusicPlayerRemote;

public class NavigationUtil {

  public static void bugReport(@NonNull Activity activity) {
    ActivityCompat.startActivity(activity, new Intent(activity, BugReportActivity.class), null);
  }

  public static void goToLyrics(@NonNull Activity activity) {
    Intent intent = new Intent(activity, LyricsActivity.class);
    ActivityCompat.startActivity(activity, intent, null);
  }

  public static void goToOpenSource(@NonNull Activity activity) {
    ActivityCompat.startActivity(activity, new Intent(activity, LicenseActivity.class), null);
  }

  public static void goToPlayingQueue(@NonNull Activity activity) {
    Intent intent = new Intent(activity, PlayingQueueActivity.class);
    ActivityCompat.startActivity(activity, intent, null);
  }

  public static void goToUserInfo(
      @NonNull Activity activity, @NonNull ActivityOptions activityOptions) {
    ActivityCompat.startActivity(
        activity, new Intent(activity, UserInfoActivity.class), activityOptions.toBundle());
  }

  public static void gotoDriveMode(@NotNull final Activity activity) {
    ActivityCompat.startActivity(activity, new Intent(activity, DriveModeActivity.class), null);
  }

  public static void gotoWhatNews(@NonNull Activity activity) {
    ActivityCompat.startActivity(activity, new Intent(activity, WhatsNewActivity.class), null);
  }

  public static void openEqualizer(@NonNull final Activity activity) {
    stockEqalizer(activity);
  }

  private static void stockEqalizer(@NonNull Activity activity) {
    final int sessionId = MusicPlayerRemote.INSTANCE.getAudioSessionId();
    if (sessionId == AudioEffect.ERROR_BAD_VALUE) {
      Toast.makeText(
              activity, activity.getResources().getString(R.string.no_audio_ID), Toast.LENGTH_LONG)
          .show();
    } else {
      try {
        final Intent effects = new Intent(AudioEffect.ACTION_DISPLAY_AUDIO_EFFECT_CONTROL_PANEL);
        effects.putExtra(AudioEffect.EXTRA_AUDIO_SESSION, sessionId);
        effects.putExtra(AudioEffect.EXTRA_CONTENT_TYPE, AudioEffect.CONTENT_TYPE_MUSIC);
        activity.startActivityForResult(effects, 0);
      } catch (@NonNull final ActivityNotFoundException notFound) {
        Toast.makeText(
                activity,
                activity.getResources().getString(R.string.no_equalizer),
                Toast.LENGTH_SHORT)
            .show();
      }
    }
  }
}
