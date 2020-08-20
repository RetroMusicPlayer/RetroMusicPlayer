package code.name.monkey.retromusic.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class SongEntity(
    @ColumnInfo(name = "song_id")
    val songId: Int,
    @ColumnInfo(name = "playlist_creator_id")
    val playlistCreatorId: Int
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "song_key")
    var songPrimaryKey: Long = 0
}