package code.name.monkey.retromusic.helper

import code.name.monkey.retromusic.model.SongMetaData
import code.name.monkey.retromusic.model.Song
import java.io.File
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import android.content.Context
import code.name.monkey.retromusic.model.FlowType

/**
 * Manages loading and caching of song data.
 */
object SongDataManager {
    var defaultSongsJson = "[]"

    /**
     * Loads the default songs JSON from file or uses initial data if file is missing/empty.
     */
    fun loadDefaultSongsJson(context: Context) {
        val file = File(context.filesDir, "outputile.txt")
        defaultSongsJson = if (!file.exists() || file.readText().isBlank()) {
            "[]"
        } else {
            file.readText()
        }
    }

    var songs: MutableList<SongMetaData> = mutableListOf()

    /**
     * Finds a song by title.
     */
    fun getSongByTitle(title: String): SongMetaData? = songs.find { it.title == title }
}

/**
 * Helper object for advanced shuffle logic based on song metadata.
 */
object ShuffleHelper {

    // Caches metadata for performance.
    private var metadataMap: Map<String, SongMetaData>? = null

    /**
     * Loads and caches metadata for songs by filename (case-insensitive, no extension).
     */
    private fun loadMetadataMap(): Map<String, SongMetaData> {
        if (metadataMap != null) return metadataMap!!

        val defaultSongsJson = SongDataManager.defaultSongsJson
        val listType = object : TypeToken<List<SongMetaData>>() {}.type
        val metadataList: List<SongMetaData> = Gson().fromJson(defaultSongsJson, listType)

        // ... inside loadMetadataMap
        val map = metadataList.filter { it.file.isNotBlank() }.associateBy { File(it.file).nameWithoutExtension.lowercase() }
        metadataMap = map
        return map
    }

    /**
     * Reorders the list to prioritize songs similar to the current one,
     * based on genre, mood, and artist similarity.
     *
     * @param listToShuffle The queue of songs to shuffle.
     * @param current The index of the current song.
     */
    fun makeShuffleList(listToShuffle: MutableList<Song>, current: Int) {
        if (listToShuffle.isEmpty() || current !in listToShuffle.indices) return

        val metadata = loadMetadataMap()
        val currentSong = listToShuffle.removeAt(current)
        val currentMeta = metadata[getSongKey(currentSong)]

        // If no metadata for any song, fallback to normal shuffle
        val hasAnyMetadata = listToShuffle.any { metadata[getSongKey(it)] != null }
        if (currentMeta == null || !hasAnyMetadata) {
            listToShuffle.shuffle()
            listToShuffle.add(0, currentSong)
            return
        }

        val scoredSongs = scoreSongs(listToShuffle, metadata, currentMeta)
        val originalScored = scoredSongs.toMutableList()

        // Select a flow based on current song's metadata
        val selectedFlow = selectFlowType(currentMeta)
        val reordered: List<Pair<Song, Int>> = reorderByFlow(selectedFlow, originalScored, metadata)
        val finalOrdered = enforceMaxMovement(reordered, originalScored, maxMovement = 5)
        val smartShuffled = finalOrdered
            .groupBy { it.second }
            .toSortedMap(compareByDescending { it })
            .flatMap { (_, group) -> group.shuffled().map { it.first } }

        // Add extra randomness: randomly swap a few pairs in the final list
        val extraRandomized = smartShuffled.toMutableList()
        val swapRange = 4
        val swaps = (extraRandomized.size / 7).coerceAtLeast(1)
        repeat(swaps) {
            val i = (1 until extraRandomized.size).random()
            // Only swap with a song within 3 positions away
            val minJ = (i - swapRange).coerceAtLeast(1)
            val maxJ = (i + swapRange).coerceAtMost(extraRandomized.size - 1)
            if (maxJ > minJ) {
                val j = (minJ..maxJ).filter { it != i }.random()
                val tmp = extraRandomized[i]
                extraRandomized[i] = extraRandomized[j]
                extraRandomized[j] = tmp
            }
        }

        // Rebuild queue: current song first
        listToShuffle.clear()
        listToShuffle.add(currentSong)
        listToShuffle.addAll(extraRandomized)
    }

