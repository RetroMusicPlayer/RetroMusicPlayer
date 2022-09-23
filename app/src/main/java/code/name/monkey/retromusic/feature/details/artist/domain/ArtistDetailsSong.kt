package code.name.monkey.retromusic.feature.details.artist.domain

data class ArtistDetailsSong(
    val id: String,
    val title: String,
    val artist: String,
    val album: String,
    val albumId: String,
    val track: Int,
    val duration: Long,
    val year: Int,
    val coverArtUrl: String
)
