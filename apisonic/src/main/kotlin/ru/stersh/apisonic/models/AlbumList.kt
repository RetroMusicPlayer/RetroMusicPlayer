package ru.stersh.apisonic.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AlbumListResponse(@Json(name = "albumList") val albumList: AlbumList) : SubsonicResponse

@JsonClass(generateAdapter = true)
data class AlbumList(@Json(name = "album") val albums: List<Album>) {

    @JsonClass(generateAdapter = true)
    data class Album(
        @Json(name = "album") val album: String,
        @Json(name = "artist") val artist: String,
        @Json(name = "averageRating") val averageRating: Double?,
        @Json(name = "coverArt") val coverArt: String,
        @Json(name = "created") val created: String,
        @Json(name = "genre") val genre: String,
        @Json(name = "id") val id: String,
        @Json(name = "isDir") val isDir: Boolean,
        @Json(name = "parent") val parent: String,
        @Json(name = "playCount") val playCount: Int,
        @Json(name = "title") val title: String,
        @Json(name = "year") val year: Int
    )
}


@JsonClass(generateAdapter = true)
data class AlbumList2Response(@Json(name = "albumList2") val albumList2: AlbumList2) : SubsonicResponse

@JsonClass(generateAdapter = true)
data class AlbumList2(@Json(name = "album") val albums: List<Album>) {

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
        @Json(name = "songCount") val songCount: Int,
        @Json(name = "starred") val starred: String?,
        @Json(name = "year") val year: Int
    )
}