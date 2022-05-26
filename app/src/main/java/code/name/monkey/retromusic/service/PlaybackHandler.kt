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
package code.name.monkey.retromusic.service

import android.media.AudioManager
import android.os.Handler
import android.os.Looper
import android.os.Message
import code.name.monkey.retromusic.util.PreferenceUtil.isAudioDucking
import code.name.monkey.retromusic.util.PreferenceUtil.isAudioFocusEnabled
import java.lang.ref.WeakReference

internal class PlaybackHandler(service: MusicService, looper: Looper) : Handler(looper) {
    private val mService: WeakReference<MusicService>
    private var currentDuckVolume = 1.0f

    override fun handleMessage(msg: Message) {
        val service = mService.get() ?: return
        when (msg.what) {
            MusicService.DUCK -> {
                if (isAudioDucking) {
                    currentDuckVolume -= .05f
                    if (currentDuckVolume > .2f) {
                        sendEmptyMessageDelayed(MusicService.DUCK, 10)
                    } else {
                        currentDuckVolume = .2f
                    }
                } else {
                    currentDuckVolume = 1f
                }
                service.playback?.setVolume(currentDuckVolume)
            }
            MusicService.UNDUCK -> {
                if (isAudioDucking) {
                    currentDuckVolume += .03f
                    if (currentDuckVolume < 1f) {
                        sendEmptyMessageDelayed(MusicService.UNDUCK, 10)
                    } else {
                        currentDuckVolume = 1f
                    }
                } else {
                    currentDuckVolume = 1f
                }
                service.playback?.setVolume(currentDuckVolume)
            }
            MusicService.TRACK_WENT_TO_NEXT ->
                if (service.pendingQuit || service.repeatMode == MusicService.REPEAT_MODE_NONE && service.isLastTrack) {
                    service.pause(false)
                    service.seek(0)
                    if (service.pendingQuit) {
                        service.pendingQuit = false
                        service.quit()
                    }
                } else {
                    service.position = service.nextPosition
                    service.prepareNextImpl()
                    service.notifyChange(MusicService.META_CHANGED)
                }
            MusicService.TRACK_ENDED -> {
                // if there is a timer finished, don't continue
                if (service.pendingQuit
                    || service.repeatMode == MusicService.REPEAT_MODE_NONE && service.isLastTrack
                ) {
                    service.notifyChange(MusicService.PLAY_STATE_CHANGED)
                    service.seek(0)
                    if (service.pendingQuit) {
                        service.pendingQuit = false
                        service.quit()
                    }
                } else {
                    service.playNextSong(false)
                }
                sendEmptyMessage(MusicService.RELEASE_WAKELOCK)
            }
            MusicService.RELEASE_WAKELOCK -> service.releaseWakeLock()
            MusicService.FOCUS_CHANGE -> when (msg.arg1) {
                AudioManager.AUDIOFOCUS_GAIN -> {
                    if (!service.isPlaying && service.isPausedByTransientLossOfFocus) {
                        service.play()
                        service.isPausedByTransientLossOfFocus = false
                    }
                    removeMessages(MusicService.DUCK)
                    sendEmptyMessage(MusicService.UNDUCK)
                }
                AudioManager.AUDIOFOCUS_LOSS -> {
                    // Lost focus for an unbounded amount of time: stop playback and release media playback
                    if (!isAudioFocusEnabled) {
                        service.pause(true)
                    }
                }
                AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> {
                    // Lost focus for a short time, but we have to stop
                    // playback. We don't release the media playback because playback
                    // is likely to resume
                    val wasPlaying = service.isPlaying
                    service.pause(true)
                    service.isPausedByTransientLossOfFocus = wasPlaying
                }
                AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> {
                    // Lost focus for a short time, but it's ok to keep playing
                    // at an attenuated level
                    removeMessages(MusicService.UNDUCK)
                    sendEmptyMessage(MusicService.DUCK)
                }
            }
        }
    }

    init {
        mService = WeakReference(service)
    }
}