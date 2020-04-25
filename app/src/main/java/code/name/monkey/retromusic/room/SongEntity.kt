package code.name.monkey.retromusic.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import code.name.monkey.retromusic.model.Song

@Entity(tableName = "song_entity")
class SongEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "track_number") val trackNumber: Int,
    @ColumnInfo(name = "year") val year: Int,
    @ColumnInfo(name = "duration") val duration: Long,
    @ColumnInfo(name = "data") val data: String,
    @ColumnInfo(name = "date_modified") val dateModified: Long,
    @ColumnInfo(name = "album_id") val albumId: Int,
    @ColumnInfo(name = "album_name") val albumName: String,
    @ColumnInfo(name = "artist_id") val artistId: Int,
    @ColumnInfo(name = "artist_name") val artistName: String,
    @ColumnInfo(name = "composer") val composer: String?
) {
    companion object {
        fun toSong(song: SongEntity): Song {
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