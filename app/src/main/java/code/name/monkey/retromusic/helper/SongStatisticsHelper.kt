package code.name.monkey.retromusic.helper

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import code.name.monkey.retromusic.model.SongStatistics
import code.name.monkey.retromusic.model.DataStatistics

object SongStatisticsManager {

    private const val FILE_NAME = "song_statistics.json"
    private var statisticsMap: MutableMap<String, SongStatistics> = mutableMapOf()

    private lateinit var appContext: Context

    fun load(context: Context) {
        appContext = context.applicationContext
        val file = getFile()
        if (file.exists()) {
            val json = file.readText()
            val type = object : TypeToken<MutableMap<String, SongStatistics>>() {}.type
            statisticsMap = Gson().fromJson(json, type) ?: mutableMapOf()
        }
    }

    fun save() {
        val file = getFile()
        val json = Gson().toJson(statisticsMap)
        file.writeText(json)
    }

    fun getSongStatistics(songId: String): SongStatistics {
        return statisticsMap.getOrPut(songId) { SongStatistics(songId) }
    }

    fun updateStatistics(stat: SongStatistics) {
        statisticsMap[stat.songId] = stat
        save()
    }

    fun getStatistics(condition: String = "song"): DataStatistics {
        if (condition == "song") {
            val map = statisticsMap.mapValues { (_, stat) ->
                mapOf(
                    "playCount" to stat.playCount,
                    "skipCount" to stat.skipCount,
                    "lastPlayed" to stat.lastPlayed,
                    "rating" to stat.rating
                )
            }
            return DataStatistics(songStats = map)
        }

//        if (condition == "alwaysAfter" && songId.isNotBlank()) {
//            val recentPlays = TopPlayedRepository.recentlyPlayedTracks()
//            val result = mutableMapOf<String, Int>()
//
//            recentPlays.forEachIndexed { i: String, song: ->
//                if (song.id.toString() == songId && i < recentPlays.size - 1) {
//                    val nextSong = recentPlays[i + 1]
//                    result[nextSong.id.toString()] = result.getOrDefault(nextSong.id.toString(), 0) + 1
//                }
//            }
//
//            val sorted = result.toList().sortedByDescending { it.second }
//            return DataStatistics(sequences = sorted)
//        }
//
//        if (condition == "alwaysBefore" && songId.isNotBlank()) {
//            val recentPlays = TopPlayedRepository.recentlyPlayedTracks()
//            val result = mutableMapOf<String, Int>()
//
//            recentPlays.forEachIndexed { i, song ->
//                if (song.id.toString() == songId && i > 0) {
//                    val prevSong = recentPlays[i - 1]
//                    result[prevSong.id.toString()] = result.getOrDefault(prevSong.id.toString(), 0) + 1
//                }
//            }
//
//            val sorted = result.toList().sortedByDescending { it.second }
//            return DataStatistics(sequences = sorted)
//        }

//        if (condition == "song2") {
//            val recentPlays = TopPlayedRepository.recentlyPlayedTracks()
//            val genreCount = mutableMapOf<String, Int>()
//
//            for (song in recentPlays) {
//                val genre = getGenreForSong(song) ?: "Unknown"
//                genreCount[genre] = genreCount.getOrDefault(genre, 0) + 1
//            }
//
//            val sorted = genreCount.toList().sortedByDescending { it.second }
//            return DataStatistics(genres = sorted)
//        }
//
//        if (condition == "artist") {
//            val recentPlays = TopPlayedRepository.recentlyPlayedTracks()
//            val artistCount = mutableMapOf<String, Int>()
//
//            for (song in recentPlays) {
//                val artist = song.artistName ?: "Unknown"
//                artistCount[artist] = artistCount.getOrDefault(artist, 0) + 1
//            }
//
//            val sorted = artistCount.toList().sortedByDescending { it.second }
//            return DataStatistics(artists = sorted)
//        }
//
       return DataStatistics()
    }

    private fun getFile(): File {
        check(::appContext.isInitialized) {
            "SongStatisticsManager not initialized. Call load(context) first."
        }
        return File(appContext.filesDir, FILE_NAME)
    }

    // 🔧 Placeholder — You need to implement this function
    private fun getGenreForSong(): String? {
        // Example: Fetch from song metadata, tags, or external map
        return null
    }
}
