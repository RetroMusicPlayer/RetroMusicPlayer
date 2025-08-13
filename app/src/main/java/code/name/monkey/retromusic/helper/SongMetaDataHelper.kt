package code.name.monkey.retromusic.helper

import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.text.InputType
import android.widget.EditText
import androidx.core.app.NotificationCompat
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import code.name.monkey.retromusic.R // Assuming you have a general notification icon
import code.name.monkey.retromusic.model.SongMetaData
import code.name.monkey.retromusic.model.SongTMPContainer
import code.name.monkey.retromusic.network.InternetConnection
import code.name.monkey.retromusic.repository.RealSongRepository
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.withContext // Add this import
import kotlinx.coroutines.CoroutineScope // Will add this
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch // Will add this


const val inputPath = "inputile.txt"
const val outputPath = "outputile.txt"

private const val ENHANCEMENT_CHANNEL_ID = "song_enhancement_channel"
public const val ENHANCEMENT_NOTIFICATION_ID = 1001

fun initialiseMetaDataProcess(context: Context) {
    val songRepository = RealSongRepository(context)
    val deviceSongs = songRepository.songs().map {
        SongTMPContainer(
            title = it.title,
            artistName = it.artistName
                .split(',', '/')
                .map { name -> name.trim() }
                .filter { name -> name.isNotEmpty() },
            data = it.data,
            year = it.year,
            liked = false,
            favorite = false,
            rating = 0
        )
    }
    CoroutineScope(Dispatchers.IO).launch {
        enhanceSongsData(inputPath, outputPath, deviceSongs, context)
    }
}

// Helper: build the song key (title + artists)
fun songKey(song: JsonObject): Pair<String, List<String>> {
    val title = song.get("title")?.asString?.lowercase() ?: ""
    val artists = song.getAsJsonArray("artists")?.mapNotNull { it.asString } ?: emptyList()
    return title to artists
}

// Helper: extract key from SongTMPContainer object
fun songKeyFromSong(song: SongTMPContainer): Pair<String, List<String>> {
    val title = song.title.lowercase()
    val artists = song.artistName ?: emptyList()
    return title to artists
}

