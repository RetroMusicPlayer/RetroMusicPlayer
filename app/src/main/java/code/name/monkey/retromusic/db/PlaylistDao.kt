package code.name.monkey.retromusic.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface PlaylistDao {
    @Insert
    suspend fun createPlaylist(playlistEntity: PlaylistEntity): Long

    @Query("UPDATE PlaylistEntity SET playlist_name = :name WHERE playlist_id = :playlistId")
    suspend fun renamePlaylist(playlistId: Int, name: String)

    @Query("SELECT * FROM PlaylistEntity WHERE playlist_name = :name")
    fun isPlaylistExists(name: String): List<PlaylistEntity>

    @Query("SELECT * FROM PlaylistEntity")
    suspend fun playlists(): List<PlaylistEntity>

    @Query("DELETE FROM SongEntity WHERE playlist_creator_id = :playlistId")
    suspend fun deleteSongsInPlaylist(playlistId: Int)

    @Query("DELETE FROM SongEntity WHERE playlist_creator_id = :playlistId AND id = :songId")
    suspend fun removeSongFromPlaylist(playlistId: Int, songId: Int)

    @Transaction
    @Query("SELECT * FROM PlaylistEntity")
    suspend fun playlistsWithSongs(): List<PlaylistWithSongs>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSongsToPlaylist(songEntities: List<SongEntity>)

    @Query("SELECT * FROM SongEntity WHERE playlist_creator_id = :playlistId AND id = :songId")
    suspend fun isSongExistsInPlaylist(playlistId: Int, songId: Int): List<SongEntity>

    @Query("SELECT * FROM SongEntity WHERE playlist_creator_id = :playlistId")
    suspend fun songsFromPlaylist(playlistId: Int): List<SongEntity>

    @Delete
    suspend fun deletePlaylist(playlistEntity: PlaylistEntity)

    @Delete
    suspend fun deletePlaylists(playlistEntities: List<PlaylistEntity>)

    @Delete
    suspend fun deleteSongsInPlaylist(songs: List<SongEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSongInHistory(historyEntity: HistoryEntity)

    @Query("SELECT * FROM HistoryEntity WHERE id = :songId LIMIT 1")
    suspend fun isSongPresentInHistory(songId: Int): HistoryEntity?

    @Update
    suspend fun updateHistorySong(historyEntity: HistoryEntity)

    @Query("SELECT * FROM HistoryEntity ORDER BY time_played DESC")
    fun historySongs(): LiveData<List<HistoryEntity>>

    @Query("SELECT * FROM SongEntity WHERE playlist_creator_id= :playlistId")
    fun favoritesSongsLiveData(playlistId: Int): LiveData<List<SongEntity>>

    @Query("SELECT * FROM SongEntity WHERE playlist_creator_id= :playlistId")
    fun favoritesSongs(playlistId: Int): List<SongEntity>



}