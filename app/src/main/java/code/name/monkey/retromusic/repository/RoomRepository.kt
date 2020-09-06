package code.name.monkey.retromusic.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import code.name.monkey.retromusic.db.*
import code.name.monkey.retromusic.model.Song


interface RoomRepository {
    fun historySongs(): List<HistoryEntity>
    fun favoritePlaylistLiveData(favorite: String): LiveData<List<SongEntity>>
    fun insertBlacklistPath(blackListStoreEntity: BlackListStoreEntity)
    fun observableHistorySongs(): LiveData<List<HistoryEntity>>
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
    suspend fun favoritePlaylist(favorite: String): PlaylistEntity
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
    suspend fun insertBlacklistPath(blackListStoreEntities: List<BlackListStoreEntity>)
    suspend fun deleteBlacklistPath(blackListStoreEntity: BlackListStoreEntity)
    suspend fun clearBlacklist()
    suspend fun insertBlacklistPathAsync(blackListStoreEntity: BlackListStoreEntity)
    suspend fun blackListPaths(): List<BlackListStoreEntity>
}

class RealRoomRepository(
    private val playlistDao: PlaylistDao,
    private val blackListStoreDao: BlackListStoreDao,
    private val playCountDao: PlayCountDao,
    private val historyDao: HistoryDao
) : RoomRepository {
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
    override suspend fun insertSongs(songs: List<SongEntity>) =
        playlistDao.insertSongsToPlaylist(songs)

    override suspend fun getSongs(playlistEntity: PlaylistEntity): List<SongEntity> =
        playlistDao.songsFromPlaylist(playlistEntity.playListId)

    override suspend fun deletePlaylistEntities(playlistEntities: List<PlaylistEntity>) =
        playlistDao.deletePlaylists(playlistEntities)

    override suspend fun renamePlaylistEntity(playlistId: Int, name: String) =
        playlistDao.renamePlaylist(playlistId, name)

    override suspend fun deleteSongsInPlaylist(songs: List<SongEntity>) =
        playlistDao.deleteSongsInPlaylist(songs)

    override suspend fun deleteSongsFromPlaylist(playlists: List<PlaylistEntity>) =
        playlists.forEach {
            playlistDao.deleteSongsInPlaylist(it.playListId)
        }

    override suspend fun favoritePlaylist(favorite: String): PlaylistEntity {
        val playlist: PlaylistEntity? = playlistDao.isPlaylistExists(favorite).firstOrNull()
        return if (playlist != null) {
            playlist
        } else {
            createPlaylist(PlaylistEntity(favorite))
            playlistDao.isPlaylistExists(favorite).first()
        }
    }

    override suspend fun isFavoriteSong(songEntity: SongEntity): List<SongEntity> =
        playlistDao.isSongExistsInPlaylist(
            songEntity.playlistCreatorId,
            songEntity.id
        )

    override suspend fun removeSongFromPlaylist(songEntity: SongEntity) =
        playlistDao.removeSongFromPlaylist(songEntity.playlistCreatorId, songEntity.id)

    override suspend fun addSongToHistory(currentSong: Song) =
        historyDao.insertSongInHistory(currentSong.toHistoryEntity(System.currentTimeMillis()))

    override suspend fun songPresentInHistory(song: Song): HistoryEntity? =
        historyDao.isSongPresentInHistory(song.id)

    override suspend fun updateHistorySong(song: Song) =
        historyDao.updateHistorySong(song.toHistoryEntity(System.currentTimeMillis()))

    override fun observableHistorySongs(): LiveData<List<HistoryEntity>> =
        historyDao.observableHistorySongs()

    override fun historySongs(): List<HistoryEntity> = historyDao.historySongs()

    override fun favoritePlaylistLiveData(favorite: String): LiveData<List<SongEntity>> =
        playlistDao.favoritesSongsLiveData(
            playlistDao.isPlaylistExists(favorite).first().playListId
        )

    override suspend fun favoritePlaylistSongs(favorite: String): List<SongEntity> =
        if (playlistDao.isPlaylistExists(favorite).isNotEmpty())
            playlistDao.favoritesSongs(
                playlistDao.isPlaylistExists(favorite).first().playListId
            ) else emptyList()

    override suspend fun insertSongInPlayCount(playCountEntity: PlayCountEntity) =
        playCountDao.insertSongInPlayCount(playCountEntity)

    override suspend fun updateSongInPlayCount(playCountEntity: PlayCountEntity) =
        playCountDao.updateSongInPlayCount(playCountEntity)

    override suspend fun deleteSongInPlayCount(playCountEntity: PlayCountEntity) =
        playCountDao.deleteSongInPlayCount(playCountEntity)

    override suspend fun checkSongExistInPlayCount(songId: Int): List<PlayCountEntity> =
        playCountDao.checkSongExistInPlayCount(songId)

    override suspend fun playCountSongs(): List<PlayCountEntity> =
        playCountDao.playCountSongs()

    override fun insertBlacklistPath(blackListStoreEntity: BlackListStoreEntity) =
        blackListStoreDao.insertBlacklistPath(blackListStoreEntity)

    override suspend fun insertBlacklistPath(blackListStoreEntities: List<BlackListStoreEntity>) =
        blackListStoreDao.insertBlacklistPath(blackListStoreEntities)

    override suspend fun insertBlacklistPathAsync(blackListStoreEntity: BlackListStoreEntity) =
        blackListStoreDao.insertBlacklistPath(blackListStoreEntity)

    override suspend fun blackListPaths(): List<BlackListStoreEntity> =
        blackListStoreDao.blackListPaths()

    override suspend fun deleteBlacklistPath(blackListStoreEntity: BlackListStoreEntity) =
        blackListStoreDao.deleteBlacklistPath(blackListStoreEntity)

    override suspend fun clearBlacklist() = blackListStoreDao.clearBlacklist()
}