// Generate AI-enhanced data using Gemini API
fun generateTextWithGemini(
    prompt: String,
    apiKey: String,
    modelName: String
): String {
    val message = JsonObject().apply {
        addProperty("role", "user")
        val partsArray = JsonArray().apply {
            add(JsonObject().apply {
                addProperty("text",
"""You are an AI Music Tag Enhancer.

You will be provided with a JSON object that contains metadata about a song.

Your tasks are strictly as follows:
    Improve and expand only the mood and genre arrays.
    Add appropriate values that enhance the description of the song.
    You must select values strictly from the provided approved lists below.
    Do not use any mood or genre not present in the approved lists.
    Add a danceability field (if it does not exist) with a numeric value between 0.0 and 1.0.
    0.0 = Not danceable
    1.0 = Very danceable
    Add or enhance the market field.
    This should be an array of region codes based on relevance to the song’s style or audience.
    Valid values include, but are not limited to: "US" (United States), "UK" (United Kingdom), "SA" (South Africa), etc.
    Add a tempo field (in beats per minute).
    This should be a numeric value (Integer or Double).
    Example: 120.0 represents 120 beats per minute.
    Add an energy field with a value between 0.0 and 1.0.
    This represents the intensity and loudness of the track.
    Higher values indicate more energetic or intense songs.
    Add a valence field with a value between 0.0 and 1.0.
    This measures the musical positivity of the song.
    Higher values indicate more positive or cheerful moods.

Important constraints:
    You must not change or remove any existing fields other than mood, genre, and market.
    Do not rename, reorder, or restructure the JSON object.
    Do not rename any keys of the JSON object
    Do not add any new keys unless they are explicitly required (danceability, tempo, energy, valence, market).
    Your output must be a valid JSON object.
    Do not include explanations, comments, or formatting (such as code blocks or markdown).
    Do not wrap the output in quotation marks, backticks, or any additional text.

Approved values:
    You must use the case-sensitive values from the provided mood and genre lists only.
    Any mood or genre not in the approved list is invalid and must not be used.

Case-sensitive valid options:

**Mood (select only from this list):**
    ['Abstract', 'Adventurous', 'Affectionate', 'Aggressive', 'Amber', 'Ambient', 'Ambitious', 'Analytical', 'Angry', 'Angsty', 'Anguished', 'Anthemic', 'Anxious', 'Apologetic', 'Aspirational', 'Atmospheric', 'Authentic', 'Bass-heavy', 'Bittersweet', 'Boastful', 'Bold', 'Bossy', 'Bouncy', 'Braggy', 'Bright', 'Brooding', 'Calm', 'Calming', 'Carefree', 'Catchy', 'Celebratory', 'Ceremonial', 'Chant', 'Charismatic', 'Cheeky', 'Cheerful', 'Chill', 'Chilled', 'Chilling', 'Cinematic', 'Classic', 'Club', 'Clubby', 'Club‑ready', 'Collaborative', 'Colorful', 'Comforting', 'Competitive', 'Confidence', 'Conflict', 'Conflicted', 'Confrontational', 'Conscious', 'Cool', 'Cozy', 'Cultural', 'Dance', 'Danceable', 'Dancey', 'Dance‑floor', 'Dark', 'Deep', 'Defiant', 'Depressed', 'Detached', 'Determined', 'Devotional', 'Dramatic', 'Dreamy', 'Driven', 'Driving', 'Dynamic', 'Earnest', 'Edgy', 'Elegant', 'Empathetic', 'Empowered', 'Encouraging', 'Energizing', 'Epic', 'Escapist', 'Ethereal', 'Exciting', 'Existential', 'Exotic', 'Experimental', 'Faithful', 'Feel-Good', 'Fierce', 'Fiery', 'Flex', 'Focused', 'Free-Spirited', 'Fresh', 'Friendship', 'Frustrated', 'Fun', 'Funky', 'Funny', 'Futuristic', 'Gentle', 'Grateful', 'Groovy', 'Happy', 'Hard', 'Hard-Hitting', 'Healing', 'Heartbroken', 'Heavy', 'High Energy', 'Hopeful', 'Humorous', 'Hungry', "Hustler's anthem", 'Hyped', 'Innovative', 'Inspirational', 'Inspiring', 'Intense', 'Intimate', 'Isolation', 'Joyful', 'Late night groove', 'Late‑night', 'Legendary', 'Liberated', 'Liberating', 'Lighthearted', 'Live', 'Lively', 'Local Pride', 'Local Vibe', 'Lonely', 'Longing', 'Lounge', 'Love‑Struck', 'Loving', 'Loyalty', 'Lyrical', 'Melancholic', 'Melodic', 'Melodramatic', 'Minimalistic', 'Money-focused', 'Morning vibe', 'Motivated', 'Mysterious', 'Mystical', 'Narrative', 'Night Vibe', 'Nonchalant', 'Nostalgic', 'Party', 'Passionate', 'Patriotic', 'Peaceful', 'Pensive', 'Personal', 'Playful', 'Political', 'Positive', 'Powerful', 'Protective', 'Proud', 'Provocative', 'Pumped-up', 'Quirky', 'Raised', 'Raise‑the‑roof', 'Raw', 'Real', 'Rebellious', 'Refreshing', 'Regretful', 'Relaxed', 'Relaxing', 'Resilient', 'Respectful', 'Retro', 'Reverent', 'Rhythmic', 'Rowdy', 'Sad', 'Sarcastic', 'Sassy', 'Satirical', 'Seductive', 'Serene', 'Serious', 'Sexy', 'Sincere', 'Slow', 'Smooth', 'Soft', 'Somber', 'Sophisticated', 'South African pride', 'Southern vibe', 'Spicy', 'Spiritual', 'Storytelling', 'Strategic', 'Street', 'Street-wise', 'Street‑empower', 'Street‑vibe', 'Strong', 'Stylish', 'Sultry', 'Supportive', 'Swagger', 'Swaggy', 'Sweet', 'Tender', 'Thankful', 'Thoughtful', 'Togetherness', 'Tough', 'Traditional', 'Tragic', 'Tranquil', 'Trendy', 'Tribal', 'Tribute', 'Trippy', 'Triumphant', 'Turn up', 'Turnt', 'Underground', 'Upbeat', 'Up‑tempo', 'Urban', 'Vengeful', 'Vibe', 'Vibey', 'Vibrant', 'Victorious', 'Vulnerable', 'Warm', 'Wavy', 'Whimsical', 'Wild', 'Wistful', 'Witty', 'Worshipful', 'Yearning', 'Young', 'Youthful', 'assertive', 'braggadocious', 'confident', 'contemplative', 'emotional', 'empowering', 'energetic', 'euphoric', 'festive', 'flirty', 'gritty', 'haunting', 'heartbreak', 'heartfelt', 'hype', 'hypnotic', 'independent', 'introspective', 'ironic', 'laid-back', 'lush', 'luxurious', 'melancholy', 'mellow', 'moody', 'motivational', 'optimistic', 'reflective', 'relatable', 'romantic', 'sensual', 'sentimental', 'soothing', 'soulful', 'tense', 'thought-provoking', 'uplifting']

**Genre (select only from this list):**
    ['Acapella', 'Acoustic', 'Adult contemporary', 'African', 'Afro Fusion', 'Afro Hip-Hop', 'Afro Rap', 'Afro Tech', 'Afro pop', 'Afro-House', 'Afro-jazz', 'Afrobeat', 'Afrobeats', 'Alternative', 'Alternative Hip Hop', 'Alternative Pop', 'Alternative R&B', 'Alternative Rap', 'Alternative Rock', 'Ambient', 'Ambient Pop', 'Ambient Rock', 'Anime-inspired', 'Bacardi', 'Bacardi House', 'Ballad', 'Barcadi', 'Baroque Pop', 'Battle Rap', 'Blues', 'Blues Rock', 'Bongo Flava', 'Boom Bap', 'Britpop', 'Broken Beat', 'Chill Rap', 'Chillout', 'Choir', 'Choral', 'Christian', 'Christian Hip‑Hop', 'Christian Pop', 'Christian Rap', 'Christian Worship', 'Christmas', 'Cinematic', 'Classic', 'Classic Rock', 'Classical', 'Cloud Rap', 'Club', 'Coleader', 'Comedy Rap', 'Comedy hip hop', 'Conscious Hip-Hop', 'Conscious Rap', 'Contemporary Christian', 'Contemporary R&B', 'Country', 'Crunk', 'Cypher', 'Dance', 'Dance Rock', 'Dance-Pop', 'Dancehall', 'Deep House', 'Detroit House', 'Disco', 'Disney', 'Diss Track', 'Doowop', 'Downtempo', 'Dream Pop', 'Drum & Bass', 'Drum and Bass', 'Dubstep', 'EDM', 'East Coast Hip‑Hop', 'Electro', 'Electro House', 'Electronic', 'Electronica', 'Electropop', 'Emo', 'Emo Rap', 'Euro Pop', 'Eurodance', 'Experimental', 'Experimental Hip‑Hop', 'Folk', 'Folk House', 'Freestyle', 'French Chanson', 'French Pop', 'Funk', 'Funk Brasileiro', 'Future Bass', 'G-Funk', 'Gangsta Rap', 'Gospel', 'Gospel House', 'Gqom', 'Grime', 'Highlife', 'Indie', 'Indie Dance', 'Indie Folk', 'Indie Pop', 'Indie rock', 'Inspirational', 'Instrumental', 'Intro', 'Jam Band', 'Jazz', 'Jazz Fusion', 'Jazz House', 'Kwaito Fusion', 'Kwaito Rap', 'Kwaito-Influenced', 'Latin', 'Latin House', 'Latin Pop', 'Latin Trap', 'Live', 'Lo-fi Hip Hop', 'Lounge', 'Lo‑fi', 'Lyricism', 'Maskandi', 'Maskandi Fusion', 'Melodic Rap', 'Minimalism', 'Motswako', 'Neo Soul', 'Novelty', 'Nu Disco', 'Nu Jazz', 'Old School Hip Hop', 'Opera', 'Orchestral', 'Orchestral Pop', 'Orchestral Rap', 'Party', 'Party Rap', 'Pop Ballad', 'Pop Rock', 'Pop Soul', 'Pop-Rap', 'Progressive House', 'R&B', 'R&B Fusion', 'Rap', 'Reggae', 'Reggaeton', 'Remix', 'Retro', 'RnB', 'Rock', 'Rock and Roll', 'Romantic', 'SA Hip-Hop', 'Singer‑Songwriter', 'Slow jam', 'Smooth Jazz', 'Soft Rock', 'Sotho Rap', 'Soul', 'Soulful', 'Soulful Amapiano', 'Soulful House', 'Soulful Piano', 'Soundtrack', 'South African', 'South African Dance', 'South African Hip Hop', 'South African Music', 'South African Rap', 'South African Street', 'South African house', 'Spiritual', 'Spiritual House', 'Spoken Word', 'Street Rap', 'Swing', 'Synthpop', 'Tech House', 'Techno', 'Traditional', 'Traditional Crossover', 'Traditional Zulu', 'Trap Metal', 'Trap Soul', 'Trip‑Hop', 'Tsonga Rap', 'UK Hip‑Hop', 'Underground Rap', 'Urban', 'West Coast Hip‑Hop', 'World', 'World Music', 'Worldbeat', 'Worship', 'Zulu Rap', 'Zulu Traditional', 'afrosoul', 'afrotrap', 'amapiano', 'arena rock', 'art rock', 'drill', 'hip hop', 'house', 'kwaito', 'pop', 'post-Britpop', 'private school', 'soul-pop', 'south african pop', 'southern rap', 'trap', 'trap-pop']

Now, here is the input JSON:

""".trimIndent() + prompt)
            })
        }
        add("parts", partsArray)
    }

    val chatArray = JsonArray().apply { add(message) }
    val payload = JsonObject().apply { add("contents", chatArray) }

    val client = OkHttpClient.Builder()
        .callTimeout(120, TimeUnit.SECONDS)
        .build()


    val apiUrl = "https://generativelanguage.googleapis.com/v1beta/models/$modelName:generateContent?key=$apiKey"

    val mediaType = "application/json".toMediaTypeOrNull()
    val body = payload.toString().toRequestBody(mediaType)

    val request = Request.Builder()
        .url(apiUrl)
        .post(body)
        .build()

    return try {
        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                return "Error making API request: ${response.code} ${response.message}"
            }
            val responseBodyString = response.body.string()
            val json = JsonParser().parse(responseBodyString).asJsonObject
            val candidates = json.getAsJsonArray("candidates")
            if (candidates != null && candidates.size() > 0) {
                val content = candidates[0].asJsonObject.getAsJsonObject("content")
                val parts = content.getAsJsonArray("parts")
                if (parts != null && parts.size() > 0) {
                    return parts[0].asJsonObject.get("text").asString
                }
            }
            "Error: Unexpected response structure"
        }
    } catch (e: Exception) {
        "An unexpected error occurred: $e"
    }
}

