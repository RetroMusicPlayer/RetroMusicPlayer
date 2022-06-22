package code.name.monkey.retromusic.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.PlaybackParams
import androidx.annotation.CallSuper
import androidx.core.content.getSystemService
import androidx.core.net.toUri
import androidx.media.AudioAttributesCompat
import androidx.media.AudioFocusRequestCompat
import androidx.media.AudioManagerCompat
import code.name.monkey.appthemehelper.util.VersionUtils
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.extensions.showToast
import code.name.monkey.retromusic.service.playback.Playback
import code.name.monkey.retromusic.util.PreferenceUtil.isAudioFocusEnabled
import code.name.monkey.retromusic.util.PreferenceUtil.playbackPitch
import code.name.monkey.retromusic.util.PreferenceUtil.playbackSpeed

abstract class LocalPlayback(val context: Context) : Playback, MediaPlayer.OnErrorListener,
    MediaPlayer.OnCompletionListener {

    private val becomingNoisyReceiverIntentFilter =
        IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY)

    private val audioManager: AudioManager? = context.getSystemService()

    private var becomingNoisyReceiverRegistered = false
    private val becomingNoisyReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action != null
                && intent.action == AudioManager.ACTION_AUDIO_BECOMING_NOISY
            ) {
                val serviceIntent = Intent(context, MusicService::class.java)
                serviceIntent.action = MusicService.ACTION_PAUSE
                context.startService(serviceIntent)
            }
        }
    }

    private var isPausedByTransientLossOfFocus = false

    private val audioFocusListener = AudioManager.OnAudioFocusChangeListener { focusChange ->
        when (focusChange) {
            AudioManager.AUDIOFOCUS_GAIN -> {
                if (!isPlaying && isPausedByTransientLossOfFocus) {
                    start()
                    callbacks?.onPlayStateChanged()
                    isPausedByTransientLossOfFocus = false
                }
                setVolume(Volume.NORMAL)
            }
            AudioManager.AUDIOFOCUS_LOSS -> {
                // Lost focus for an unbounded amount of time: stop playback and release media playback
                if (!isAudioFocusEnabled) {
                    pause()
                    callbacks?.onPlayStateChanged()
                }
            }
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> {
                // Lost focus for a short time, but we have to stop
                // playback. We don't release the media playback because playback
                // is likely to resume
                val wasPlaying = isPlaying
                pause()
                callbacks?.onPlayStateChanged()
                isPausedByTransientLossOfFocus = wasPlaying
            }
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> {
                // Lost focus for a short time, but it's ok to keep playing
                // at an attenuated level
                setVolume(Volume.DUCK)
            }
        }
    }

    private val audioFocusRequest: AudioFocusRequestCompat =
        AudioFocusRequestCompat.Builder(AudioManagerCompat.AUDIOFOCUS_GAIN)
            .setOnAudioFocusChangeListener(audioFocusListener)
            .setAudioAttributes(
                AudioAttributesCompat.Builder()
                    .setContentType(AudioAttributesCompat.CONTENT_TYPE_MUSIC).build()
            ).build()

    @CallSuper
    override fun start(): Boolean {
        if (!requestFocus()) {
            context.showToast(R.string.audio_focus_denied)
        }
        registerBecomingNoisyReceiver()
        return true
    }

    @CallSuper
    override fun stop() {
        abandonFocus()
        unregisterBecomingNoisyReceiver()
    }

    @CallSuper
    override fun pause(): Boolean {
        unregisterBecomingNoisyReceiver()
        return true
    }

    /**
     * @param player The [MediaPlayer] to use
     * @param path The path of the file, or the http/rtsp URL of the stream you want to play
     * @return True if the <code>player</code> has been prepared and is ready to play, false otherwise
     */
    fun setDataSourceImpl(
        player: MediaPlayer,
        path: String,
        completion: (success: Boolean) -> Unit,
    ) {
        player.reset()
        try {
            if (path.startsWith("content://")) {
                player.setDataSource(context, path.toUri())
            } else {
                player.setDataSource(path)
            }
            player.setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
            )
            if (VersionUtils.hasMarshmallow())
                player.playbackParams =
                    PlaybackParams().setSpeed(playbackSpeed).setPitch(playbackPitch)

            player.setOnPreparedListener {
                player.setOnPreparedListener(null)
                completion(true)
            }
            player.prepareAsync()
        } catch (e: Exception) {
            completion(false)
            e.printStackTrace()
        }
        player.setOnCompletionListener(this)
        player.setOnErrorListener(this)
    }

    private fun unregisterBecomingNoisyReceiver() {
        if (becomingNoisyReceiverRegistered) {
            context.unregisterReceiver(becomingNoisyReceiver)
            becomingNoisyReceiverRegistered = false
        }
    }

    private fun registerBecomingNoisyReceiver() {
        if (!becomingNoisyReceiverRegistered) {
            context.registerReceiver(
                becomingNoisyReceiver,
                becomingNoisyReceiverIntentFilter
            )
            becomingNoisyReceiverRegistered = true
        }
    }

    private fun requestFocus(): Boolean {
        return AudioManagerCompat.requestAudioFocus(
            audioManager!!,
            audioFocusRequest
        ) == AudioManager.AUDIOFOCUS_REQUEST_GRANTED
    }

    private fun abandonFocus() {
        AudioManagerCompat.abandonAudioFocusRequest(audioManager!!, audioFocusRequest)
    }

    object Volume {
        /**
         * The volume we set the media player to when we lose audio focus, but are
         * allowed to reduce the volume instead of stopping playback.
         */
        const val DUCK = 0.2f

        /** The volume we set the media player when we have audio focus.  */
        const val NORMAL = 1.0f
    }
}