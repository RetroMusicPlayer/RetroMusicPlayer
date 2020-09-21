package code.name.monkey.retromusic.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface PlaylistDao {
    @Insert
    suspend fun createPlaylist(playlistEntity: PlaylistEntity): Long

    @Query("UPDATE PlaylistEntity SET playlist_name = :name WHERE playlist_id = :playlistId")
    suspend fun renamePlaylist(playlistId: Long, name: String)

    @Query("SELECT * FROM PlaylistEntity WHERE playlist_name = :name")
    fun isPlaylistExists(name: String): List<PlaylistEntity>

    @Query("SELECT * FROM PlaylistEntity")
    suspend fun playlists(): List<PlaylistEntity>

    @Query("DELETE FROM SongEntity WHERE playlist_creator_id = :playlistId")
    suspend fun deletePlaylistSongs(playlistId: Long)

    @Query("DELETE FROM SongEntity WHERE playlist_creator_id = :playlistId AND id = :songId")
    suspend fun deleteSongFromPlaylist(playlistId: Long, songId: Long)

    @Transaction
    @Query("SELECT * FROM PlaylistEntity")
    suspend fun playlistsWithSongs(): List<PlaylistWithSongs>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSongsToPlaylist(songEntities: List<SongEntity>)

    @Query("SELECT * FROM SongEntity WHERE playlist_creator_id = :playlistId AND id = :songId")
    suspend fun isSongExistsInPlaylist(playlistId: Long, songId: Long): List<SongEntity>

    @Query("SELECT * FROM SongEntity WHERE playlist_creator_id = :playlistId")
    fun songsFromPlaylist(playlistId: Long): LiveData<List<SongEntity>>

    @Delete
    suspend fun deletePlaylist(playlistEntity: PlaylistEntity)

    @Delete
    suspend fun deletePlaylists(playlistEntities: List<PlaylistEntity>)

    @Delete
    suspend fun deletePlaylistSongs(songs: List<SongEntity>)


    @Query("SELECT * FROM SongEntity WHERE playlist_creator_id= :playlistId")
    fun favoritesSongsLiveData(playlistId: Long): LiveData<List<SongEntity>>

    @Query("SELECT * FROM SongEntity WHERE playlist_creator_id= :playlistId")
    fun favoritesSongs(playlistId: Long): List<SongEntity>


}