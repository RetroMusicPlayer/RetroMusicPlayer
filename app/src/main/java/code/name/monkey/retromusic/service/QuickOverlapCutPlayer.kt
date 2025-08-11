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
//import kotlinx.coroutines.*
//
//class QuickOverlapCutPlayer(
//    private val context: Context,
//    private val overlapMs: Int = 500, // overlap duration in ms (very short)
//    private val fadeMs: Int = 400     // fade in duration for next song
//) : AudioManagerPlayback(context), MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener {
//
//    private var currentPlayer: MediaPlayer = MediaPlayer()
//    private var nextPlayer: MediaPlayer = MediaPlayer()
//    private var mIsInitialized = false
//    private var isTransitioning = false
//    override var callbacks: PlaybackCallbacks? = null
//    private var transitionJob: Job? = null
//
//    init {
//        currentPlayer.setWakeMode(context, PowerManager.PARTIAL_WAKE_LOCK)
//        nextPlayer.setWakeMode(context, PowerManager.PARTIAL_WAKE_LOCK)
//        currentPlayer.setOnCompletionListener(this)
//        currentPlayer.setOnErrorListener(this)
//        nextPlayer.setOnErrorListener(this)
//    }
//
//    override fun start(): Boolean {
//        super.start()
//        if (!currentPlayer.isPlaying) {
//            currentPlayer.start()
//        }
//        return true
//    }
//
//    override fun pause(): Boolean {
//        super.pause()
//        currentPlayer.pause()
//        nextPlayer.pause()
//        return true
//    }
//
//    override fun stop() {
//        super.stop()
//        currentPlayer.stop()
//        nextPlayer.stop()
//        mIsInitialized = false
//    }
//
//    override fun release() {
//        stop()
//        currentPlayer.release()
//        nextPlayer.release()
//    }
//
//    override fun setVolume(vol: Float): Boolean {
//        currentPlayer.setVolume(vol, vol)
//        return true
//    }
//
//    override val isInitialized: Boolean
//        get() = mIsInitialized
//
//    override val isPlaying: Boolean
//        get() = mIsInitialized && (currentPlayer.isPlaying || nextPlayer.isPlaying)
//
//    override fun setDataSource(
//        song: Song,
//        force: Boolean,
//        completion: (success: Boolean) -> Unit,
//    ) {
//        mIsInitialized = false
//        try {
//            currentPlayer.reset()
//            val path = song.uri.toString()
//            if (path.startsWith("content://")) {
//                currentPlayer.setDataSource(context, path.toUri())
//            } else {
//                currentPlayer.setDataSource(path)
//            }
//            currentPlayer.setAudioAttributes(
//                AudioAttributes.Builder()
//                    .setUsage(AudioAttributes.USAGE_MEDIA)
//                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
//                    .build()
//            )
//            currentPlayer.setOnPreparedListener {
//                currentPlayer.setOnPreparedListener(null)
//                mIsInitialized = true
//                completion(true)
//            }
//            currentPlayer.prepare()
//        } catch (e: Exception) {
//            completion(false)
//            e.printStackTrace()
//        }
//    }
//
//    override fun setNextDataSource(path: Uri?) {
//        // Not used directly; see quickOverlapToNext
//    }
//
//    override fun setAudioSessionId(sessionId: Int): Boolean {
//        return try {
//            currentPlayer.audioSessionId = sessionId
//            true
//        } catch (e: Exception) {
//            false
//        }
//    }
//
//    override val audioSessionId: Int
//        get() = currentPlayer.audioSessionId
//
//    override fun duration(): Int {
//        return if (!mIsInitialized) -1 else currentPlayer.duration
//    }
//
//    override fun position(): Int {
//        return if (!mIsInitialized) -1 else currentPlayer.currentPosition
//    }
//
//    override fun seek(whereto: Int, force: Boolean): Int {
//        return try {
//            currentPlayer.seekTo(whereto)
//            whereto
//        } catch (e: Exception) {
//            -1
//        }
//    }
//
//    override fun onCompletion(mp: MediaPlayer?) {
//        callbacks?.onTrackEnded()
//    }
//
//    override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
//        mIsInitialized = false
//        mp?.release()
//        currentPlayer = MediaPlayer()
//        nextPlayer = MediaPlayer()
//        mIsInitialized = true
//        currentPlayer.setWakeMode(context, PowerManager.PARTIAL_WAKE_LOCK)
//        nextPlayer.setWakeMode(context, PowerManager.PARTIAL_WAKE_LOCK)
//        return false
//    }
//
//    /**
//     * Starts a quick overlap/cut transition to the next song.
//     * The next song starts at low volume, then current song is cut and next song fades in.
//     */
//    fun quickOverlapToNext(next: Song) {
//        if (isTransitioning) return
//        isTransitioning = true
//        nextPlayer.reset()
//        try {
//            val path = next.uri.toString()
//            if (path.startsWith("content://")) {
//                nextPlayer.setDataSource(context, path.toUri())
//            } else {
//                nextPlayer.setDataSource(path)
//            }
//            nextPlayer.setAudioAttributes(
//                AudioAttributes.Builder()
//                    .setUsage(AudioAttributes.USAGE_MEDIA)
//                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
//                    .build()
//            )
//            nextPlayer.setOnPreparedListener {
//                nextPlayer.setOnPreparedListener(null)
//                // Start next song at low volume
//                nextPlayer.setVolume(0.1f, 0.1f)
//                nextPlayer.start()
//                // Schedule cut and fade in
//                transitionJob?.cancel()
//                transitionJob = CoroutineScope(Dispatchers.Main).launch {
//                    delay(overlapMs.toLong())
//                    currentPlayer.pause()
//                    fadeInNext()
//                    swapPlayers()
//                    isTransitioning = false
//                    callbacks?.onTrackEnded()
//                }
//            }
//            nextPlayer.prepareAsync()
//        } catch (e: Exception) {
//            isTransitioning = false
//            e.printStackTrace()
//        }
//    }
//
//    private fun fadeInNext() {
//        val fade = ValueAnimator.ofFloat(0.1f, 1f).apply {
//            duration = fadeMs.toLong()
//            addUpdateListener { anim ->
//                val v = anim.animatedValue as Float
//                nextPlayer.setVolume(v, v)
//            }
//            start()
//        }
//    }
//
//    private fun swapPlayers() {
//        val temp = currentPlayer
//        currentPlayer = nextPlayer
//        nextPlayer = temp
//    }
//}
