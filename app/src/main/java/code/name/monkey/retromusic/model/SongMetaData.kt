package code.name.monkey.retromusic.model

// Your custom data model for enriched songs
data class SongMetaData(
    val title: String,
    val artists: List<String>,
    var file: String,
    val mood: List<String>,
    val genre: List<String>,
    val playlist: List<String> = emptyList(),
    val year: String = "",
    var liked: Boolean = false,
    var favorite: Boolean = false,
    var rating: Int = 0,
    val danceability: Double? = null, // corrected spelling
    val tempo: Double? = null,        // in BPM (e.g., 120.0)
    val energy: Double? = null,       // 0.0 - 1.0 (intensity/loudness)
    val valence: Double? = null,      // 0.0 - 1.0 (musical positivity)
    val market: List<String>? = null,
    var skips: Int = 0
)