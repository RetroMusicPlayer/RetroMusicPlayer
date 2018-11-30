package code.name.monkey.retromusic.service

import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.audiofx.AudioEffect
import android.net.Uri
import android.os.PowerManager
import android.util.Log
import android.widget.Toast

import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.service.playback.Playback
import code.name.monkey.retromusic.util.PreferenceUtil

/**
 * @author Andrew Neal, Karim Abou Zeid (kabouzeid)
 */
class MultiPlayer
/**
 * Constructor of `MultiPlayer`
 */
internal constructor(private val context: Context?) : Playback, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener {

    private var mCurrentMediaPlayer = MediaPlayer()
    private var mNextMediaPlayer: MediaPlayer? = null
    private var callbacks: Playback.PlaybackCallbacks? = null

    private var mIsInitialized = false

    init {
        mCurrentMediaPlayer.setWakeMode(context, PowerManager.PARTIAL_WAKE_LOCK)
    }

    /**
     * @param path The path of the file, or the http/rtsp URL of the stream
     * you want to play
     * @return True if the `player` has been prepared and is
     * ready to play, false otherwise
     */
    override fun setDataSource(path: String): Boolean {
        mIsInitialized = false
        mIsInitialized = setDataSourceImpl(mCurrentMediaPlayer, path)
        if (mIsInitialized) {
            setNextDataSource(null)
        }
        return mIsInitialized
    }

    /**
     * @param player The [MediaPlayer] to use
     * @param path   The path of the file, or the http/rtsp URL of the stream
     * you want to play
     * @return True if the `player` has been prepared and is
     * ready to play, false otherwise
     */
    private fun setDataSourceImpl(player: MediaPlayer, path: String): Boolean {
        if (context == null) {
            return false
        }
        try {
            player.reset()
            player.setOnPreparedListener(null)
            if (path.startsWith("content://")) {
                player.setDataSource(context, Uri.parse(path))
            } else {
                player.setDataSource(path)
            }
            player.setAudioStreamType(AudioManager.STREAM_MUSIC)
            player.prepare()
        } catch (e: Exception) {
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

    /**
     * Set the MediaPlayer to start when this MediaPlayer finishes playback.
     *
     * @param path The path of the file, or the http/rtsp URL of the stream
     * you want to play
     */
    override fun setNextDataSource(path: String?) {
        if (context == null) {
            return
        }
        try {
            mCurrentMediaPlayer.setNextMediaPlayer(null)
        } catch (e: IllegalArgumentException) {
            Log.i(TAG, "Next media player is current one, continuing")
        } catch (e: IllegalStateException) {
            Log.e(TAG, "Media player not initialized!")
            return
        }

        if (mNextMediaPlayer != null) {
            mNextMediaPlayer!!.release()
            mNextMediaPlayer = null
        }
        if (path == null) {
            return
        }
        if (PreferenceUtil.getInstance().gaplessPlayback()) {
            mNextMediaPlayer = MediaPlayer()
            mNextMediaPlayer!!.setWakeMode(context, PowerManager.PARTIAL_WAKE_LOCK)
            mNextMediaPlayer!!.audioSessionId = audioSessionId
            if (setDataSourceImpl(mNextMediaPlayer!!, path)) {
                try {
                    mCurrentMediaPlayer.setNextMediaPlayer(mNextMediaPlayer)
                } catch (e: IllegalArgumentException) {
                    Log.e(TAG, "setNextDataSource: setNextMediaPlayer()", e)
                    if (mNextMediaPlayer != null) {
                        mNextMediaPlayer!!.release()
                        mNextMediaPlayer = null
                    }
                } catch (e: IllegalStateException) {
                    Log.e(TAG, "setNextDataSource: setNextMediaPlayer()", e)
                    if (mNextMediaPlayer != null) {
                        mNextMediaPlayer!!.release()
                        mNextMediaPlayer = null
                    }
                }

            } else {
                if (mNextMediaPlayer != null) {
                    mNextMediaPlayer!!.release()
                    mNextMediaPlayer = null
                }
            }
        }
    }

    /**
     * Sets the callbacks
     *
     * @param callbacks The callbacks to use
     */
    override fun setCallbacks(callbacks: Playback.PlaybackCallbacks?) {
        this.callbacks = callbacks
    }

    /**
     * @return True if the player is ready to go, false otherwise
     */
    override fun isInitialized(): Boolean {
        return mIsInitialized
    }

    /**
     * Starts or resumes playback.
     */
    override fun start(): Boolean {
        try {
            mCurrentMediaPlayer.start()
            return true
        } catch (e: IllegalStateException) {
            return false
        }

    }

    /**
     * Resets the MediaPlayer to its uninitialized state.
     */
    override fun stop() {
        mCurrentMediaPlayer.reset()
        mIsInitialized = false
    }

    /**
     * Releases resources associated with this MediaPlayer object.
     */
    override fun release() {
        stop()
        mCurrentMediaPlayer.release()
        if (mNextMediaPlayer != null) {
            mNextMediaPlayer!!.release()
        }
    }

    /**
     * Pauses playback. Call start() to resume.
     */
    override fun pause(): Boolean {
        try {
            mCurrentMediaPlayer.pause()
            return true
        } catch (e: IllegalStateException) {
            return false
        }

    }

    /**
     * Checks whether the MultiPlayer is playing.
     */
    override fun isPlaying(): Boolean {
        return mIsInitialized && mCurrentMediaPlayer.isPlaying
    }

    /**
     * Gets the duration of the file.
     *
     * @return The duration in milliseconds
     */
    override fun duration(): Int {
        if (!mIsInitialized) {
            return -1
        }
        try {
            return mCurrentMediaPlayer.duration
        } catch (e: IllegalStateException) {
            return -1
        }

    }

    /**
     * Gets the current playback position.
     *
     * @return The current position in milliseconds
     */
    override fun position(): Int {
        if (!mIsInitialized) {
            return -1
        }
        try {
            return mCurrentMediaPlayer.currentPosition
        } catch (e: IllegalStateException) {
            return -1
        }

    }

    /**
     * Gets the current playback position.
     *
     * @param whereto The offset in milliseconds from the start to seek to
     * @return The offset in milliseconds from the start to seek to
     */
    override fun seek(whereto: Int): Int {
        try {
            mCurrentMediaPlayer.seekTo(whereto)
            return whereto
        } catch (e: IllegalStateException) {
            return -1
        }

    }

    override fun setVolume(vol: Float): Boolean {
        try {
            mCurrentMediaPlayer.setVolume(vol, vol)
            return true
        } catch (e: IllegalStateException) {
            return false
        }

    }

    /**
     * Sets the audio session ID.
     *
     * @param sessionId The audio session ID
     */
    override fun setAudioSessionId(sessionId: Int): Boolean {
        try {
            mCurrentMediaPlayer.audioSessionId = sessionId
            return true
        } catch (e: IllegalArgumentException) {
            return false
        } catch (e: IllegalStateException) {
            return false
        }

    }

    /**
     * Returns the audio session ID.
     *
     * @return The current audio session ID.
     */
    override fun getAudioSessionId(): Int {
        return mCurrentMediaPlayer.audioSessionId
    }

    /**
     * {@inheritDoc}
     */
    override fun onError(mp: MediaPlayer, what: Int, extra: Int): Boolean {
        mIsInitialized = false
        mCurrentMediaPlayer.release()
        mCurrentMediaPlayer = MediaPlayer()
        mCurrentMediaPlayer.setWakeMode(context, PowerManager.PARTIAL_WAKE_LOCK)
        if (context != null) {
            Toast.makeText(context, context.resources.getString(R.string.unplayable_file), Toast.LENGTH_SHORT).show()
        }
        return false
    }

    /**
     * {@inheritDoc}
     */
    override fun onCompletion(mp: MediaPlayer) {
        if (mp === mCurrentMediaPlayer && mNextMediaPlayer != null) {
            mIsInitialized = false
            mCurrentMediaPlayer.release()
            mCurrentMediaPlayer = mNextMediaPlayer as MediaPlayer
            mIsInitialized = true
            mNextMediaPlayer = null
            if (callbacks != null)
                callbacks!!.onTrackWentToNext()
        } else {
            if (callbacks != null)
                callbacks!!.onTrackEnded()
        }
    }

    companion object {
        val TAG: String = MultiPlayer::class.java.simpleName
    }


}