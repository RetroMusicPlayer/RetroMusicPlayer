package code.name.monkey.retromusic.room.playlist

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import code.name.monkey.retromusic.App

@Database(
    entities = [PlaylistSongEntity::class, PlaylistEntity::class],
    version = 6,
    exportSchema = false
)
abstract class PlaylistDatabase : RoomDatabase() {
    abstract fun playlistDao(): PlaylistSongDao

    companion object {
        fun instance() = Room.databaseBuilder(
            App.getContext().applicationContext,
            PlaylistDatabase::class.java, "retro_playlist"
        ).fallbackToDestructiveMigration().build()
    }
}