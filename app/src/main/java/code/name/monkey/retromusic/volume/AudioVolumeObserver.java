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

import android.content.Context;
import android.media.AudioManager;
import android.os.Handler;
import androidx.annotation.NonNull;

public class AudioVolumeObserver {

    private final Context mContext;
    private final AudioManager mAudioManager;
    private AudioVolumeContentObserver mAudioVolumeContentObserver;

    public AudioVolumeObserver(@NonNull Context context) {
        mContext = context;
        mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    }

    public void register(int audioStreamType, @NonNull OnAudioVolumeChangedListener listener) {

        Handler handler = new Handler();
        // with this handler AudioVolumeContentObserver#onChange() 
        //   will be executed in the main thread
        // To execute in another thread you can use a Looper
        // +info: https://stackoverflow.com/a/35261443/904907

        mAudioVolumeContentObserver = new AudioVolumeContentObserver(
                handler,
                mAudioManager,
                audioStreamType,
                listener);

        mContext.getContentResolver().registerContentObserver(
                android.provider.Settings.System.CONTENT_URI,
                true,
                mAudioVolumeContentObserver);
    }

    public void unregister() {
        if (mAudioVolumeContentObserver != null) {
            mContext.getContentResolver().unregisterContentObserver(mAudioVolumeContentObserver);
            mAudioVolumeContentObserver = null;
        }
    }
}