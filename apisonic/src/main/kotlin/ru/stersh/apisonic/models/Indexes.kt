package ru.stersh.apisonic.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class IndexesResponse(@Json(name = "indexes") val indexes: Indexes) : SubsonicResponse

@JsonClass(generateAdapter = true)
data class Indexes(
    @Json(name = "ignoredArticles") val ignoredArticles: String,
    @Json(name = "index") val indices: List<Index>,
    @Json(name = "lastModified") val lastModified: Long
) {

    @JsonClass(generateAdapter = true)
    data class Index(
        @Json(name = "artist") val artist: List<Artist>,
        @Json(name = "name") val name: String
    )

    @JsonClass(generateAdapter = true)
    data class Artist(
        @Json(name = "artistImageUrl") val artistImageUrl: String?,
        @Json(name = "id") val id: String,
        @Json(name = "name") val name: String
    )
}