    /**
     * Scores all songs in the list against the current song's metadata.
     */
    private fun scoreSongs(
        songs: List<Song>,
        metadata: Map<String, SongMetaData>,
        currentMeta: SongMetaData
    ): List<Pair<Song, Int>> {
        return songs.mapNotNull { song ->
            val meta = metadata[getSongKey(song)]
            if (meta == null || isCorrupted(meta)) {
                // Log corrupted metadata for debugging
                null
            } else {
                val score = calculateSimilarity(currentMeta, meta)
                Pair(song, score)
            }
        }
    }

    /**
     * Selects the flow type based on the current song's metadata.
     */
    private fun selectFlowType(currentMeta: SongMetaData): FlowType {
        return when {
            (currentMeta.energy ?: 0.0) > 0.7 && (currentMeta.danceability)!! > 0.7 -> FlowType.Pulse
            (currentMeta.energy ?: 0.0) < 0.4 && (currentMeta.valence ?: 0.0) < 0.4 -> FlowType.WindDown
            (currentMeta.valence ?: 0.0) > 0.7 && (currentMeta.energy ?: 0.0) > 0.4 -> FlowType.MoodLift
            currentMeta.mood.any { it.contains("party", ignoreCase = true) || it.contains("dance", ignoreCase = true) } -> FlowType.RollerCoaster
            (currentMeta.tempo ?: 0.0) > 130.0 -> FlowType.Wave
            else -> FlowType.RollerCoaster
        }
    }

    /**
     * Reorders the scored list according to the selected flow type.
     */
    private fun reorderByFlow(
        flow: FlowType,
        scored: List<Pair<Song, Int>>,
        metadata: Map<String, SongMetaData>
    ): List<Pair<Song, Int>> {
        fun Song.getMeta(): SongMetaData? = metadata[getSongKey(this)]
        return when (flow) {
            FlowType.RollerCoaster -> {
                // Alternate high/low energy, then valence
                val sorted = scored.sortedByDescending { it.first.getMeta()?.energy ?: 0.0 }
                val high = sorted.filterIndexed { i, _ -> i % 2 == 0 }
                val low = sorted.filterIndexed { i, _ -> i % 2 != 0 }.reversed()
                (high + low).take(scored.size)
            }
            FlowType.WindDown -> {
                // Descending energy, then valence
                scored.sortedWith(
                    compareByDescending<Pair<Song, Int>> { it.first.getMeta()?.energy ?: 0.0 }
                        .thenByDescending { it.first.getMeta()?.valence ?: 0.0 }
                )
            }
            FlowType.MoodLift -> {
                // Ascending valence, then energy
                scored.sortedWith(
                    compareBy<Pair<Song, Int>> { it.first.getMeta()?.valence ?: 0.0 }
                        .thenBy { it.first.getMeta()?.energy ?: 0.0 }
                )
            }
            FlowType.Pulse -> {
                // Alternate high/low danceability
                val sorted = scored.sortedByDescending { it.first.getMeta()?.danceability ?: 0.0 }
                val high = sorted.filterIndexed { i, _ -> i % 2 == 0 }
                val low = sorted.filterIndexed { i, _ -> i % 2 != 0 }.reversed()
                (high + low).take(scored.size)
            }
            FlowType.Wave -> {
                // Up and down: sort by energy, then reverse every 5 songs
                val sorted = scored.sortedByDescending { it.first.getMeta()?.energy ?: 0.0 }
                val chunked = sorted.chunked(5).flatMapIndexed { idx, chunk ->
                    if (idx % 2 == 0) chunk else chunk.reversed()
                }
                chunked
            }
        }
    }

    /**
     * Enforces a maximum movement constraint for each song to preserve some original order.
     */
    private fun enforceMaxMovement(
        reordered: List<Pair<Song, Int>>,
        originalScored: List<Pair<Song, Int>>,
        maxMovement: Int
    ): MutableList<Pair<Song, Int>> {
        val finalOrdered = MutableList(reordered.size) { reordered[it] }
        for ((originalIdx, pair) in reordered.withIndex()) {
            val origPos = originalScored.indexOf(pair)
            val minPos = (origPos - maxMovement).coerceAtLeast(0)
            val maxPos = (origPos + maxMovement).coerceAtMost(reordered.size - 1)
            val targetPos = originalIdx.coerceIn(minPos, maxPos)
            finalOrdered.remove(pair)
            finalOrdered.add(targetPos, pair)
        }
        return finalOrdered
    }

