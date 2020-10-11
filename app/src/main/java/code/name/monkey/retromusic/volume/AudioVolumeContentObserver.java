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

package code.name.monkey.retromusic.volume;

import android.database.ContentObserver;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Handler;
import androidx.annotation.NonNull;

public class AudioVolumeContentObserver extends ContentObserver {

  private final OnAudioVolumeChangedListener mListener;

  private final AudioManager mAudioManager;

  private final int mAudioStreamType;

  private float mLastVolume;

  AudioVolumeContentObserver(
      @NonNull Handler handler,
      @NonNull AudioManager audioManager,
      int audioStreamType,
      @NonNull OnAudioVolumeChangedListener listener) {

    super(handler);
    mAudioManager = audioManager;
    mAudioStreamType = audioStreamType;
    mListener = listener;
    mLastVolume = audioManager.getStreamVolume(mAudioStreamType);
  }

  /** Depending on the handler this method may be executed on the UI thread */
  @Override
  public void onChange(boolean selfChange, Uri uri) {
    if (mAudioManager != null && mListener != null) {
      int maxVolume = mAudioManager.getStreamMaxVolume(mAudioStreamType);
      int currentVolume = mAudioManager.getStreamVolume(mAudioStreamType);
      if (currentVolume != mLastVolume) {
        mLastVolume = currentVolume;
        mListener.onAudioVolumeChanged(currentVolume, maxVolume);
      }
    }
  }

  @Override
  public boolean deliverSelfNotifications() {
    return super.deliverSelfNotifications();
  }
}
