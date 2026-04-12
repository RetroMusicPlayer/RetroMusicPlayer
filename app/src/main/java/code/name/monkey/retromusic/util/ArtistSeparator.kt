package code.name.monkey.retromusic.util

/**
 * Utility for splitting multi-artist strings into individual artist names.
 * 
 * Handles common separators used in audio tags like "/", ";", ",", "feat.", "ft."
 */
object ArtistSeparator {

    /**
     * Array of separators commonly used to delimit multiple artists in metadata.
     */
    val SEPARATORS: Array<String> = arrayOf("/", ";", ",", "feat.", "ft.")

    private val regex = SEPARATORS.joinToString("|") { 
        Regex.escape(it) 
    }.toRegex(RegexOption.IGNORE_CASE)

    /**
     * Splits artist names by common separators.
     * 
     * Examples:
     * - `"Artist1 / Artist2"` → `["Artist1", "Artist2"]`
     * - `"Artist1 feat. Artist2"` → `["Artist1", "Artist2"]`
     * - `"Artist1, Artist2, Artist3"` → `["Artist1", "Artist2", "Artist3"]`
     * 
     * @param artistString The artist name(s) to split
     * @return List of individual artist names, trimmed and deduplicated. 
     *         Returns empty list if input is null or empty.
     */
    fun split(artistString: String?): List<String> {
        if (artistString.isNullOrEmpty()) {
            return emptyList()
        }

        return artistString.split(regex)
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .distinct()
    }
}