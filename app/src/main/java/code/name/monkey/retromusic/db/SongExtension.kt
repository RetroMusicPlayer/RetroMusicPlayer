package code.name.monkey.retromusic.db

import code.name.monkey.retromusic.model.Song

fun List<SongEntity>.toSongs(): List<Song> {
    return map {
        it.toSong()
    }
}

fun List<Song>.toSongs(playlistId: Int): List<SongEntity> {
    return map {
        it.toSongEntity(playlistId)
    }
}

fun SongEntity.toSong(): Song {
    return Song(
        id,
        title,
        trackNumber,
        year,
        duration,
        data,
        dateModified,
        albumId,
        albumName,
        artistId,
        artistName,
        composer,
        albumArtist
    )
}

fun PlayCountEntity.toSong(): Song {
    return Song(
        id,
        title,
        trackNumber,
        year,
        duration,
        data,
        dateModified,
        albumId,
        albumName,
        artistId,
        artistName,
        composer,
        albumArtist
    )
}

fun HistoryEntity.toSong(): Song {
    return Song(
        id,
        title,
        trackNumber,
        year,
        duration,
        data,
        dateModified,
        albumId,
        albumName,
        artistId,
        artistName,
        composer,
        albumArtist
    )
}

fun Song.toPlayCount(): PlayCountEntity {
    return PlayCountEntity(
        id,
        title,
        trackNumber,
        year,
        duration,
        data,
        dateModified,
        albumId,
        albumName,
        artistId,
        artistName,
        composer,
        albumArtist,
        System.currentTimeMillis(),
        1
    )
}

fun List<Song>.toSongsEntity(playlistEntity: PlaylistEntity): List<SongEntity> {
    return map {
        it.toSongEntity(playlistEntity.playListId)
    }
}
