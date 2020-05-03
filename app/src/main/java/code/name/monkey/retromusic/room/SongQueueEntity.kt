package code.name.monkey.retromusic.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import code.name.monkey.retromusic.BuildConfig
import code.name.monkey.retromusic.model.Song

@Entity(tableName = "playing_queue_${BuildConfig.FLAVOR}")
data class SongQueueEntity(
    @ColumnInfo(name = "id") var id: Int,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "track_number") var trackNumber: Int,
    @ColumnInfo(name = "year") var year: Int,
    @ColumnInfo(name = "duration") var duration: Long,
    @ColumnInfo(name = "data") var data: String,
    @ColumnInfo(name = "date_modified") var dateModified: Long,
    @ColumnInfo(name = "album_id") var albumId: Int,
    @ColumnInfo(name = "album_name") var albumName: String,
    @ColumnInfo(name = "artist_id") var artistId: Int,
    @ColumnInfo(name = "artist_name") var artistName: String,
    @ColumnInfo(name = "composer") var composer: String?
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "primary_id")
    var primaryId: Int? = null

    companion object {
        fun toSong(song: SongQueueEntity): Song {
            return Song(
                song.id,
                song.title,
                song.trackNumber,
                song.year,
                song.duration,
                song.data,
                song.dateModified,
                song.albumId,
                song.albumName,
                song.artistId,
                song.artistName,
                song.composer
            )
        }
    }
}