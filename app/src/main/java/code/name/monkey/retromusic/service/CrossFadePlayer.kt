package code.name.monkey.retromusic.service

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.audiofx.AudioEffect
import android.net.Uri
import android.os.Handler
import android.os.Message
import android.os.PowerManager
import android.widget.Toast
import androidx.core.animation.doOnEnd
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.helper.MusicPlayerRemote
import code.name.monkey.retromusic.service.playback.Playback
import code.name.monkey.retromusic.service.playback.Playback.PlaybackCallbacks
import code.name.monkey.retromusic.util.MusicUtil
import code.name.monkey.retromusic.util.PreferenceUtil

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
    private var trackEndHandledByCrossFade = false
    private var mIsInitialized = false
    private var hasDataSource: Boolean = false /* Whether first player has DataSource */
    private var fadeInAnimator: Animator? = null
    private var fadeOutAnimator: Animator? = null
    private var callbacks: PlaybackCallbacks? = null

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

    // This has to run when queue is changed or song is changed manually by user
    fun sourceChangedByUser() {
        this.hasDataSource = false
        cancelFade()
        getCurrentPlayer()?.apply {
            if (isPlaying) stop()
        }
        getNextPlayer()?.apply {
            if (isPlaying) stop()
        }
    }

    override fun setDataSource(path: String): Boolean {
        cancelFade()
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
     * @param path   The path of the file, or the http/rtsp URL of the stream you want to play
     * @return True if the <code>player</code> has been prepared and is ready to play, false otherwise
     */
    private fun setDataSourceImpl(
        player: MediaPlayer,
        path: String
    ): Boolean {
        player.reset()
        player.setOnPreparedListener(null)
        try {
            if (path.startsWith("content://")) {
                player.setDataSource(context, Uri.parse(path))
            } else {
                player.setDataSource(path)
            }
            player.setAudioAttributes(
                AudioAttributes.Builder().setLegacyStreamType(AudioManager.STREAM_MUSIC).build()
            )
            player.prepare()
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
        if (mp == getNextPlayer()) {
            if (trackEndHandledByCrossFade) {
                trackEndHandledByCrossFade = false
            } else {
                notifyTrackEnded()
            }
        }
    }

    private fun notifyTrackEnded(){
        if (callbacks != null) {
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
            println("Fade In Completed")
            fadeInAnimator = null
        }
        fadeInAnimator?.start()
    }

    private fun fadeOut(mediaPlayer: MediaPlayer) {
        fadeOutAnimator = createFadeAnimator(false, mediaPlayer) {
            println("Fade Out Completed")
            fadeOutAnimator = null
        }
        fadeOutAnimator?.start()
    }

    private fun cancelFade() {
        fadeInAnimator?.cancel()
        fadeOutAnimator?.cancel()
        fadeInAnimator = null
        fadeOutAnimator = null
        getCurrentPlayer()?.setVolume(1f, 1f)
        getNextPlayer()?.setVolume(0f, 0f)
    }

    private fun createFadeAnimator(
        fadeIn: Boolean /* fadeIn -> true  fadeOut -> false*/,
        mediaPlayer: MediaPlayer,
        callback: Runnable /* Code to run when Animator Ends*/
    ): Animator? {
        val duration = PreferenceUtil.crossFadeDuration * 1000
        if (duration == 0) {
            return null
        }
        val startValue = if (fadeIn) 0f else 1.0f
        val endValue = if (fadeIn) 1.0f else 0f
        val animator = ValueAnimator.ofFloat(startValue, endValue)
        animator.duration = duration.toLong()
        animator.addUpdateListener { animation: ValueAnimator ->
            mediaPlayer.setVolume(
                animation.animatedValue as Float, animation.animatedValue as Float
            )
        }
        animator.doOnEnd {
            callback.run()
            // Set end values
            mediaPlayer.setVolume(endValue, endValue)
        }
        return animator
    }

    override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
        mIsInitialized = false
        mp?.release()
        player1 = MediaPlayer()
        player2 = MediaPlayer()
        mIsInitialized = true
        mp?.setWakeMode(context, PowerManager.PARTIAL_WAKE_LOCK)
        Toast.makeText(
            context,
            context.resources.getString(R.string.unplayable_file),
            Toast.LENGTH_SHORT
        )
            .show()
        return false
    }

    enum class CurrentPlayer {
        PLAYER_ONE,
        PLAYER_TWO,
        NOT_SET
    }

    inner class DurationListener : Handler() {

        fun start() {
            nextRefresh()
        }

        fun stop() {
            removeMessages(DURATION_CHANGED)
        }

        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (msg.what == DURATION_CHANGED) {
                nextRefresh()
                onDurationUpdated(position(), duration())
            }
        }

        private fun nextRefresh() {
            val message = obtainMessage(DURATION_CHANGED)
            removeMessages(DURATION_CHANGED)
            sendMessageDelayed(message, 100)
        }
    }


    fun onDurationUpdated(progress: Int, total: Int) {
        if (total > 0 && (total - progress).div(1000) == PreferenceUtil.crossFadeDuration) {
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
        notifyTrackEnded()
        trackEndHandledByCrossFade = true
    }

    companion object {
        private const val DURATION_CHANGED = 1
    }
}
