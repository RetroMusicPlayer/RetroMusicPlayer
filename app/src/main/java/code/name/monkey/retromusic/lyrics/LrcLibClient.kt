package code.name.monkey.retromusic.lyrics

import code.name.monkey.retromusic.model.Song
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import kotlin.math.abs

/** Looks lyrics up on https://lrclib.net (free, no key). Blocking: call it off the main thread. */
object LrcLibClient {
    private const val BASE = "https://lrclib.net/api"
    private const val USER_AGENT = "R Music (https://github.com/ELRFAAY/RetroMusicPlayer)"

    class Found(val synced: String?, val plain: String?, val instrumental: Boolean)

    /** Returns null when nothing was found; throws [IOException] when the service can't be reached. */
    @Throws(IOException::class)
    fun fetch(song: Song): Found? {
        val seconds = (song.duration / 1000).toInt()
        val exact = request(
            "$BASE/get?track_name=${enc(song.title)}&artist_name=${enc(song.artistName)}" +
                    "&album_name=${enc(song.albumName)}&duration=$seconds"
        )
        if (exact != null) {
            toFound(JSONObject(exact))?.let { return it }
        }
        val list = request(
            "$BASE/search?track_name=${enc(song.title)}&artist_name=${enc(song.artistName)}"
        ) ?: return null
        val array = JSONArray(list)
        val items = (0 until array.length()).map { array.getJSONObject(it) }
        val closeInLength = items.filter { abs(it.optDouble("duration") - seconds) <= 3 }
        val ordered = closeInLength + (items - closeInLength.toSet())
        // Prefer a result that has timestamps.
        val best = ordered.firstOrNull { !it.isNull("syncedLyrics") && it.optString("syncedLyrics").isNotBlank() }
            ?: ordered.firstOrNull { !it.isNull("plainLyrics") && it.optString("plainLyrics").isNotBlank() }
        return best?.let { toFound(it) }
    }

    private fun toFound(o: JSONObject): Found? {
        val synced = o.optString("syncedLyrics").takeIf { !o.isNull("syncedLyrics") && it.isNotBlank() }
        val plain = o.optString("plainLyrics").takeIf { !o.isNull("plainLyrics") && it.isNotBlank() }
        val instrumental = o.optBoolean("instrumental", false)
        return if (synced == null && plain == null && !instrumental) null
        else Found(synced, plain, instrumental)
    }

    /** Response body, or null for HTTP 404 (nothing found). */
    private fun request(url: String): String? {
        val connection = URL(url).openConnection() as HttpURLConnection
        try {
            connection.connectTimeout = 10_000
            connection.readTimeout = 15_000
            connection.setRequestProperty("User-Agent", USER_AGENT)
            val code = connection.responseCode
            if (code == HttpURLConnection.HTTP_NOT_FOUND) return null
            if (code != HttpURLConnection.HTTP_OK) throw IOException("HTTP $code")
            return connection.inputStream.bufferedReader().use { it.readText() }
        } finally {
            connection.disconnect()
        }
    }

    private fun enc(value: String) = URLEncoder.encode(value, "UTF-8")
}
