package code.name.monkey.retromusic.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * Created by hemanths on 2020-02-23.
 */
@Dao
interface QueueDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveQueue(playingQueue: List<SongEntity>)


    @Query("SELECT * FROM song_entity")
    fun getQueue(): List<SongEntity>

    @Query("SELECT * FROM song_entity")
    fun getOriginalQueue(): List<SongEntity>
}