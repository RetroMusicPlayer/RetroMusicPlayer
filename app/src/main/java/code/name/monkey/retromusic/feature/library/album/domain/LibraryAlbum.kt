package code.name.monkey.retromusic.feature.library.album.domain

data class LibraryAlbum(
    val id: String,
    val title: String,
    val artist: String,
    val coverUrl: String,
    val year: Int,
)