// Convert a SongTMPContainer to SongMetaData for JSON export
fun songToSongMetaData(song: SongTMPContainer): SongMetaData {
    return SongMetaData(
        title = song.title,
        artists = song.artistName ?: emptyList(),
        file = song.data,
        mood = emptyList(),
        genre = emptyList(),
        playlist = emptyList(),
        year = song.year?.toString() ?: "",
        liked = song.liked ?: false,
        favorite = song.favorite ?: false,
        rating = song.rating ?: 0,
        danceability = null, // corrected spelling
        tempo = null,        // in BPM (e.g., 120.0)
        energy = null,       // 0.0 - 1.0 (intensity/loudness)
        valence = null,
        market = null,
        skips = 0
    )
}

// Merge new device songs into file if not already present
fun scanAndAddNewDeviceSongs(context: Context, songsDataPath: String, deviceSongs: List<SongTMPContainer>) {
    val gson = Gson()
    val existingSongs: List<JsonObject> = try {
        val arr = JsonParser().parse(readFileOrCreate(context, songsDataPath, "[]")).asJsonArray
        arr.map { it.asJsonObject }
    } catch (e: Exception) {
        emptyList()
    }

    val existingKeys = existingSongs.map { songKey(it) }.toSet()

    val newDeviceSongs = deviceSongs
        .filter { songKeyFromSong(it) !in existingKeys }
        .map { songToSongMetaData(it) }

    if (newDeviceSongs.isNotEmpty()) {
        val updatedSongs = existingSongs.toMutableList()
        newDeviceSongs.forEach { updatedSongs.add(gson.toJsonTree(it).asJsonObject) }
        val updatedSongsTxt = gson.toJson(updatedSongs)
        writeToInternalStorage(context, songsDataPath, updatedSongsTxt)
    }
}

