package code.name.monkey.retromusic.util

object ArtistSeparator {

    val SEPARATORS: Array<String> = arrayOf("/", ";", ",", "feat.", "ft.")

    private val regex = SEPARATORS.joinToString("|") { Regex.escape(it) }.toRegex(RegexOption.IGNORE_CASE)

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