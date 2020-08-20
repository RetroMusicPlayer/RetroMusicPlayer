package code.name.monkey.retromusic.db

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Relation
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PlaylistWithSongs(
    @Embedded val playlistEntity: PlaylistEntity,
    @Relation(
        parentColumn = "playlist_id",
        entityColumn = "playlist_creator_id"
    )
    val songs: List<SongEntity>
):Parcelable