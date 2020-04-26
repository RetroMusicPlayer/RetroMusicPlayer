package code.name.monkey.retromusic.room

import code.name.monkey.retromusic.model.Song

class MusicQueueRepository(private val queueDao: QueueDao) {

    fun getQueue(): List<Song> = queueDao.getQueue()

    fun getOriginalQueue(): List<SongEntity> = queueDao.getOriginalQueue()

    suspend fun insertQueue(queue: List<Song>) {
        queueDao.saveQueue(queue)
    }

    suspend fun insertOriginalQueue(queue: List<SongEntity>) {
        queueDao.saveOriginalQueue(queue)
    }
}