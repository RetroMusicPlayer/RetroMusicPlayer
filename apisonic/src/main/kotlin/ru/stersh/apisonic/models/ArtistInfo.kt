package ru.stersh.apisonic.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ArtistInfo2Response(@Json(name = "artistInfo2") val artistInfo2: ArtistInfo) : SubsonicResponse

@JsonClass(generateAdapter = true)
data class ArtistInfoResponse(@Json(name = "artistInfo") val artistInfo: ArtistInfo) : SubsonicResponse

@JsonClass(generateAdapter = true)
data class ArtistInfo(
    @Json(name = "biography") val biography: String,
    @Json(name = "largeImageUrl") val largeImageUrl: String,
    @Json(name = "lastFmUrl") val lastFmUrl: String,
    @Json(name = "musicBrainzId") val musicBrainzId: String?,
    @Json(name = "mediumImageUrl") val mediumImageUrl: String,
    @Json(name = "similarArtist") val similarArtist: List<SimilarArtist>?,
    @Json(name = "smallImageUrl") val smallImageUrl: String
) {

    @JsonClass(generateAdapter = true)
    data class SimilarArtist(
        @Json(name = "albumCount") val albumCount: Int,
        @Json(name = "artistImageUrl") val artistImageUrl: String?,
        @Json(name = "coverArt") val coverArt: String,
        @Json(name = "id") val id: String,
        @Json(name = "name") val name: String
    )
}