    /**
     * Extracts the lowercase filename (without extension) for metadata matching.
     */
    private fun getSongKey(song: Song): String {
        return File(song.data).nameWithoutExtension.lowercase()
    }

    /**
     * Calculates a similarity score between two songs.
     * Artist match: 10 pts each, Genre match: 9 pts each, Mood match: 8 pts each,
     * danceability: up to 5 pts (closer = higher), Market: 2 pts each, Year: up to 5 pts, Energy/Valence/Tempo: up to 5 pts each.
     */
    private fun calculateSimilarity(
        a: SongMetaData,
        b: SongMetaData,
        favoriteArtists: Set<String> = emptySet(),
        favoriteGenres: Set<String> = emptySet(),
        favoriteMoods: Set<String> = emptySet()
    ): Int {
        // 1. Artist Matching
        val commonArtists = a.artists.intersect(b.artists.toSet())
        val artistScore = commonArtists.size * 12  // Increased weight

        // 2. Genre Matching
        val commonGenres = a.genre.intersect(b.genre.toSet())
        val genreScore = commonGenres.size * 8

        // 3. Mood Matching
        val commonMoods = a.mood.intersect(b.mood.toSet())
        val moodScore = commonMoods.size * 8

        // 4. Danceability
        val danceabilityScore = (8 - (kotlin.math.abs(a.danceability?.minus(b.danceability ?: 0.0) ?: 0.0) * 4).coerceAtMost(8.0)).toInt()

        // 5. Market Similarity
        val marketScore = try {
            b.market?.let { a.market?.intersect(it.toSet())?.size ?: 0 }?.times(2) ?: 0
        } catch (e: Exception) { 0 }

        // 6. Year Proximity
        val yearScore = try {
            val aYear = a.year.toIntOrNull()
            val bYear = b.year.toIntOrNull()
            if (aYear != null && bYear != null && aYear > 0 && bYear > 0) {
                (4 - (kotlin.math.abs(aYear - bYear) / 2).coerceAtMost(10)).coerceAtLeast(-4)
            } else 0
        } catch (e: Exception) { 0 }

        // 7. Modern Song Bonus — strong boost for newer songs
        val modernBonus = try {
            val bYear = b.year.toIntOrNull()
            val normalized = (((bYear?.coerceIn(1990, 2025) ?: 0) - 1990) / 35.0)
            (normalized * 25).toInt()
        } catch (e: Exception) { 0 }

        // 8. Energy
        val energyScore = if (a.energy != null && b.energy != null) {
            (10 - (kotlin.math.abs(a.energy - b.energy) * 10).coerceAtMost(10.0)).toInt()
        } else 0

        // 9. Valence
        val valenceScore = if (a.valence != null && b.valence != null) {
            (10 - (kotlin.math.abs(a.valence - b.valence) * 10).coerceAtMost(10.0)).toInt()
        } else 0

        // 10. Tempo
        val tempoScore = if (a.tempo != null && b.tempo != null) {
            (10 - (kotlin.math.abs(a.tempo - b.tempo) / 10).coerceAtMost(10.0)).toInt()
        } else 0

        // 11. Genre-Based Artist Similarity
        val genreArtistSimilarity = getGenreBasedArtistSimilarity(a, b)

        // 12. Favorite Preference Boost (optional)
        val favArtistBoost = b.artists.count { it in favoriteArtists } * 15
        val favGenreBoost = b.genre.count { it in favoriteGenres } * 10
        val favMoodBoost = b.mood.count { it in favoriteMoods } * 10

        return artistScore +
            genreScore +
            moodScore +
            danceabilityScore +
            marketScore +
            yearScore +
            energyScore +
            valenceScore +
            tempoScore +
            genreArtistSimilarity +
            favArtistBoost +
            favGenreBoost +
            favMoodBoost +
            modernBonus
    }

    /**
     * Checks if the metadata is corrupted (basic check: file, artists, danceability).
     */
    private fun isCorrupted(meta: SongMetaData): Boolean {
        return meta.file.isBlank()
            || meta.artists.isEmpty()
            || meta.danceability?.isNaN() == true
    }

