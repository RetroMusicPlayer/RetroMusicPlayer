package code.name.monkey.retromusic.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface HistoryDao {
    companion object {
        private const val HISTORY_LIMIT = 100
    }

    @Upsert
    suspend fun upsertSongInHistory(historyEntity: HistoryEntity)

    @Query("DELETE FROM HistoryEntity WHERE id= :songId")
    fun deleteSongInHistory(songId: Long)

    @Query("SELECT * FROM HistoryEntity ORDER BY time_played DESC LIMIT $HISTORY_LIMIT")
    fun historySongs(): List<HistoryEntity>

    @Query("SELECT * FROM HistoryEntity ORDER BY time_played DESC LIMIT $HISTORY_LIMIT")
    fun observableHistorySongs(): LiveData<List<HistoryEntity>>

    @Query("DELETE FROM HistoryEntity")
    suspend fun clearHistory()
}
