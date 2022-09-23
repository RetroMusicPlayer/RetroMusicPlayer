package code.name.monkey.retromusic.feature.details.artist.domain

data class ArtistDetails(
    val title: String,
    val biography: String,
    val coverArtUrl: String,
    val albumCount: Int,
    val songCount: Int,
    val totalDuration: Long
)
