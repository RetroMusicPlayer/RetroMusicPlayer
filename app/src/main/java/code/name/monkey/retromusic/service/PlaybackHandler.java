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

package code.name.monkey.retromusic.service;

import android.media.AudioManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import java.lang.ref.WeakReference;

import code.name.monkey.retromusic.util.PreferenceUtil;

import static code.name.monkey.retromusic.service.MusicService.DUCK;
import static code.name.monkey.retromusic.service.MusicService.META_CHANGED;
import static code.name.monkey.retromusic.service.MusicService.PLAY_STATE_CHANGED;
import static code.name.monkey.retromusic.service.MusicService.REPEAT_MODE_NONE;
import static code.name.monkey.retromusic.service.MusicService.TRACK_ENDED;
import static code.name.monkey.retromusic.service.MusicService.TRACK_WENT_TO_NEXT;

class PlaybackHandler extends Handler {

    @NonNull
    private final WeakReference<MusicService> mService;
    private float currentDuckVolume = 1.0f;

    PlaybackHandler(final MusicService service, @NonNull final Looper looper) {
        super(looper);
        mService = new WeakReference<>(service);
    }

    @Override
    public void handleMessage(@NonNull final Message msg) {
        final MusicService service = mService.get();
        if (service == null) {
            return;
        }

        switch (msg.what) {
            case MusicService.DUCK:
                if (PreferenceUtil.INSTANCE.isAudioDucking()) {
                    currentDuckVolume -= .05f;
                    if (currentDuckVolume > .2f) {
                        sendEmptyMessageDelayed(DUCK, 10);
                    } else {
                        currentDuckVolume = .2f;
                    }
                } else {
                    currentDuckVolume = 1f;
                }
                service.playback.setVolume(currentDuckVolume);
                break;

            case MusicService.UNDUCK:
                if (PreferenceUtil.INSTANCE.isAudioDucking()) {
                    currentDuckVolume += .03f;
                    if (currentDuckVolume < 1f) {
                        sendEmptyMessageDelayed(MusicService.UNDUCK, 10);
                    } else {
                        currentDuckVolume = 1f;
                    }
                } else {
                    currentDuckVolume = 1f;
                }
                service.playback.setVolume(currentDuckVolume);
                break;

            case TRACK_WENT_TO_NEXT:
                if (service.pendingQuit || service.getRepeatMode() == REPEAT_MODE_NONE && service.isLastTrack()) {
                    service.pause();
                    service.seek(0);
                    if (service.pendingQuit) {
                        service.pendingQuit = false;
                        service.quit();
                        break;
                    }
                } else {
                    service.position = service.nextPosition;
                    service.prepareNextImpl();
                    service.notifyChange(META_CHANGED);
                }
                break;

            case TRACK_ENDED:
                // if there is a timer finished, don't continue
                if (service.pendingQuit ||
                        service.getRepeatMode() == REPEAT_MODE_NONE && service.isLastTrack()) {
                    service.notifyChange(PLAY_STATE_CHANGED);
                    service.seek(0);
                    if (service.pendingQuit) {
                        service.pendingQuit = false;
                        service.quit();
                        break;
                    }
                } else {
                    service.playNextSong(false);
                }
                sendEmptyMessage(MusicService.RELEASE_WAKELOCK);
                break;

            case MusicService.RELEASE_WAKELOCK:
                service.releaseWakeLock();
                break;

            case MusicService.PLAY_SONG:
                service.playSongAtImpl(msg.arg1);
                break;

            case MusicService.SET_POSITION:
                service.openTrackAndPrepareNextAt(msg.arg1);
                service.notifyChange(PLAY_STATE_CHANGED);
                break;

            case MusicService.PREPARE_NEXT:
                service.prepareNextImpl();
                break;

            case MusicService.RESTORE_QUEUES:
                service.restoreQueuesAndPositionIfNecessary();
                break;

            case MusicService.FOCUS_CHANGE:
                switch (msg.arg1) {
                    case AudioManager.AUDIOFOCUS_GAIN:
                        if (!service.isPlaying() && service.isPausedByTransientLossOfFocus()) {
                            service.play();
                            service.setPausedByTransientLossOfFocus(false);
                        }
                        removeMessages(DUCK);
                        sendEmptyMessage(MusicService.UNDUCK);
                        break;

                    case AudioManager.AUDIOFOCUS_LOSS:
                        // Lost focus for an unbounded amount of time: stop playback and release media playback
                        service.pause();
                        break;

                    case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                        // Lost focus for a short time, but we have to stop
                        // playback. We don't release the media playback because playback
                        // is likely to resume
                        boolean wasPlaying = service.isPlaying();
                        service.pause();
                        service.setPausedByTransientLossOfFocus(wasPlaying);
                        break;

                    case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                        // Lost focus for a short time, but it's ok to keep playing
                        // at an attenuated level
                        removeMessages(MusicService.UNDUCK);
                        sendEmptyMessage(DUCK);
                        break;
                }
                break;
        }
    }
}
