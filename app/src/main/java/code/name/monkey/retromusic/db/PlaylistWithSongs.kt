package code.name.monkey.retromusic.db

import androidx.room.Embedded
import androidx.room.Relation

data class PlaylistWithSongs(
    @Embedded val playlistEntity: PlaylistEntity,
    @Relation(
        parentColumn = "playlist_id",
        entityColumn = "playlist_creator_id"
    ) val songs: List<SongEntity>
)