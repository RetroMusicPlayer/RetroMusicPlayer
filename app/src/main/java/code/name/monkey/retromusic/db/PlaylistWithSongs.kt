package code.name.monkey.retromusic.db

import androidx.room.Embedded
import androidx.room.Relation

data class PlaylistWithSongs(
    @Embedded val playlistEntity: PlaylistEntity,
    @Relation(
        parentColumn = "playlist_name",
        entityColumn = "playlist_creator_name"
    ) val songs: List<SongEntity>
)