package code.name.monkey.retromusic.service.playback

import android.os.Handler
import android.os.Looper
import android.util.Log
import java.util.Date
import kotlin.math.max

class PlaybackSleepTimer(
    private val pausePlayback: () -> Unit
) {
    private var handler: Handler? = null
    private var playToEnd: Boolean = false
    private var startTime: Date? = null
    private var delay: Long = 0L

    /**
     * Start the sleep timer.
     *
     * @param delayMillis time in milliseconds until the sleep timer should sleep.
     * @param playToEnd whether to wait until the current track ends before pausing playback.
     */
    fun startTimer(
        delayMillis: Long,
        playToEnd: Boolean
    ){
        Log.v("PlaybackSleepTimer", "startTimer() called.. Delay: ${delay}ms")

        this.startTime = Date()
        this.delay = delayMillis
        this.playToEnd = playToEnd
        handler?.removeCallbacksAndMessages(null)
        handler = Handler(Looper.getMainLooper())

        handler?.postDelayed({
            if (!playToEnd) {
                sleep()
            }
        }, delay)
    }

    /**
     * Cancels the sleep timer
     */
    fun stopTimer() {
        Log.v("PlaybackSleepTimer","stopTimer() called")

    }

    /**
     * @return the time remaining until sleep, or null if the sleep timer has not been started.
     */
    fun timeRemaining(): Long? {
        startTime?.let { startTime ->
            return max(0L, delay - (Date().time - startTime.time))
        }

        return null
    }

    private fun sleep() {
        Log.v("PlaybackSleepTimer","sleep() called")
        pausePlayback()
        stopTimer()
    }

    /**
     * This function should be called in the playback callback for when a track is ended
     * so playToEnd parameter can work properly
     * @return true if timer stopped playback
     */
    fun onTrackEnded(): Boolean {
        Log.v("PlaybackSleepTimer","onPlaybackComplete, playToEnd: $playToEnd, timeRemaining: ${timeRemaining()}")
        if (playToEnd && timeRemaining() == 0L) {
            sleep()
            return true
        }
        return false
    }
}