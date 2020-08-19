package code.name.monkey.retromusic.db

import androidx.room.*

@Dao
interface PlaylistDao {
    @Insert
    suspend fun createPlaylist(playlistEntity: PlaylistEntity)

    @Query("SELECT * FROM PlaylistEntity")
    suspend fun playlists(): List<PlaylistEntity>

    @Transaction
    @Query("SELECT * FROM PlaylistEntity")
    suspend fun playlistsWithSong(): List<PlaylistWithSongs>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSongs(songEntities: List<SongEntity>)
}