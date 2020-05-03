package code.name.monkey.retromusic.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import code.name.monkey.retromusic.BuildConfig
import code.name.monkey.retromusic.model.Song

/**
 * Created by hemanths on 2020-02-23.
 */
@Dao
interface QueueDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveQueue(playingQueue: List<SongQueueEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveOriginalQueue(playingQueue: List<SongEntity>)


    @Query("SELECT * FROM playing_queue_${BuildConfig.FLAVOR}")
    fun getQueue(): List<SongQueueEntity>

    @Query("SELECT * FROM original_playing_queue_${BuildConfig.FLAVOR}")
    fun getOriginalQueue(): List<SongEntity>

    @Query("DELETE FROM playing_queue_${BuildConfig.FLAVOR}")
    suspend fun deleteQueue()

    @Query("DELETE FROM original_playing_queue_${BuildConfig.FLAVOR}")
    suspend fun deleteOriginalQueue()
}