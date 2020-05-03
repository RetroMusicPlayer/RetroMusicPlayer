package code.name.monkey.retromusic.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import code.name.monkey.retromusic.BuildConfig
import code.name.monkey.retromusic.model.Song

@Database(entities = [SongQueueEntity::class, SongEntity::class], version = 8, exportSchema = false)
abstract class MusicPlaybackQueueStoreDatabase : RoomDatabase() {

    abstract fun queueDao(): QueueDao

    companion object {
        @Volatile
        private var INSTANCE: MusicPlaybackQueueStoreDatabase? = null

        fun getMusicDatabase(context: Context): MusicPlaybackQueueStoreDatabase {
            val tempInstance =
                INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MusicPlaybackQueueStoreDatabase::class.java,
                    "music_playback_state"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                return instance
            }
        }
    }
} 