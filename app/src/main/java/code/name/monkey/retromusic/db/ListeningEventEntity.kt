package code.name.monkey.retromusic.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * One row per listening session of a song (a song change, skip or finish closes a session).
 *
 * The song is identified by title/artist/album/duration instead of only [songId] because
 * MediaStore ids change when the library moves to another device or is rescanned; this keeps
 * the history usable after a restore and even after the song file is deleted.
 */
@Entity
class ListeningEventEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "song_id")
    val songId: Long,
    val title: String,
    @ColumnInfo(name = "artist_name")
    val artistName: String,
    @ColumnInfo(name = "album_name")
    val albumName: String,
    val duration: Long,
    /** Wall clock time (ms since epoch) when this session started. */
    @ColumnInfo(name = "played_at")
    val playedAt: Long,
    /** Time actually spent playing during this session, in milliseconds. */
    @ColumnInfo(name = "listened_ms")
    val listenedMs: Long
)

/** Aggregated numbers for the stats screen. */
data class ListeningStats(
    val totalMs: Long,
    val plays: Int,
    val songs: Int
)
