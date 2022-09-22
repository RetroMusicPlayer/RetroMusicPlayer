package code.name.monkey.retromusic.feature.details.album.presentation

data class AlbumDetailsUi(
    val id: String,
    val title: String,
    val artist: ArtistUi,
    val coverArtUrl: String,
    val year: Int,
    val songs: List<SongUi>
) {
    data class ArtistUi(
        val id: String,
        val title: String,
        val coverArtUrl: String
    )

    data class SongUi(
        val id: String,
        val albumId: String,
        val coverArtUrl: String,
        val title: String,
        val artist: String,
        val album: String,
        val year: Int
    )
}
