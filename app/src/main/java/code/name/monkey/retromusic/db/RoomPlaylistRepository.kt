package code.name.monkey.retromusic.db

import androidx.annotation.WorkerThread

class RoomPlaylistRepository(private val playlistDao: PlaylistDao) {

    @WorkerThread
    suspend fun getPlaylistWithSongs(): List<PlaylistWithSongs> = playlistDao.playlistsWithSong()
}