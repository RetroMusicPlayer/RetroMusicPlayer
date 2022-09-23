package code.name.monkey.retromusic.feature.details.artist.presentation.entity

data class ArtistDetailsSongUi(
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
