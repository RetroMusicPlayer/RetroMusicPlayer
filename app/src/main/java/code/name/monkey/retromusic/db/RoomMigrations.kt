package code.name.monkey.retromusic.db

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_23_24 = object : Migration(23, 24) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("DROP TABLE LyricsEntity")
        database.execSQL("DROP TABLE BlackListStoreEntity")
    }
}

val MIGRATION_24_25 = object : Migration(24, 25) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Must match ListeningEventEntity exactly, Room validates the schema after migrating.
        database.execSQL(
            "CREATE TABLE IF NOT EXISTS `ListeningEventEntity` (" +
                    "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "`song_id` INTEGER NOT NULL, " +
                    "`title` TEXT NOT NULL, " +
                    "`artist_name` TEXT NOT NULL, " +
                    "`album_name` TEXT NOT NULL, " +
                    "`duration` INTEGER NOT NULL, " +
                    "`played_at` INTEGER NOT NULL, " +
                    "`listened_ms` INTEGER NOT NULL)"
        )
    }
}