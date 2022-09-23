package code.name.monkey.retromusic.feature.details.artist.presentation.entity

data class ArtistDetailsUi(
    val title: String,
    val biography: String,
    val coverArtUrl: String,
    val albumCount: Int,
    val songCount: Int,
    val totalDuration: Long
)
