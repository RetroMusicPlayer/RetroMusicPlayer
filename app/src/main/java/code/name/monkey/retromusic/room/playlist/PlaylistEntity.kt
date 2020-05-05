package code.name.monkey.retromusic.room.playlist

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "playlist_table")
class PlaylistEntity(
    @ColumnInfo(name = "playlist_name") val playlistName: String
) : Parcelable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "playlist_id")
    var playlistId: Int = 0
}