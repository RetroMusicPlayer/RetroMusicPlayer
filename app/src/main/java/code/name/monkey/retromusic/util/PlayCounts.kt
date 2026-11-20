/*
 * Copyright (c) 2026 crunchbank.
 *
 * This is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 */
package code.name.monkey.retromusic.util

import code.name.monkey.retromusic.db.PlayCountDao
import code.name.monkey.retromusic.db.toPlayCount
import code.name.monkey.retromusic.model.Song
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.text.Collator

/**
 * How many times each song has been played: the counts behind "Most played" and the
 * play-count sort orders. All calls touch the database, so make them off the main thread.
 */
object PlayCounts : KoinComponent {

    private val dao: PlayCountDao by inject()

    /** Counts one play of [song]. Called by the player once a song has been listened to. */
    fun bump(song: Song) {
        if (song.id < 0) return
        val existing = dao.findSongExistInPlayCount(song.id)
        if (existing != null) {
            existing.playCount += 1
            dao.upsertSongInPlayCount(existing)
        } else {
            dao.upsertSongInPlayCount(song.toPlayCount())
        }
    }

    /** Song id → times played, for every song played at least once. */
    fun all(): Map<Long, Int> = dao.playCounts().associate { it.id to it.playCount }

    /** [songs] most played first; songs with the same count (or never played) by title. */
    fun mostPlayedFirst(songs: List<Song>, counts: Map<Long, Int> = all()): List<Song> {
        val collator = Collator.getInstance()
        return songs.sortedWith { a, b ->
            val byCount = (counts[b.id] ?: 0).compareTo(counts[a.id] ?: 0)
            if (byCount != 0) byCount else collator.compare(a.title, b.title)
        }
    }
}
