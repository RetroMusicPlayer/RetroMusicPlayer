package ru.stersh.apisonic.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ArtistResponse(@Json(name = "artist") val artist: Artist) : SubsonicResponse

@JsonClass(generateAdapter = true)
data class Artist(
    @Json(name = "id") val id: String,
    @Json(name = "name") val name: String,
    @Json(name = "coverArt") val coverArt: String,
    @Json(name = "albumCount") val albumCount: Int,
    @Json(name = "artistImageUrl") val artistImageUrl: String?,
    @Json(name = "album") val albums: List<Album>?
)

