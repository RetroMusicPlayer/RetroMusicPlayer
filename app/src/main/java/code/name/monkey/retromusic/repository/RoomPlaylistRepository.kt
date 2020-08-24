package code.name.monkey.retromusic.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import code.name.monkey.retromusic.db.*
import code.name.monkey.retromusic.model.Song


interface RoomPlaylistRepository {
    suspend fun createPlaylist(playlistEntity: PlaylistEntity): Long
    suspend fun checkPlaylistExists(playlistName: String): List<PlaylistEntity>
    suspend fun playlists(): List<PlaylistEntity>
    suspend fun playlistWithSongs(): List<PlaylistWithSongs>
    suspend fun insertSongs(songs: List<SongEntity>)
    suspend fun getSongs(playlistEntity: PlaylistEntity): List<SongEntity>
    suspend fun deletePlaylistEntities(playlistEntities: List<PlaylistEntity>)
    suspend fun renamePlaylistEntity(playlistId: Int, name: String)
    suspend fun deleteSongsInPlaylist(songs: List<SongEntity>)
    suspend fun deleteSongsFromPlaylist(playlists: List<PlaylistEntity>)
    suspend fun favoritePlaylist(favorite: String): List<PlaylistEntity>
    suspend fun isFavoriteSong(songEntity: SongEntity): List<SongEntity>
    suspend fun removeSongFromPlaylist(songEntity: SongEntity)
    suspend fun addSongToHistory(currentSong: Song)
    suspend fun songPresentInHistory(song: Song): HistoryEntity?
    suspend fun updateHistorySong(song: Song)
    suspend fun favoritePlaylistSongs(favorite: String): List<SongEntity>
    suspend fun insertSongInPlayCount(playCountEntity: PlayCountEntity)
    suspend fun updateSongInPlayCount(playCountEntity: PlayCountEntity)
    suspend fun deleteSongInPlayCount(playCountEntity: PlayCountEntity)
    suspend fun checkSongExistInPlayCount(songId: Int): List<PlayCountEntity>
    suspend fun playCountSongs(): List<PlayCountEntity>
    fun historySongs(): LiveData<List<HistoryEntity>>
    fun favoritePlaylistLiveData(favorite: String): LiveData<List<SongEntity>>
}

class RealRoomRepository(
    private val playlistDao: PlaylistDao
) : RoomPlaylistRepository {
    @WorkerThread
    override suspend fun createPlaylist(playlistEntity: PlaylistEntity): Long =
        playlistDao.createPlaylist(playlistEntity)

    @WorkerThread
    override suspend fun checkPlaylistExists(playlistName: String): List<PlaylistEntity> =
        playlistDao.isPlaylistExists(playlistName)

    @WorkerThread
    override suspend fun playlists(): List<PlaylistEntity> = playlistDao.playlists()

    @WorkerThread
    override suspend fun playlistWithSongs(): List<PlaylistWithSongs> =
        playlistDao.playlistsWithSongs()

    @WorkerThread
    override suspend fun insertSongs(songs: List<SongEntity>) {
        /* val tempList = ArrayList<SongEntity>(songs)
         val existingSongs = songs.map {
             playlistDao.checkSongExistsWithPlaylistName(it.playlistCreatorName, it.songId)
         }.first()
         println("Existing ${existingSongs.size}")
         tempList.removeAll(existingSongs)*/
        playlistDao.insertSongsToPlaylist(songs)
    }

    override suspend fun getSongs(playlistEntity: PlaylistEntity): List<SongEntity> {
        return playlistDao.songsFromPlaylist(playlistEntity.playListId)
    }

    override suspend fun deletePlaylistEntities(playlistEntities: List<PlaylistEntity>) =
        playlistDao.deletePlaylists(playlistEntities)

    override suspend fun renamePlaylistEntity(playlistId: Int, name: String) =
        playlistDao.renamePlaylist(playlistId, name)

    override suspend fun deleteSongsInPlaylist(songs: List<SongEntity>) =
        playlistDao.deleteSongsInPlaylist(songs)

    override suspend fun deleteSongsFromPlaylist(playlists: List<PlaylistEntity>) {
        playlists.forEach {
            playlistDao.deleteSongsInPlaylist(it.playListId)
        }
    }

    override suspend fun favoritePlaylist(favorite: String): List<PlaylistEntity> =
        playlistDao.isPlaylistExists(favorite)

    override suspend fun isFavoriteSong(songEntity: SongEntity): List<SongEntity> =
        playlistDao.isSongExistsInPlaylist(
            songEntity.playlistCreatorId,
            songEntity.id
        )

    override suspend fun removeSongFromPlaylist(songEntity: SongEntity) =
        playlistDao.removeSongFromPlaylist(songEntity.playlistCreatorId, songEntity.id)

    override suspend fun addSongToHistory(currentSong: Song) =
        playlistDao.insertSongInHistory(currentSong.toHistoryEntity(System.currentTimeMillis()))

    override suspend fun songPresentInHistory(song: Song): HistoryEntity? =
        playlistDao.isSongPresentInHistory(song.id)

    override suspend fun updateHistorySong(song: Song) =
        playlistDao.updateHistorySong(song.toHistoryEntity(System.currentTimeMillis()))

    override fun historySongs(): LiveData<List<HistoryEntity>> {
        return playlistDao.historySongs()
    }

    override fun favoritePlaylistLiveData(favorite: String): LiveData<List<SongEntity>> =
        playlistDao.favoritesSongsLiveData(
            playlistDao.isPlaylistExists(favorite).first().playListId
        )

    override suspend fun favoritePlaylistSongs(favorite: String): List<SongEntity> {
        return if (playlistDao.isPlaylistExists(favorite).isNotEmpty())
            playlistDao.favoritesSongs(
                playlistDao.isPlaylistExists(favorite).first().playListId
            ) else emptyList()
    }

    override suspend fun insertSongInPlayCount(playCountEntity: PlayCountEntity) =
        playlistDao.insertSongInPlayCount(playCountEntity)

    override suspend fun updateSongInPlayCount(playCountEntity: PlayCountEntity) =
        playlistDao.updateSongInPlayCount(playCountEntity)

    override suspend fun deleteSongInPlayCount(playCountEntity: PlayCountEntity) =
        playlistDao.deleteSongInPlayCount(playCountEntity)

    override suspend fun checkSongExistInPlayCount(songId: Int): List<PlayCountEntity> =
        playlistDao.checkSongExistInPlayCount(songId)

    override suspend fun playCountSongs(): List<PlayCountEntity> =
        playlistDao.playCountSongs()
}