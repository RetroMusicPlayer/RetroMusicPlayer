package code.name.monkey.retromusic.room

import androidx.room.Transaction
import code.name.monkey.retromusic.model.Song

class MusicQueueRepository(private val queueDao: QueueDao) {

    fun getQueue(): List<Song> =
        queueDao.getQueue().map { SongQueueEntity.toSong(it) }

    fun getOriginalQueue(): List<Song> =
        queueDao.getOriginalQueue().map { SongEntity.toSong(it) }

    suspend fun insertQueue(queue: List<Song>) {
        deleteAndSave(queue)
    }

    @Transaction
    private suspend fun deleteAndSave(queue: List<Song>) {
        queueDao.deleteQueue()
        queueDao.saveQueue(queue.map { Song.toSongEntity(it) })
    }

    suspend fun insertOriginalQueue(queue: List<Song>) {
        deleteAndSaveOriginalQueue(queue)
    }

    @Transaction
    private suspend fun deleteAndSaveOriginalQueue(queue: List<Song>) {
        queueDao.deleteOriginalQueue()
        queueDao.saveOriginalQueue(queue.map { Song.toSongQueueEntity(it) })
    }
}