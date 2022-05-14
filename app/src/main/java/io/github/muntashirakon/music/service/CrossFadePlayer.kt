package io.github.muntashirakon.music.service

import android.animation.Animator
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.PlaybackParams
import android.media.audiofx.AudioEffect
import android.os.PowerManager
import android.util.Log
import androidx.core.net.toUri
import code.name.monkey.appthemehelper.util.VersionUtils.hasMarshmallow
import io.github.muntashirakon.music.R
import io.github.muntashirakon.music.extensions.showToast
import io.github.muntashirakon.music.helper.MusicPlayerRemote
import io.github.muntashirakon.music.service.AudioFader.Companion.createFadeAnimator
import io.github.muntashirakon.music.service.playback.Playback
import io.github.muntashirakon.music.service.playback.Playback.PlaybackCallbacks
import io.github.muntashirakon.music.util.MusicUtil
import io.github.muntashirakon.music.util.PreferenceUtil
import io.github.muntashirakon.music.util.PreferenceUtil.playbackPitch
import io.github.muntashirakon.music.util.PreferenceUtil.playbackSpeed
import kotlinx.coroutines.*

/** @author Prathamesh M */

/*
* To make Crossfade work we need two MediaPlayer's
* Basically, we switch back and forth between those two mp's
* e.g. When song is about to end (Reaches Crossfade duration) we let current mediaplayer
* play but with decreasing volume and start the player with the next song with increasing volume
* and vice versa for upcoming song and so on.
*/
class CrossFadePlayer(val context: Context) : Playback, MediaPlayer.OnCompletionListener,
    MediaPlayer.OnErrorListener {

    private var currentPlayer: CurrentPlayer = CurrentPlayer.NOT_SET
    private var player1 = MediaPlayer()
    private var player2 = MediaPlayer()
    private var durationListener = DurationListener()
    private var mIsInitialized = false
    private var hasDataSource: Boolean = false /* Whether first player has DataSource */
    private var fadeInAnimator: Animator? = null
    private var fadeOutAnimator: Animator? = null
    private var callbacks: PlaybackCallbacks? = null
    private var crossFadeDuration = PreferenceUtil.crossFadeDuration

    init {
        player1.setWakeMode(context, PowerManager.PARTIAL_WAKE_LOCK)
        player2.setWakeMode(context, PowerManager.PARTIAL_WAKE_LOCK)
        currentPlayer = CurrentPlayer.PLAYER_ONE
    }

    override fun start(): Boolean {
        durationListener.start()
        return try {
            getCurrentPlayer()?.start()
            true
        } catch (e: IllegalStateException) {
            e.printStackTrace()
            false
        }
    }

    override fun release() {
        getCurrentPlayer()?.release()
        getNextPlayer()?.release()
        durationListener.stop()
    }

    override fun setCallbacks(callbacks: PlaybackCallbacks) {
        this.callbacks = callbacks
    }

    override fun stop() {
        getCurrentPlayer()?.reset()
        mIsInitialized = false
    }

    override fun pause(): Boolean {
        durationListener.stop()
        cancelFade()
        getCurrentPlayer()?.let {
            if (it.isPlaying) {
                it.pause()
            }
        }
        getNextPlayer()?.let {
            if (it.isPlaying) {
                it.pause()
            }
        }
        return true
    }

    override fun seek(whereto: Int): Int {
        cancelFade()
        getNextPlayer()?.stop()
        return try {
            getCurrentPlayer()?.seekTo(whereto)
            whereto
        } catch (e: java.lang.IllegalStateException) {
            e.printStackTrace()
            -1
        }
    }

    override fun setVolume(vol: Float): Boolean {
        cancelFade()
        return try {
            getCurrentPlayer()?.setVolume(vol, vol)
            true
        } catch (e: IllegalStateException) {
            e.printStackTrace()
            false
        }
    }

    override val isInitialized: Boolean
        get() = mIsInitialized

    override val isPlaying: Boolean
        get() = mIsInitialized && getCurrentPlayer()?.isPlaying == true

    override fun setDataSource(path: String, force: Boolean): Boolean {
        cancelFade()
        if (force) hasDataSource = false
        mIsInitialized = false
        /* We've already set DataSource if initialized is true in setNextDataSource */
        if (!hasDataSource) {
            getCurrentPlayer()?.let { mIsInitialized = setDataSourceImpl(it, path) }
            hasDataSource = true
        } else {
            mIsInitialized = true
        }
        return mIsInitialized
    }

    override fun setNextDataSource(path: String?) {}

    /**
     * @param player The {@link MediaPlayer} to use
     * @param path The path of the file, or the http/rtsp URL of the stream you want to play
     * @return True if the <code>player</code> has been prepared and is ready to play, false otherwise
     */
    private fun setDataSourceImpl(
        player: MediaPlayer,
        path: String,
    ): Boolean {
        player.reset()
        player.setOnPreparedListener(null)
        try {
            if (path.startsWith("content://")) {
                player.setDataSource(context, path.toUri())
            } else {
                player.setDataSource(path)
            }
            player.setAudioAttributes(
                AudioAttributes.Builder().setLegacyStreamType(AudioManager.STREAM_MUSIC).build()
            )
            player.prepare()
            player.setPlaybackSpeedPitch(playbackSpeed, playbackPitch)
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
        player.setOnCompletionListener(this)
        player.setOnErrorListener(this)
        val intent = Intent(AudioEffect.ACTION_OPEN_AUDIO_EFFECT_CONTROL_SESSION)
        intent.putExtra(AudioEffect.EXTRA_AUDIO_SESSION, audioSessionId)
        intent.putExtra(AudioEffect.EXTRA_PACKAGE_NAME, context.packageName)
        intent.putExtra(AudioEffect.EXTRA_CONTENT_TYPE, AudioEffect.CONTENT_TYPE_MUSIC)
        context.sendBroadcast(intent)
        return true
    }

    override fun setAudioSessionId(sessionId: Int): Boolean {
        return try {
            getCurrentPlayer()?.audioSessionId = sessionId
            true
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            false
        } catch (e: IllegalStateException) {
            e.printStackTrace()
            false
        }
    }

    override val audioSessionId: Int
        get() = getCurrentPlayer()?.audioSessionId!!

    /**
     * Gets the duration of the file.
     *
     * @return The duration in milliseconds
     */
    override fun duration(): Int {
        return if (!mIsInitialized) {
            -1
        } else try {
            getCurrentPlayer()?.duration!!
        } catch (e: IllegalStateException) {
            e.printStackTrace()
            -1
        }
    }

    /**
     * Gets the current position in audio.
     * @return The position in milliseconds
     */
    override fun position(): Int {
        return if (!mIsInitialized) {
            -1
        } else try {
            getCurrentPlayer()?.currentPosition!!
        } catch (e: IllegalStateException) {
            e.printStackTrace()
            -1
        }
    }

    override fun onCompletion(mp: MediaPlayer?) {
        if (mp == getCurrentPlayer()) {
            callbacks?.onTrackEnded()
        }
    }

    private fun getCurrentPlayer(): MediaPlayer? {
        return when (currentPlayer) {
            CurrentPlayer.PLAYER_ONE -> {
                player1
            }
            CurrentPlayer.PLAYER_TWO -> {
                player2
            }
            CurrentPlayer.NOT_SET -> {
                null
            }
        }
    }

    private fun getNextPlayer(): MediaPlayer? {
        return when (currentPlayer) {
            CurrentPlayer.PLAYER_ONE -> {
                player2
            }
            CurrentPlayer.PLAYER_TWO -> {
                player1
            }
            CurrentPlayer.NOT_SET -> {
                null
            }
        }
    }

    private fun fadeIn(mediaPlayer: MediaPlayer) {
        fadeInAnimator = createFadeAnimator(true, mediaPlayer) {
            fadeInAnimator = null
            durationListener.start()
        }
        fadeInAnimator?.start()
    }

    private fun fadeOut(mediaPlayer: MediaPlayer) {
        fadeOutAnimator = createFadeAnimator(false, mediaPlayer) {
            fadeOutAnimator = null
            mediaPlayer.stop()
        }
        fadeOutAnimator?.start()
    }

    private fun cancelFade() {
        fadeInAnimator = null
        fadeOutAnimator = null
    }

    override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
        mIsInitialized = false
        mp?.release()
        player1 = MediaPlayer()
        player2 = MediaPlayer()
        mIsInitialized = true
        mp?.setWakeMode(context, PowerManager.PARTIAL_WAKE_LOCK)
        context.showToast(R.string.unplayable_file)
        Log.e(TAG, what.toString() + extra)
        return false
    }

    enum class CurrentPlayer {
        PLAYER_ONE,
        PLAYER_TWO,
        NOT_SET
    }

    inner class DurationListener : CoroutineScope by crossFadeScope() {

        private var job: Job? = null

        fun start() {
            job?.cancel()
            job = launch {
                while (true) {
                    delay(250)
                    onDurationUpdated(position(), duration())
                }
            }
        }

        fun stop() {
            job?.cancel()
        }
    }


    fun onDurationUpdated(progress: Int, total: Int) {
        if (total > 0 && (total - progress).div(1000) == crossFadeDuration) {
            getNextPlayer()?.let { player ->
                val nextSong = MusicPlayerRemote.nextSong
                if (nextSong != null) {
                    setDataSourceImpl(player, MusicUtil.getSongFileUri(nextSong.id).toString())
                    // Switch to other player / Crossfade only if next song exists
                    switchPlayer()
                }
            }
        }
    }

    private fun switchPlayer() {
        getNextPlayer()?.start()
        getCurrentPlayer()?.let { fadeOut(it) }
        getNextPlayer()?.let { fadeIn(it) }
        currentPlayer =
            if (currentPlayer == CurrentPlayer.PLAYER_ONE || currentPlayer == CurrentPlayer.NOT_SET) {
                CurrentPlayer.PLAYER_TWO
            } else {
                CurrentPlayer.PLAYER_ONE
            }
        callbacks?.onTrackEndedWithCrossfade()
    }

    override fun setCrossFadeDuration(duration: Int) {
        crossFadeDuration = duration
    }

    override fun setPlaybackSpeedPitch(speed: Float, pitch: Float) {
        getCurrentPlayer()?.setPlaybackSpeedPitch(speed, pitch)
    }

    private fun MediaPlayer.setPlaybackSpeedPitch(speed: Float, pitch: Float) {
        if (hasMarshmallow()) {
            val wasPlaying: Boolean = isPlaying
            playbackParams = PlaybackParams().setSpeed(speed).setPitch(pitch)
            if (!wasPlaying) {
                if (isPlaying) pause()
            }
        }
    }

    companion object {
        val TAG: String = CrossFadePlayer::class.java.simpleName
    }
}

internal fun crossFadeScope(): CoroutineScope = CoroutineScope(Job() + Dispatchers.Main)