package ru.stersh.apisonic.models
import com.squareup.moshi.JsonClass

import com.squareup.moshi.Json


@JsonClass(generateAdapter = true)
data class GenreResponse(@Json(name = "genres") val genres: Genres) : SubsonicResponse

@JsonClass(generateAdapter = true)
data class Genres(
    @Json(name = "genre") val genres: List<Genre>
)

@JsonClass(generateAdapter = true)
data class Genre(
    @Json(name = "albumCount") val albumCount: Int,
    @Json(name = "songCount") val songCount: Int,
    @Json(name = "value") val value: String
)