package code.name.monkey.retromusic.db

import androidx.room.*

@Dao
interface PlayCountDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSongInPlayCount(playCountEntity: PlayCountEntity)

    @Update
    fun updateSongInPlayCount(playCountEntity: PlayCountEntity)

    @Delete
    fun deleteSongInPlayCount(playCountEntity: PlayCountEntity)

    @Query("SELECT * FROM PlayCountEntity WHERE id =:songId")
    fun checkSongExistInPlayCount(songId: Int): List<PlayCountEntity>

    @Query("SELECT * FROM PlayCountEntity ORDER BY play_count DESC")
    fun playCountSongs(): List<PlayCountEntity>

    @Query("DELETE FROM SongEntity WHERE id =:songId")
    fun deleteSong(songId: Int)
}