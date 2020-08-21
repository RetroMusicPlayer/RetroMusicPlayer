package code.name.monkey.retromusic.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface PlaylistDao {
    @Insert
    suspend fun createPlaylist(playlistEntity: PlaylistEntity): Long

    @Query("UPDATE PlaylistEntity SET playlist_name = :name WHERE playlist_id = :playlistId")
    suspend fun renamePlaylistEntity(playlistId: Int, name: String)

    @Query("SELECT * FROM PlaylistEntity WHERE playlist_name = :name")
    suspend fun checkPlaylistExists(name: String): List<PlaylistEntity>

    @Query("SELECT * FROM PlaylistEntity")
    suspend fun playlists(): List<PlaylistEntity>

    @Query("DELETE FROM SongEntity WHERE playlist_creator_id = :playlistId")
    suspend fun deleteSongsFromPlaylist(playlistId: Int)

    @Transaction
    @Query("SELECT * FROM PlaylistEntity")
    suspend fun playlistsWithSong(): List<PlaylistWithSongs>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSongs(songEntities: List<SongEntity>)

    @Query("SELECT * FROM SongEntity WHERE playlist_creator_id = :playlistId AND id = :songId")
    suspend fun checkSongExistsWithPlaylistId(playlistId: Int, songId: Int): List<SongEntity>

    @Query("SELECT * FROM SongEntity WHERE playlist_creator_id = :playlistId")
    suspend fun getSongs(playlistId: Int): List<SongEntity>

    @Query("DELETE FROM SongEntity WHERE playlist_creator_id = :playlistId AND id = :songId")
    fun removeSong(playlistId: Int, songId: Int)

    @Delete
    suspend fun deletePlaylistEntity(playlistEntity: PlaylistEntity)

    @Delete
    suspend fun deletePlaylistEntities(playlistEntities: List<PlaylistEntity>)

    @Delete
    suspend fun removeSongsFromPlaylist(songs: List<SongEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSong(historyEntity: HistoryEntity)

    @Query("SELECT * FROM HistoryEntity WHERE id = :songId LIMIT 1")
    suspend fun songPresentInHistory(songId: Int): HistoryEntity?

    @Update
    suspend fun updateHistorySong(historyEntity: HistoryEntity)

    @Query("SELECT * FROM HistoryEntity ORDER BY time_played DESC")
    fun historySongs(): LiveData<List<HistoryEntity>>

}