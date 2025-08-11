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
    var skips: Int = 0,
    val bpm: Float? = null
)

// Dummy classes to replace missing references (You should replace these with actual project classes)
data class SongTMPContainer(
    val title: String,
    val artistName: List<String>?, // fixed: List type assumed
    val data: String,
    val year: Int? = null,
    val liked: Boolean? = false,
    val favorite: Boolean? = false,
    val rating: Int? = 0
)

data class SongStatistics(
    val songId: String,
    var playCount: Int = 0,
    var skipCount: Int = 0,
    var rating: Int = 0,
    var lastPlayed: Long = 0L
)

data class DataStatistics(
    val songStats: Map<String, Map<String, Any>>? = null,
    val sequences: List<Pair<String, Int>>? = null,
    val genres: List<Pair<String, Int>>? = null,
    val artists: List<Pair<String, Int>>? = null
)

/**
 * Enum representing different music flow types for shuffling.
 */
enum class FlowType { RollerCoaster, WindDown, MoodLift, Pulse, Wave }