// Secure API key storage/retrieval
fun saveApiKeys(context: Context, apiKeys: List<String>) {
    val masterKey = MasterKey.Builder(context, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        "secure_api_keys",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )
    sharedPreferences.edit().putString("api_keys", apiKeys.joinToString(",")).apply()
}

fun getApiKeys(context: Context): List<String> {
    val masterKey = MasterKey.Builder(context, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        "secure_api_keys",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )
    val keys = sharedPreferences.getString("api_keys", "") ?: ""
    return keys.split(",").map { it.trim() }.filter { it.isNotEmpty() }
}

fun addApiKey(context: Context): Boolean {
    var result = false
    val editText = EditText(context)
    editText.inputType = InputType.TYPE_CLASS_TEXT
    editText.hint = "Enter a Google Gemini API Key"

    val dialog = AlertDialog.Builder(context)
        .setTitle("Enter a Google Gemini API Key")
        .setMessage("Please enter your Google API key, you can add multiple keys separated by commas (,):")
        .setView(editText)
        .setCancelable(false)
        .setPositiveButton("Save") { _, _ ->
            val input = editText.text.toString()
            val keys = input.split(",").map { it.trim() }.filter { it.isNotEmpty() }
            if (keys.isNotEmpty()) {
                saveApiKeys(context, keys)
                val storedKeys = getApiKeys(context)
                result = storedKeys.isNotEmpty()
            }
        }
        .setNegativeButton("Cancel") { dialogInterface, _ ->
            dialogInterface.dismiss()
            result = false
        }
        .create()

    dialog.show()
    return result
}

