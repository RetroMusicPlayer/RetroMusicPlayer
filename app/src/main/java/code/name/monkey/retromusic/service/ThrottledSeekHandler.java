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


import android.os.Handler;

import static code.name.monkey.retromusic.service.MusicService.PLAY_STATE_CHANGED;

public class ThrottledSeekHandler implements Runnable {
    // milliseconds to throttle before calling run() to aggregate events
    private static final long THROTTLE = 500;
    private final MusicService musicService;
    private Handler handler;

    ThrottledSeekHandler(MusicService musicService, Handler handler) {
        this.musicService = musicService;
        this.handler = handler;
    }

    void notifySeek() {
        handler.removeCallbacks(this);
        handler.postDelayed(this, THROTTLE);
    }

    @Override
    public void run() {
        musicService.savePositionInTrack();
        musicService.sendPublicIntent(PLAY_STATE_CHANGED); // for musixmatch synced lyrics
    }
}