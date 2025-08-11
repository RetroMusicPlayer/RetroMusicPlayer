//package code.name.monkey.retromusic.service
//
//import android.animation.ValueAnimator
//import android.content.Context
//import android.media.AudioAttributes
//import android.media.MediaPlayer
//import android.net.Uri
//import android.os.PowerManager
//import androidx.core.net.toUri
//import code.name.monkey.retromusic.model.Song
//import code.name.monkey.retromusic.service.playback.Playback.PlaybackCallbacks
//
//class FadeOutPauseFadeInPlayer(
//    private val context: Context,
//    private val fadeDuration: Int = 3000 // milliseconds
//) : AudioManagerPlayback(context), MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener {
//
//    private var mediaPlayer: MediaPlayer = MediaPlayer()
//    private var mIsInitialized = false
//    private var fadeAnimator: ValueAnimator? = null
//    override var callbacks: PlaybackCallbacks? = null
//
//    init {
//        mediaPlayer.setWakeMode(context, PowerManager.PARTIAL_WAKE_LOCK)
//        mediaPlayer.setOnCompletionListener(this)
//        mediaPlayer.setOnErrorListener(this)
//    }
//
//    override fun start(): Boolean {
//        super.start()
//        if (!mediaPlayer.isPlaying) {
//            mediaPlayer.start()
//            fadeIn()
//        }
//        return true
//    }
//
//    override fun pause(): Boolean {
//        super.pause()
//        if (mediaPlayer.isPlaying) {
//            fadeOut {
//                mediaPlayer.pause()
//            }
//        }
//        return true
//    }
//
//    override fun stop() {
//        super.stop()
//        mediaPlayer.stop()
//        mIsInitialized = false
//    }
//
//    override fun release() {
//        stop()
//        mediaPlayer.release()
//    }
//
//    override fun setVolume(vol: Float): Boolean {
//        mediaPlayer.setVolume(vol, vol)
//        return true
//    }
//
//    override val isInitialized: Boolean
//        get() = mIsInitialized
//
//    override val isPlaying: Boolean
//        get() = mIsInitialized && mediaPlayer.isPlaying
//
//    override fun setDataSource(
//        song: Song,
//        force: Boolean,
//        completion: (success: Boolean) -> Unit,
//    ) {
//        mIsInitialized = false
//        try {
//            mediaPlayer.reset()
//            val path = song.uri.toString()
//            if (path.startsWith("content://")) {
//                mediaPlayer.setDataSource(context, path.toUri())
//            } else {
//                mediaPlayer.setDataSource(path)
//            }
//            mediaPlayer.setAudioAttributes(
//                AudioAttributes.Builder()
//                    .setUsage(AudioAttributes.USAGE_MEDIA)
//                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
//                    .build()
//            )
//            mediaPlayer.setOnPreparedListener {
//                mediaPlayer.setOnPreparedListener(null)
//                mIsInitialized = true
//                completion(true)
//            }
//            mediaPlayer.prepare()
//        } catch (e: Exception) {
//            completion(false)
//            e.printStackTrace()
//        }
//    }
//
//    override fun setNextDataSource(path: Uri?) {
//        // Not used in this implementation
//    }
//
//    override fun setAudioSessionId(sessionId: Int): Boolean {
//        return try {
//            mediaPlayer.audioSessionId = sessionId
//            true
//        } catch (e: Exception) {
//            false
//        }
//    }
//
//    override val audioSessionId: Int
//        get() = mediaPlayer.audioSessionId
//
//    override fun duration(): Int {
//        return if (!mIsInitialized) -1 else mediaPlayer.duration
//    }
//
//    override fun position(): Int {
//        return if (!mIsInitialized) -1 else mediaPlayer.currentPosition
//    }
//
//    override fun seek(whereto: Int, force: Boolean): Int {
//        return try {
//            mediaPlayer.seekTo(whereto)
//            whereto
//        } catch (e: Exception) {
//            -1
//        }
//    }
//
//    override fun onCompletion(mp: MediaPlayer?) {
//        fadeOut {
//            callbacks?.onTrackEnded()
//        }
//    }
//
//    override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
//        mIsInitialized = false
//        mp?.release()
//        mediaPlayer = MediaPlayer()
//        mIsInitialized = true
//        mediaPlayer.setWakeMode(context, PowerManager.PARTIAL_WAKE_LOCK)
//        return false
//    }
//
//    /**
//     * Fades out the current song, pauses, sets the next song, starts, and fades in.
//     */
//    fun fadeToNextSong(next: Song) {
//        fadeOut {
//            mediaPlayer.pause()
//            setDataSource(next, true) { success ->
//                if (success) {
//                    mediaPlayer.setVolume(0f, 0f)
//                    mediaPlayer.start()
//                    fadeIn()
//                }
//            }
//        }
//    }
//
//    private fun fadeIn() {
//        fadeAnimator?.cancel()
//        mediaPlayer.setVolume(0f, 0f)
//        fadeAnimator = ValueAnimator.ofFloat(0f, 1f).apply {
//            duration = fadeDuration.toLong()
//            addUpdateListener { anim ->
//                val v = anim.animatedValue as Float
//                mediaPlayer.setVolume(v, v)
//            }
//            start()
//        }
//    }
//
//    private fun fadeOut(onEnd: (() -> Unit)? = null) {
//        if (!mediaPlayer.isPlaying) {
//            onEnd?.invoke()
//            return
//        }
//        fadeAnimator?.cancel()
//        fadeAnimator = ValueAnimator.ofFloat(1f, 0f).apply {
//            duration = fadeDuration.toLong()
//            addUpdateListener { anim ->
//                val v = anim.animatedValue as Float
//                mediaPlayer.setVolume(v, v)
//            }
//            doOnEnd {
//                onEnd?.invoke()
//            }
//            start()
//        }
//    }
//
//    private fun ValueAnimator.doOnEnd(action: () -> Unit) {
//        addListener(object : android.animation.Animator.AnimatorListener {
//            override fun onAnimationStart(animation: android.animation.Animator) {}
//            override fun onAnimationEnd(animation: android.animation.Animator) { action() }
//            override fun onAnimationCancel(animation: android.animation.Animator) { action() }
//            override fun onAnimationRepeat(animation: android.animation.Animator) {}
//        })
//    }
//}
