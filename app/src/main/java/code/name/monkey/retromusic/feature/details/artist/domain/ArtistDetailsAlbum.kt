package code.name.monkey.retromusic.feature.details.artist.domain

data class ArtistDetailsAlbum(
    val id: String,
    val title: String,
    val artist: String,
    val coverArtUrl: String,
    val year: Int
)