package code.name.monkey.retromusic.feature.details.album.domain

data class AlbumDetails(
    val id: String,
    val title: String,
    val artist: Artist,
    val coverArtUrl: String,
    val year: Int,
    val songs: List<Song>
) {
    data class Artist(
        val id: String,
        val title: String,
        val coverArtUrl: String
    )

    data class Song(
        val id: String,
        val albumId: String,
        val coverArtUrl: String,
        val title: String,
        val artist: String,
        val album: String,
        val year: Int
    )
}
