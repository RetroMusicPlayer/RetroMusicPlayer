package ru.stersh.apisonic.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NowPlayingResponse(
    @Json(name = "nowPlaying") val nowPlaying: NowPlaying
) : SubsonicResponse

@JsonClass(generateAdapter = true)
data class NowPlaying(@Json(name = "entry") val entries: List<NowPlayingEntry>)

@JsonClass(generateAdapter = true)
data class NowPlayingEntry(
    @Json(name = "album") val album: String,
    @Json(name = "albumId") val albumId: String,
    @Json(name = "artist") val artist: String,
    @Json(name = "artistId") val artistId: String,
    @Json(name = "averageRating") val averageRating: Double,
    @Json(name = "bitRate") val bitRate: Int,
    @Json(name = "contentType") val contentType: String,
    @Json(name = "coverArt") val coverArt: String,
    @Json(name = "created") val created: String,
    @Json(name = "duration") val duration: Int,
    @Json(name = "genre") val genre: String,
    @Json(name = "id") val id: String,
    @Json(name = "isDir") val isDir: Boolean,
    @Json(name = "minutesAgo") val minutesAgo: Int,
    @Json(name = "parent") val parent: String,
    @Json(name = "path") val path: String,
    @Json(name = "playCount") val playCount: Int,
    @Json(name = "playerId") val playerId: Int,
    @Json(name = "size") val size: Int,
    @Json(name = "suffix") val suffix: String,
    @Json(name = "title") val title: String,
    @Json(name = "track") val track: Int,
    @Json(name = "type") val type: String,
    @Json(name = "userRating") val userRating: Int,
    @Json(name = "username") val username: String,
    @Json(name = "year") val year: Int
)