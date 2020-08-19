package code.name.monkey.retromusic.db

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
class PlaylistEntity(
    @PrimaryKey
    @ColumnInfo(name = "playlist_name")
    val playlistName: String
) : Parcelable