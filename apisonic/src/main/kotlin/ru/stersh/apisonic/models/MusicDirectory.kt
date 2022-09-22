package ru.stersh.apisonic.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MusicDirectoryResponse(@Json(name = "directory") val directory: Directory) : SubsonicResponse

@JsonClass(generateAdapter = true)
data class Directory(
    @Json(name = "averageRating") val averageRating: Double,
    @Json(name = "child") val children: List<Child>,
    @Json(name = "id") val id: String,
    @Json(name = "name") val name: String,
    @Json(name = "parent") val parent: String,
    @Json(name = "playCount") val playCount: Int
)

@JsonClass(generateAdapter = true)
data class Child(
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