package code.name.monkey.retromusic.db

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
class SongEntity(
    @ColumnInfo(name = "song_id")
    val songId: Int,
    @ColumnInfo(name = "playlist_creator_id")
    val playlistCreatorId: Int
) : Parcelable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "song_key")
    var songPrimaryKey: Long = 0
}