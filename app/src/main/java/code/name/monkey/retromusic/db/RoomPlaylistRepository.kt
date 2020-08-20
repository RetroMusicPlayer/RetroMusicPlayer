package code.name.monkey.retromusic.db

import androidx.annotation.WorkerThread


interface RoomPlaylistRepository {
    suspend fun createPlaylist(playlistEntity: PlaylistEntity): Long
    suspend fun checkPlaylistExists(playlistName: String): List<PlaylistEntity>
    suspend fun playlists(): List<PlaylistEntity>
    suspend fun playlistWithSongs(): List<PlaylistWithSongs>
    suspend fun insertSongs(songs: List<SongEntity>)
    suspend fun getSongs(playlistEntity: PlaylistEntity): List<SongEntity>
}

class RealRoomPlaylistRepository(private val playlistDao: PlaylistDao) : RoomPlaylistRepository {
    @WorkerThread
    override suspend fun createPlaylist(playlistEntity: PlaylistEntity): Long =
        playlistDao.createPlaylist(playlistEntity)

    @WorkerThread
    override suspend fun checkPlaylistExists(playlistName: String): List<PlaylistEntity> =
        playlistDao.checkPlaylistExists(playlistName)

    @WorkerThread
    override suspend fun playlists(): List<PlaylistEntity> = playlistDao.playlists()

    @WorkerThread
    override suspend fun playlistWithSongs(): List<PlaylistWithSongs> =
        playlistDao.playlistsWithSong()

    @WorkerThread
    override suspend fun insertSongs(songs: List<SongEntity>) {
        /* val tempList = ArrayList<SongEntity>(songs)
         val existingSongs = songs.map {
             playlistDao.checkSongExistsWithPlaylistName(it.playlistCreatorName, it.songId)
         }.first()
         println("Existing ${existingSongs.size}")
         tempList.removeAll(existingSongs)*/
        playlistDao.insertSongs(songs)
    }

    override suspend fun getSongs(playlistEntity: PlaylistEntity): List<SongEntity> {
        return playlistDao.getSongs(playlistEntity.playListId)
    }
}