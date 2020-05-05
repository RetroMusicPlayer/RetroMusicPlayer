package code.name.monkey.retromusic.room.playlist

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PlaylistSongDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPlaylistSongs(playlistSongs: List<PlaylistSongEntity>)

    @Query("SELECT * FROM playlist_songs WHERE playlist_id =:playlistId AND id=:id")
    suspend fun checkPlaylistSongExist(playlistId: Int, id: Int): List<PlaylistSongEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPlaylist(playlistEntity: PlaylistEntity)

    @Query("SELECT * FROM playlist_table")
    suspend fun getPlaylists(): List<PlaylistEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSingle(playlistSongEntity: PlaylistSongEntity)
}