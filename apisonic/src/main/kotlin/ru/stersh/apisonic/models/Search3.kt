package ru.stersh.apisonic.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class Search3Response(@Json(name = "searchResult3") val searchResult3: SearchResult3) : SubsonicResponse

@JsonClass(generateAdapter = true)
data class SearchResult3(
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
        @Json(name = "year") val year: Int
    )

    @JsonClass(generateAdapter = true)
    data class Artist(
        @Json(name = "albumCount") val albumCount: Int,
        @Json(name = "artistImageUrl") val artistImageUrl: String?,
        @Json(name = "coverArt") val coverArt: String,
        @Json(name = "id") val id: String,
        @Json(name = "name") val name: String
    )

    @JsonClass(generateAdapter = true)
    data class Song(
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
        @Json(name = "parent") val parent: String,
        @Json(name = "path") val path: String,
        @Json(name = "playCount") val playCount: Int,
        @Json(name = "size") val size: Int,
        @Json(name = "starred") val starred: String,
        @Json(name = "suffix") val suffix: String,
        @Json(name = "title") val title: String,
        @Json(name = "track") val track: Int,
        @Json(name = "type") val type: String,
        @Json(name = "userRating") val userRating: Int,
        @Json(name = "year") val year: Int
    )
}