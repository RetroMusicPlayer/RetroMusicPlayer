package code.name.monkey.retromusic.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [PlaylistEntity::class, SongEntity::class, HistoryEntity::class, PlayCountEntity::class, BlackListStoreEntity::class],
    version = 20,
    exportSchema = false
)
abstract class RetroDatabase : RoomDatabase() {
    abstract fun playlistDao(): PlaylistDao
    abstract fun blackListStore(): BlackListStoreDao
    abstract fun playCountDao(): PlayCountDao
}