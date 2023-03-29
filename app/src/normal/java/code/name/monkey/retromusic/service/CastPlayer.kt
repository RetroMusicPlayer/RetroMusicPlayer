package code.name.monkey.retromusic.service

import code.name.monkey.retromusic.cast.CastHelper.toMediaInfo
import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.service.playback.Playback
import code.name.monkey.retromusic.util.PreferenceUtil.playbackSpeed
import com.google.android.gms.cast.MediaLoadOptions
import com.google.android.gms.cast.MediaSeekOptions
import com.google.android.gms.cast.MediaStatus
import com.google.android.gms.cast.framework.CastSession
import com.google.android.gms.cast.framework.media.RemoteMediaClient

class CastPlayer(castSession: CastSession) : Playback, RemoteMediaClient.Callback() {

    override val isInitialized: Boolean = true

    private val remoteMediaClient: RemoteMediaClient? = castSession.remoteMediaClient

    init {
        remoteMediaClient?.registerCallback(this)
        remoteMediaClient?.setPlaybackRate(playbackSpeed.toDouble().coerceIn(0.5, 2.0))
    }

    private var isActuallyPlaying = false

    override val isPlaying: Boolean
        get() {
            return remoteMediaClient?.isPlaying == true || isActuallyPlaying
        }

    override val audioSessionId: Int = 0

    override var callbacks: Playback.PlaybackCallbacks? = null

    override fun setDataSource(
        song: Song,
        force: Boolean,
        completion: (success: Boolean) -> Unit,
    ) {
        try {
            val mediaLoadOptions =
                MediaLoadOptions.Builder().setPlayPosition(0).setAutoplay(true).build()
            remoteMediaClient?.load(song.toMediaInfo()!!, mediaLoadOptions)
            completion(true)
        } catch (e: Exception) {
            e.printStackTrace()
            completion(false)
        }
    }

    override fun setNextDataSource(path: String?) {}

    override fun start(): Boolean {
        isActuallyPlaying = true
        remoteMediaClient?.play()
        return true
    }

    override fun stop() {
        isActuallyPlaying = false
        remoteMediaClient?.stop()
    }

    override fun release() {
        stop()
    }

    override fun pause(): Boolean {
        isActuallyPlaying = false
        remoteMediaClient?.pause()
        return true
    }

    override fun duration(): Int {
        return remoteMediaClient?.streamDuration?.toInt() ?: 0
    }

    override fun position(): Int {
        return remoteMediaClient?.approximateStreamPosition?.toInt() ?: 0
    }

    override fun seek(whereto: Int, force: Boolean): Int {
        remoteMediaClient?.seek(MediaSeekOptions.Builder().setPosition(whereto.toLong()).build())
        return whereto
    }

    override fun setVolume(vol: Float) = true

    override fun setAudioSessionId(sessionId: Int) = true

    override fun setCrossFadeDuration(duration: Int) {}

    override fun setPlaybackSpeedPitch(speed: Float, pitch: Float) {
        remoteMediaClient?.setPlaybackRate(speed.toDouble().coerceIn(0.5, 2.0))
    }

    override fun onStatusUpdated() {
        when (remoteMediaClient?.playerState) {
            MediaStatus.PLAYER_STATE_IDLE -> {
                val idleReason = remoteMediaClient.idleReason
                if (idleReason == MediaStatus.IDLE_REASON_FINISHED) {
                    callbacks?.onTrackEnded()
                }
            }
            MediaStatus.PLAYER_STATE_PLAYING, MediaStatus.PLAYER_STATE_PAUSED -> {
                callbacks?.onPlayStateChanged()
            }
        }
    }
}