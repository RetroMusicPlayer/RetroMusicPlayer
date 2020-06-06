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
package code.name.monkey.retromusic.volume

import android.content.Context
import android.media.AudioManager
import android.os.Handler
import android.provider.Settings

class AudioVolumeObserver(private val context: Context) {
    private val mAudioManager: AudioManager =
        context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    private var contentObserver: AudioVolumeContentObserver? = null

    fun register(audioStreamType: Int, listener: OnAudioVolumeChangedListener) {
        val handler = Handler()
        // with this handler AudioVolumeContentObserver#onChange()
        //   will be executed in the main thread
        // To execute in another thread you can use a Looper
        // +info: https://stackoverflow.com/a/35261443/904907
        contentObserver = AudioVolumeContentObserver(
            handler,
            mAudioManager,
            audioStreamType,
            listener
        )
        context.contentResolver.registerContentObserver(
            Settings.System.CONTENT_URI,
            true,
            contentObserver!!
        )
    }

    fun unregister() {
        if (contentObserver != null) {
            context.contentResolver.unregisterContentObserver(contentObserver!!)
            contentObserver = null
        }
    }
}