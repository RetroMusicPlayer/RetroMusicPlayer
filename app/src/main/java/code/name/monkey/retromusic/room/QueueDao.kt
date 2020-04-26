package code.name.monkey.retromusic.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import code.name.monkey.retromusic.model.Song

/**
 * Created by hemanths on 2020-02-23.
 */
@Dao
interface QueueDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveQueue(playingQueue: List<Song>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveOriginalQueue(playingQueue: List<SongEntity>)


    @Query("SELECT * FROM playing_queue")
    fun getQueue(): List<Song>

    @Query("SELECT * FROM original_playing_queue")
    fun getOriginalQueue(): List<SongEntity>
}