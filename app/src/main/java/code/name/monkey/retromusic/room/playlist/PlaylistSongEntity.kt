package code.name.monkey.retromusic.room.playlist

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_songs")
class PlaylistSongEntity(
    @ForeignKey(
        entity = PlaylistEntity::class,
        childColumns = ["playlist_id"],
        parentColumns = ["playlist_id"],
        onDelete = ForeignKey.CASCADE
    )
    @ColumnInfo(name = "playlist_id") val playlistId: Int,
    @ColumnInfo(name = "playlist_name") val playlistName: String,

    @ColumnInfo(name = "id") val id: Int,
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
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "song_id")
    var songId: Int = 0
}