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

import android.content.Context
import android.media.MediaPlayer
import android.os.PowerManager
import android.util.Log
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.extensions.showToast
import code.name.monkey.retromusic.extensions.uri
import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.service.playback.Playback.PlaybackCallbacks
import code.name.monkey.retromusic.util.PreferenceUtil.isGapLessPlayback
import code.name.monkey.retromusic.util.logE

/**
 * @author Andrew Neal, Karim Abou Zeid (kabouzeid)
 */
class MultiPlayer(context: Context) : LocalPlayback(context) {
    private var mCurrentMediaPlayer = MediaPlayer()
    private var mNextMediaPlayer: MediaPlayer? = null
    override var callbacks: PlaybackCallbacks? = null

    /**
     * @return True if the player is ready to go, false otherwise
     */
    override var isInitialized = false
        private set

    init {
        mCurrentMediaPlayer.setWakeMode(context, PowerManager.PARTIAL_WAKE_LOCK)
    }

    /**
     * @param song The song object you want to play
     * @return True if the `player` has been prepared and is ready to play, false otherwise
     */
    override fun setDataSource(
        song: Song,
        force: Boolean,
        completion: (success: Boolean) -> Unit,
    ) {
        isInitialized = false
        setDataSourceImpl(mCurrentMediaPlayer, song.uri.toString()) { success ->
            isInitialized = success
            if (isInitialized) {
                setNextDataSource(null)
            }
            completion(isInitialized)
        }
    }

    /**
     * Set the MediaPlayer to start when this MediaPlayer finishes playback.
     *
     * @param path The path of the file, or the http/rtsp URL of the stream you want to play
     */
    override fun setNextDataSource(path: String?) {
        try {
            mCurrentMediaPlayer.setNextMediaPlayer(null)
        } catch (e: IllegalArgumentException) {
            Log.i(TAG, "Next media player is current one, continuing")
        } catch (e: IllegalStateException) {
            Log.e(TAG, "Media player not initialized!")
            return
        }
        if (mNextMediaPlayer != null) {
            mNextMediaPlayer?.release()
            mNextMediaPlayer = null
        }
        if (path == null) {
            return
        }
        if (isGapLessPlayback) {
            mNextMediaPlayer = MediaPlayer()
            mNextMediaPlayer?.setWakeMode(context, PowerManager.PARTIAL_WAKE_LOCK)
            mNextMediaPlayer?.audioSessionId = audioSessionId
            setDataSourceImpl(mNextMediaPlayer!!, path) { success ->
                if (success) {
                    try {
                        mCurrentMediaPlayer.setNextMediaPlayer(mNextMediaPlayer)
                    } catch (e: IllegalArgumentException) {
                        Log.e(TAG, "setNextDataSource: setNextMediaPlayer()", e)
                        if (mNextMediaPlayer != null) {
                            mNextMediaPlayer?.release()
                            mNextMediaPlayer = null
                        }
                    } catch (e: IllegalStateException) {
                        Log.e(TAG, "setNextDataSource: setNextMediaPlayer()", e)
                        if (mNextMediaPlayer != null) {
                            mNextMediaPlayer?.release()
                            mNextMediaPlayer = null
                        }
                    }
                } else {
                    if (mNextMediaPlayer != null) {
                        mNextMediaPlayer?.release()
                        mNextMediaPlayer = null
                    }
                }
            }
        }
    }

    /**
     * Starts or resumes playback.
     */
    override fun start(): Boolean {
        super.start()
        return try {
            mCurrentMediaPlayer.start()
            true
        } catch (e: IllegalStateException) {
            false
        }
    }

    /**
     * Resets the MediaPlayer to its uninitialized state.
     */
    override fun stop() {
        super.stop()
        mCurrentMediaPlayer.reset()
        isInitialized = false
    }

    /**
     * Releases resources associated with this MediaPlayer object.
     */
    override fun release() {
        stop()
        mCurrentMediaPlayer.release()
        mNextMediaPlayer?.release()
    }

    /**
     * Pauses playback. Call start() to resume.
     */
    override fun pause(): Boolean {
        super.pause()
        return try {
            mCurrentMediaPlayer.pause()
            true
        } catch (e: IllegalStateException) {
            false
        }
    }

    /**
     * Checks whether the MultiPlayer is playing.
     */
    override val isPlaying: Boolean
        get() = isInitialized && mCurrentMediaPlayer.isPlaying

    /**
     * Gets the duration of the file.
     *
     * @return The duration in milliseconds
     */
    override fun duration(): Int {
        return if (!this.isInitialized) {
            -1
        } else try {
            mCurrentMediaPlayer.duration
        } catch (e: IllegalStateException) {
            -1
        }
    }

    /**
     * Gets the current playback position.
     *
     * @return The current position in milliseconds
     */
    override fun position(): Int {
        return if (!this.isInitialized) {
            -1
        } else try {
            mCurrentMediaPlayer.currentPosition
        } catch (e: IllegalStateException) {
            -1
        }
    }

    /**
     * Gets the current playback position.
     *
     * @param whereto The offset in milliseconds from the start to seek to
     * @return The offset in milliseconds from the start to seek to
     */
    override fun seek(whereto: Int, force: Boolean): Int {
        return try {
            mCurrentMediaPlayer.seekTo(whereto)
            whereto
        } catch (e: IllegalStateException) {
            -1
        }
    }

    override fun setVolume(vol: Float): Boolean {
        return try {
            mCurrentMediaPlayer.setVolume(vol, vol)
            true
        } catch (e: IllegalStateException) {
            false
        }
    }

    /**
     * Sets the audio session ID.
     *
     * @param sessionId The audio session ID
     */
    override fun setAudioSessionId(sessionId: Int): Boolean {
        return try {
            mCurrentMediaPlayer.audioSessionId = sessionId
            true
        } catch (e: IllegalArgumentException) {
            false
        } catch (e: IllegalStateException) {
            false
        }
    }

    /**
     * Returns the audio session ID.
     *
     * @return The current audio session ID.
     */
    override val audioSessionId: Int
        get() = mCurrentMediaPlayer.audioSessionId

    override fun onError(mp: MediaPlayer, what: Int, extra: Int): Boolean {
        isInitialized = false
        mCurrentMediaPlayer.release()
        mCurrentMediaPlayer = MediaPlayer()
        mCurrentMediaPlayer.setWakeMode(context, PowerManager.PARTIAL_WAKE_LOCK)
        context.showToast(R.string.unplayable_file)
        logE(what.toString() + extra)
        return false
    }

    override fun onCompletion(mp: MediaPlayer) {
        if (mp == mCurrentMediaPlayer && mNextMediaPlayer != null) {
            isInitialized = false
            mCurrentMediaPlayer.release()
            mCurrentMediaPlayer = mNextMediaPlayer!!
            isInitialized = true
            mNextMediaPlayer = null
            callbacks?.onTrackWentToNext()
        } else {
            callbacks?.onTrackEnded()
        }
    }

    override fun setCrossFadeDuration(duration: Int) {}

    override fun setPlaybackSpeedPitch(speed: Float, pitch: Float) {
        mCurrentMediaPlayer.setPlaybackSpeedPitch(speed, pitch)
        mNextMediaPlayer?.setPlaybackSpeedPitch(speed, pitch)
    }

    companion object {
        val TAG: String = MultiPlayer::class.java.simpleName
    }
}