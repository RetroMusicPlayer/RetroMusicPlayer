package ru.stersh.apisonic.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AlbumResponse(@Json(name = "album") val album: Album) : SubsonicResponse

@JsonClass(generateAdapter = true)
data class Album(
    @Json(name = "artist") val artist: String,
    @Json(name = "artistId") val artistId: String,
    @Json(name = "coverArt") val coverArt: String,
    @Json(name = "created") val created: String,
    @Json(name = "duration") val duration: Int,
    @Json(name = "genre") val genre: String,
    @Json(name = "id") val id: String,
    @Json(name = "name") val name: String,
    @Json(name = "playCount") val playCount: Int?,
    @Json(name = "song") val song: List<Song>?,
    @Json(name = "songCount") val songCount: Int,
    @Json(name = "year") val year: Int
)
