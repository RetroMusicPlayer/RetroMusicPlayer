/*
 * Copyright (c) 2020 Hemanth Savarla.
 *
 * Licensed under the GNU General Public License v3
 *
 * This is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 */
package code.name.monkey.retromusic.helper

import android.os.Handler
import android.os.Looper
import android.os.Message
import kotlin.math.max

class MusicProgressViewUpdateHelper : Handler {

    private var callback: Callback? = null
    private var intervalPlaying: Int = 0
    private var intervalPaused: Int = 0
    private var firstUpdate = true

    fun start() {
        queueNextRefresh(refreshProgressViews().toLong())
    }

    fun stop() {
        removeMessages(CMD_REFRESH_PROGRESS_VIEWS)
    }

    constructor(callback: Callback) : super(Looper.getMainLooper()) {
        this.callback = callback
        this.intervalPlaying = UPDATE_INTERVAL_PLAYING
        this.intervalPaused = UPDATE_INTERVAL_PAUSED
    }

    constructor(
        callback: Callback,
        intervalPlaying: Int,
        intervalPaused: Int,
    ) : super(Looper.getMainLooper()) {
        this.callback = callback
        this.intervalPlaying = intervalPlaying
        this.intervalPaused = intervalPaused
    }

    override fun handleMessage(msg: Message) {
        super.handleMessage(msg)
        if (msg.what == CMD_REFRESH_PROGRESS_VIEWS) {
            queueNextRefresh(refreshProgressViews().toLong())
        }
    }

    private fun refreshProgressViews(): Int {
        val progressMillis = MusicPlayerRemote.songProgressMillis
        val totalMillis = MusicPlayerRemote.songDurationMillis
        if (totalMillis > 0) {
            firstUpdate = false
            callback?.onUpdateProgressViews(progressMillis, totalMillis)
        }
        if (!MusicPlayerRemote.isPlaying && !firstUpdate) {
            return intervalPaused
        }

        val remainingMillis = intervalPlaying - progressMillis % intervalPlaying

        return max(MIN_INTERVAL, remainingMillis)
    }

    private fun queueNextRefresh(delay: Long) {
        val message = obtainMessage(CMD_REFRESH_PROGRESS_VIEWS)
        removeMessages(CMD_REFRESH_PROGRESS_VIEWS)
        sendMessageDelayed(message, delay)
    }

    interface Callback {
        fun onUpdateProgressViews(progress: Int, total: Int)
    }

    companion object {
        private const val CMD_REFRESH_PROGRESS_VIEWS = 1
        private const val MIN_INTERVAL = 20
        private const val UPDATE_INTERVAL_PLAYING = 500
        private const val UPDATE_INTERVAL_PAUSED = 500
    }
}
