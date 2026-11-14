package code.name.monkey.retromusic.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ListeningEventDao {

    @Insert
    suspend fun insert(event: ListeningEventEntity)

    @Insert
    suspend fun insertAll(events: List<ListeningEventEntity>)

    @Query("SELECT * FROM ListeningEventEntity ORDER BY played_at DESC")
    suspend fun allEvents(): List<ListeningEventEntity>

    @Query("SELECT * FROM ListeningEventEntity ORDER BY played_at DESC LIMIT :limit")
    fun recentEvents(limit: Int): LiveData<List<ListeningEventEntity>>

    /** Used by restore to avoid inserting the same session twice. */
    @Query(
        "SELECT COUNT(*) FROM ListeningEventEntity " +
                "WHERE played_at = :playedAt AND title = :title AND artist_name = :artistName"
    )
    suspend fun countSameEvent(playedAt: Long, title: String, artistName: String): Int

    /** Pass 0 as [since] for all-time numbers. */
    @Query(
        "SELECT COALESCE(SUM(listened_ms), 0) AS totalMs, " +
                "COUNT(*) AS plays, " +
                "COUNT(DISTINCT title || '|' || artist_name || '|' || album_name) AS songs " +
                "FROM ListeningEventEntity WHERE played_at >= :since"
    )
    fun stats(since: Long): LiveData<ListeningStats>

    @Query("DELETE FROM ListeningEventEntity")
    suspend fun clear()
}