    // List of similar artist groups for genre-based similarity scoring.
    private val similarArtistGroups = listOf(
        listOf("Drake", "Lil Wayne", "Future", "Kanye West", "21 Savage", "Travis Scott", "Young Thug", "Gunna", "DaBaby", "Pop Smoke", "ASAP Rocky", "Meek Mill", "Lil Baby", "Lil Durk", "Tyga","2 Chains"),
        listOf("Kendrick Lamar", "J. Cole", "Big Sean", "Joey BadaSS", "Logic", "Mac Miller", "Wale", "Denzel Curry", "NF", "Cordae", "Mick Jenkins", "IDK", "Isaiah Rashad", "Russ", "Bas"),
        listOf("Cardi B", "Nicki Minaj", "Megan Thee Stallion", "Doja Cat", "Latto", "Iggy Azalea", "Saweetie", "Remy Ma", "City Girls", "Coi Leray", "BIA", "Rico Nasty", "Chika", "Kash Doll", "CupcakKe"),
        listOf("Taylor Swift", "Selena Gomez", "Demi Lovato", "Olivia Rodrigo", "Katy Perry", "Billie Eilish", "Ava Max", "Sabrina Carpenter", "Tate McRae", "Charli XCX", "Hailee Steinfeld", "Halsey", "Bea Miller", "Bebe Rexha", "Anne-Marie"),
        listOf("Ariana Grande", "Dua Lipa", "Camila Cabello", "Rita Ora", "Zara Larsson", "Tinashe", "Alessia Cara", "Tove Lo", "Madison Beer", "Ellie Goulding", "Jessie J", "Sia", "Lady Gaga", "Lorde", "Britney Spears"),
        listOf("Justin Bieber", "Shawn Mendes", "Charlie Puth", "Troye Sivan", "Lauv", "Conan Gray", "Niall Horan", "ZAYN", "Jonas Brothers", "Ed Sheeran", "James Arthur", "Dean Lewis", "Lewis Capaldi", "Jason Derulo", "AJ Mitchell"),
        listOf("Metallica", "Slayer", "Megadeth", "Anthrax", "Pantera", "Iron Maiden", "Judas Priest", "Lamb of God", "Slipknot", "Korn", "Disturbed", "System of a Down", "Tool", "Avenged Sevenfold", "Ghost"),
        listOf("Nirvana", "Pearl Jam", "Soundgarden", "Alice in Chains", "Stone Temple Pilots", "Smashing Pumpkins", "Bush", "Temple of the Dog", "Silverchair", "Radiohead", "The Offspring", "Green Day", "Blink-182", "My Chemical Romance", "Fall Out Boy"),
        listOf("Imagine Dragons", "OneRepublic", "Coldplay", "Bastille", "X Ambassadors", "The Script", "Walk the Moon", "American Authors", "Foster the People", "AWOLNATION", "Arctic Monkeys", "The Killers", "Muse", "Thirty Seconds to Mars", "Kings of Leon"),
        listOf("The Weeknd", "Frank Ocean", "Miguel", "Chris Brown", "Trey Songz", "Bryson Tiller", "Giveon", "6LACK", "Khalid", "Daniel Caesar", "Tory Lanez", "PARTYNEXTDOOR", "Brent Faiyaz", "Ty Dolla Sign", "Eric Bellinger"),
        listOf("Bruno Mars", "Anderson .Paak", "Ne-Yo", "John Legend", "Usher", "Tank", "Robin Thicke", "Mario", "Ginuwine", "Maxwell", "Babyface", "Charlie Wilson", "Raheem DeVaughn", "Joe", "Lloyd"),
        listOf("Burna Boy", "Wizkid", "Davido", "Rema", "Tems", "Omah Lay", "Ayra Starr", "Fireboy DML", "Joeboy", "Tiwa Savage", "Yemi Alade", "Mr Eazi", "CKay", "Patoranking", "Tekno"),
        listOf("Bad Bunny", "J Balvin", "Ozuna", "Anuel AA", "Maluma", "Karol G", "Nicky Jam", "Daddy Yankee", "Farruko", "Becky G", "Myke Towers", "Sech", "Rauw Alejandro", "Manuel Turizo", "Feid"),
        listOf("Calvin Harris", "David Guetta", "Zedd", "Martin Garrix", "Kygo", "Avicii", "Alesso", "Steve Aoki", "Marshmello", "The Chainsmokers", "Alan Walker", "Tiesto", "Dillon Francis", "Illenium", "Don Diablo"),
        listOf("Luke Bryan", "Blake Shelton", "Jason Aldean", "Thomas Rhett", "Morgan Wallen", "Kane Brown", "Dierks Bentley", "Chris Stapleton", "Zac Brown Band", "Florida Georgia Line", "Tim McGraw", "Keith Urban", "Eric Church", "Sam Hunt", "Jake Owen"),
        listOf("Kabza De Small", "DJ Maphorisa", "Young Stunna", "Daliwonga", "Focalistic", "Sha Sha", "Mr JazziQ", "DBN Gogo", "Busta 929", "Mellow & Sleazy", "Uncle Waffles", "Boohle", "Zuma", "Reece Madlisa", "Tyler ICU", "Scotts Maphuma", "CowBoii", "Aymos"),
        listOf("Nasty C", "AKA", "Cassper Nyovest", "A-Reece", "Blxckie", "Emtee", "Kwesta", "Shane Eagle", "Big Zulu", "K.O", "Maglera Doe Boy", "Boity", "Nadia Nakai", "Priddy Ugly", "Reason"),
        listOf("Mandoza", "Arthur Mafokate", "Trompies", "Zola", "Chicco Twala", "Brickz", "Mzekezeke", "Mapaputsi", "Professor", "Spikiri", "DJ Cleo", "Oskido", "Big Nuz", "Thebe", "Boom Shaka"),
        listOf("Babes Wodumo", "Distuction Boyz", "DJ Tira", "Mampintsha", "RudeBoyz", "Dlala Thukzin", "Busiswa", "Tipcee", "Heavy-K", "Moonchild Sanelly", "Patoranking", "Zodwa Wabantu", "Goldmax", "Que", "Mr Thela"),
        listOf("Black Coffee", "Culoe De Song", "DJ Zinhle", "Heavy-K", "Prince Kaybee", "Sun-El Musician", "Master KG", "Samthing Soweto", "Msaki", "Da Capo", "DJ Kent", "DJ Sbu", "Lady Zamar", "Holly Rey", "DJ Merlon", "Nomcebo"),
        listOf("Shekhinah", "Elaine", "Ami Faku", "Simmy", "Lloyiso", "Manana", "Brenda Fassie", "Zonke", "Judith Sephuma", "Sjava", "Berita", "Nathi", "Amanda Black", "Azana", "Ntando"),
        listOf("Mi Casa", "Tresor", "Mafikizolo", "Jeremy Loops", "GoodLuck", "Danny K", "Majozi", "Locnville", "Matthew Mole", "Mellisa Allison", "Dr Victor", "Lira", "TKZee", "Bongo Maffin", "Micasa"),
        listOf("Joyous Celebration", "Rebecca Malope", "Winnie Mashaba", "Dr Tumi", "Sfiso Ncwane", "Solly Mahlangu", "Dumi Mkokstad", "Ntokozo Mbambo", "Benjamin Dube", "Lebo Sekgobela", "Sipho Makhabane", "Zaza", "Lundi Tyamara", "Kholeka", "Sechaba"),
        listOf("Lucky Dube", "Johnny Clegg", "Miriam Makeba", "Yvonne Chaka Chaka", "Brenda Fassie", "Busi Mhlongo", "Soweto Gospel Choir", "Thandiswa Mazwai", "Simphiwe Dana", "Oliver Mtukudzi", "Ringo Madlingozi", "Caiphus Semenya", "Letta Mbulu", "Judith Sephuma", "Sipho Hotstix Mabuse")
    )

    /**
     * Returns a similarity score if both songs share artists from the same group.
     */
    private fun getGenreBasedArtistSimilarity(a: SongMetaData, b: SongMetaData): Int {
        for (group in similarArtistGroups) {
            val groupSet = group.toSet()
            val aMatch = a.artists.any { it in groupSet }
            val bMatch = b.artists.any { it in groupSet }
            if (aMatch && bMatch) {
                return 20 // Adjust this score based on importance
            }
        }
        return 0
    }

    // TODO: Profile performance, add unit/integration tests, modularize further as needed.
}