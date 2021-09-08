package code.name.monkey.retromusic.service

import code.name.monkey.retromusic.service.playback.Playback
import java.util.*

class AudioFader(
    private val player: Playback,
    durationMillis: Long,
    private val fadeIn: Boolean,
    private val doOnEnd: Runnable
) {
    val timer = Timer()
    var volume = if (fadeIn) 0F else 1F
    val maxVolume = if (fadeIn) 1F else 0F
    private val volumeStep: Float = PERIOD / durationMillis.toFloat()

    fun start() {
        timer.scheduleAtFixedRate(
            object : TimerTask() {
                override fun run() {
                    setVolume()
                    if (volume < 0 || volume > 1) {
                        player.setVolume(maxVolume)
                        stop()
                        doOnEnd.run()
                    } else {
                        player.setVolume(volume)
                    }
                }
            }, 0, PERIOD
        )
    }

    fun stop() {
        timer.purge()
        timer.cancel()
    }

    private fun setVolume() {
        if (fadeIn) {
            volume += volumeStep
        } else {
            volume -= volumeStep
        }
    }

    companion object {
        const val PERIOD = 100L
    }
}