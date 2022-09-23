package ru.stersh.apisonic.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class StarredResponse(
    @Json(name = "starred") val starred: Starred
) : SubsonicResponse

@JsonClass(generateAdapter = true)
data class Starred(
    // TODO Emptylist instead of nullable possible?
    @Json(name = "album") val albums: List<Album>?,
    @Json(name = "artist") val artists: List<Artist>?,
    @Json(name = "song") val songs: List<Song>?
) {

    @JsonClass(generateAdapter = true)
    data class Album(
        @Json(name = "album") val album: String,
        @Json(name = "artist") val artist: String,
        @Json(name = "averageRating") val averageRating: Double,
        @Json(name = "coverArt") val coverArt: String,
        @Json(name = "created") val created: String,
        @Json(name = "genre") val genre: String,
        @Json(name = "id") val id: String,
        @Json(name = "isDir") val isDir: Boolean,
        @Json(name = "parent") val parent: String,
        @Json(name = "playCount") val playCount: Int,
        @Json(name = "starred") val starred: String,
        @Json(name = "title") val title: String,
        @Json(name = "year") val year: Int
    )

    @JsonClass(generateAdapter = true)
    data class Artist(
        @Json(name = "artistImageUrl") val artistImageUrl: String?,
        @Json(name = "id") val id: String,
        @Json(name = "name") val name: String,
        @Json(name = "starred") val starred: String
    )
}


@JsonClass(generateAdapter = true)
data class Starred2Response(
    @Json(name = "starred2") val starred2: Starred2
) : SubsonicResponse

@JsonClass(generateAdapter = true)
data class Starred2(
    @Json(name = "album") val album: List<Album>?,
    @Json(name = "artist") val artist: List<Artist>?,
    @Json(name = "song") val song: List<Song>?
) {

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
        @Json(name = "playCount") val playCount: Int,
        @Json(name = "songCount") val songCount: Int,
        @Json(name = "starred") val starred: String,
        @Json(name = "year") val year: Int
    )

    @JsonClass(generateAdapter = true)
    data class Artist(
        @Json(name = "coverArt") val coverArt: String,
        @Json(name = "albumCount") val albumCount: Int,
        @Json(name = "id") val id: String,
        @Json(name = "name") val name: String,
        @Json(name = "starred") val starred: String
    )
}