private fun createNotificationChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = "Song Enhancement Progress"
        val descriptionText = "Shows the progress of song metadata enhancement"
        val importance = NotificationManager.IMPORTANCE_LOW
        val channel = NotificationChannel(ENHANCEMENT_CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}

suspend fun enhanceSongsData(inputPath: String, outputPath: String, deviceSongs: List<SongTMPContainer>, context: Context) {
    val gson = Gson()
    createNotificationChannel(context)
    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    val notificationBuilder = NotificationCompat.Builder(context, ENHANCEMENT_CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_notification)
        .setContentTitle("Song Metadata Enhancement")
        .setContentText("Starting enhancement process...")
        .setPriority(NotificationCompat.PRIORITY_LOW)
        .setOngoing(true)
        .setProgress(0, 0, true)

    notificationManager.notify(ENHANCEMENT_NOTIFICATION_ID, notificationBuilder.build())

    scanAndAddNewDeviceSongs(context, inputPath, deviceSongs)
    fixMissingSongMetaFields(context, outputPath)

    withContext(Dispatchers.IO) {
        InternetConnection.waitForConnection(context, notificationBuilder, notificationManager)
    }

    val songs = JsonParser().parse(readFileOrCreate(context, inputPath, "[]")).asJsonArray
    val enhancedSongs: MutableList<JsonObject> = try {
        val arr = JsonParser().parse(readFileOrCreate(context, outputPath, "[]")).asJsonArray
        arr.map { it.asJsonObject }.toMutableList()
    } catch (e: Exception) {
        mutableListOf()
    }

    val processedPaths = enhancedSongs.mapNotNull { it.get("file")?.asString }.toSet()
    val newEntries = songs.map { it.asJsonObject }
        .filter { it.get("file")?.asString !in processedPaths }

    if (newEntries.isNotEmpty()) {
        val myApiKeys = getApiKeys(context)
        if (myApiKeys.isEmpty()) {
            notificationBuilder
                .setContentText("Enhancement stopped: No API keys found.")
                .setProgress(0, 0, false)
                .setOngoing(false)
            notificationManager.notify(ENHANCEMENT_NOTIFICATION_ID, notificationBuilder.build())
            return
        }

        val modelNames = mutableListOf(
            "gemini-2.0-flash", "gemini-2.0-flash-lite", "gemini-2.5-flash", "gemini-2.5-pro", "gemini-2.5-flash-lite"
        )
        var apiKeyIndex = 0
        var modelIndex = 0
        var songsProcessedCount = 0
        val totalSongsToProcess = newEntries.size

        notificationBuilder
            .setContentText("Processing 0 of $totalSongsToProcess songs.")
            .setProgress(totalSongsToProcess, 0, false)
        notificationManager.notify(ENHANCEMENT_NOTIFICATION_ID, notificationBuilder.build())

        for (song in newEntries) {
            var processedThisSong = false
            while (apiKeyIndex < myApiKeys.size && !processedThisSong) {
                val apiKey = myApiKeys[apiKeyIndex]
                while (modelIndex < modelNames.size && !processedThisSong) {
                    val modelName = modelNames[modelIndex]
                    val prompt = song.toString()
                    val result = generateTextWithGemini(prompt, apiKey, modelName)
                        .replace("```json", "")
                        .replace("```", "")
                        .replace("\n", "")
                        .replace("\"artist\"", "artists")
                        .replace("\"moods\"", "mood")
                        .replace("\"genres\"", "genre")
                        .replace("\"markets\"", "market")
                        .trim()
                    try {
                        val enhancedSong = JsonParser().parse(result).asJsonObject
                        enhancedSongs.add(enhancedSong)
                        writeToInternalStorage(context, outputPath, gson.toJson(enhancedSongs))
                        SongDataManager.loadDefaultSongsJson(context)
                        processedThisSong = true
                        songsProcessedCount++
                        notificationBuilder
                            .setContentText("Processing $songsProcessedCount of $totalSongsToProcess songs.")
                            .setProgress(totalSongsToProcess, songsProcessedCount, false)
                        notificationManager.notify(ENHANCEMENT_NOTIFICATION_ID, notificationBuilder.build())
                        Thread.sleep(3000)
                    } catch (e: Exception) {
                        if (!InternetConnection.hasInternetConnection(context)){
                            processedThisSong == true
                        }else{
                            modelIndex++
                        }
                    }
                }
                if (!processedThisSong) {
                    if (!InternetConnection.hasInternetConnection(context)){
                        processedThisSong == true
                    }else{
                        apiKeyIndex++
                        modelIndex = 0
                    }
                }
            }
            if (!processedThisSong) {
                if (!InternetConnection.hasInternetConnection(context)) {
                    withContext(Dispatchers.IO) {
                        InternetConnection.waitForConnection(context, notificationBuilder, notificationManager)
                    }
                }else{
                    notificationBuilder
                        .setContentText("Enhancement partially complete. API/Model issues after $songsProcessedCount songs.")
                        .setProgress(totalSongsToProcess, songsProcessedCount, false)
                        .setOngoing(false)
                    notificationManager.notify(ENHANCEMENT_NOTIFICATION_ID, notificationBuilder.build())
                    return
                }
            }
        }
        notificationBuilder
            .setContentText("Enhancement complete. $songsProcessedCount songs processed.")
            .setProgress(totalSongsToProcess, songsProcessedCount, false)
            .setOngoing(false)
        notificationManager.notify(ENHANCEMENT_NOTIFICATION_ID, notificationBuilder.build())
    } else {
        notificationBuilder
            .setContentText("No new songs to enhance.")
            .setProgress(0, 0, false)
            .setOngoing(false)
        notificationManager.notify(ENHANCEMENT_NOTIFICATION_ID, notificationBuilder.build())
    }
}

