package code.name.monkey.retromusic.service

import android.os.SystemClock
import code.name.monkey.retromusic.db.ListeningEventEntity
import code.name.monkey.retromusic.model.Song

/**
 * Measures how long the current song was actually played and hands back a finished
 * [ListeningEventEntity] when the song changes. Pure in-memory bookkeeping: no IO here,
 * the caller is responsible for saving the returned event off the main thread.
 *
 * It is separate from [SongPlayCountHelper] on purpose: that class only decides whether to bump
 * the play count and its stop watch is not restarted on gapless track changes.
 */
class ListeningTracker {

    private var song: Song = Song.emptySong
    private var accumulatedMs = 0L
    private var runStartedAt = 0L // SystemClock.elapsedRealtime(), only valid while running
    private var running = false
    private var sessionStartedAt = 0L // wall clock of the first start for this song, 0 = never

    /**
     * Call when the current song changes. Returns the event for the song that just ended,
     * or null if it was not listened to long enough.
     */
    @Synchronized
    fun onSongChanged(newSong: Song, isPlaying: Boolean): ListeningEventEntity? {
        val finished = buildEvent()
        song = newSong
        accumulatedMs = 0L
        running = false
        sessionStartedAt = 0L
        if (isPlaying) start()
        return finished
    }

    @Synchronized
    fun onPlayStateChanged(isPlaying: Boolean) {
        if (isPlaying) start() else pause()
    }

    /**
     * Closes the current session without changing the song, e.g. when the service is destroyed.
     * The song stays tracked so a later resume starts a new session.
     */
    @Synchronized
    fun flush(): ListeningEventEntity? {
        val finished = buildEvent()
        accumulatedMs = 0L
        sessionStartedAt = 0L
        if (running) {
            // Still playing: the next session starts right now.
            runStartedAt = SystemClock.elapsedRealtime()
            sessionStartedAt = System.currentTimeMillis()
        }
        return finished
    }

    private fun start() {
        if (running || song.id == Song.emptySong.id) return
        running = true
        runStartedAt = SystemClock.elapsedRealtime()
        if (sessionStartedAt == 0L) sessionStartedAt = System.currentTimeMillis()
    }

    private fun pause() {
        if (!running) return
        accumulatedMs += SystemClock.elapsedRealtime() - runStartedAt
        running = false
    }

    private fun buildEvent(): ListeningEventEntity? {
        if (song.id == Song.emptySong.id || sessionStartedAt == 0L) return null
        val total = accumulatedMs +
                if (running) SystemClock.elapsedRealtime() - runStartedAt else 0L
        if (total < MIN_LISTENED_MS) return null
        return ListeningEventEntity(
            songId = song.id,
            title = song.title,
            artistName = song.artistName,
            albumName = song.albumName,
            duration = song.duration,
            playedAt = sessionStartedAt,
            listenedMs = total
        )
    }

    companion object {
        /** Sessions shorter than this (skips) are not recorded. */
        const val MIN_LISTENED_MS = 5_000L
    }
}