fun writeToInternalStorage(context: Context, filename: String, content: String) {
    try {
        val file = File(context.filesDir, filename)
        FileOutputStream(file).use {
            it.write(content.toByteArray())
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun readFileOrCreate(context: Context, filename: String, defaultContent: String = ""): String? {
    val file = File(context.filesDir, filename)
    return try {
        if (file.exists()) {
            file.readText()
        } else {
            file.writeText(defaultContent)
            defaultContent
        }
    } catch (e: IOException) {
        e.printStackTrace()
        "[]"
    }
}

fun fixMissingSongMetaFields(context: Context, outputPath: String) {
    val gson = Gson()
    val fileContent = readFileOrCreate(context, outputPath, "[]") ?: "[]"
    if (fileContent !== "[]") {
    try {
        val arr = JsonParser().parse(fileContent).asJsonArray
        val filteredArr = arr.filter { element ->
            if (!element.isJsonObject) return@filter false
            val obj = element.asJsonObject

            fun isPresentAndNotNullPrimitive(fieldName: String): Boolean {
                return obj.has(fieldName) && obj.get(fieldName).isJsonPrimitive && !obj.get(
                    fieldName
                ).isJsonNull
            }

            fun isPresentAndNotNullArray(fieldName: String): Boolean {
                return obj.has(fieldName) && obj.get(fieldName).isJsonArray
            }

            fun isPresentAndNonEmptyString(fieldName: String): Boolean {
                if (!obj.has(fieldName)) return false
                val jsonElement = obj.get(fieldName)
                return jsonElement.isJsonPrimitive && jsonElement.asJsonPrimitive.isString && jsonElement.asString.isNotEmpty()
            }

            if (!isPresentAndNonEmptyString("file")) return@filter false
            if (!isPresentAndNonEmptyString("title")) return@filter false
            if (!isPresentAndNotNullArray("artists")) return@filter false
            if (!isPresentAndNotNullArray("genre")) return@filter false
            if (!isPresentAndNotNullArray("mood")) return@filter false
            if (!isPresentAndNotNullArray("market")) return@filter false
            if (!isPresentAndNotNullPrimitive("danceability")) return@filter false
            if (!isPresentAndNotNullPrimitive("tempo")) return@filter false
            if (!isPresentAndNotNullPrimitive("energy")) return@filter false
            if (!isPresentAndNotNullPrimitive("valence")) return@filter false
            true
        }

        if (filteredArr.size != arr.size()) {
            writeToInternalStorage(context, outputPath, gson.toJson(filteredArr))
        }
    } catch (e: Exception) {
        writeToInternalStorage(context, outputPath, "[]")
    }
    }
}
