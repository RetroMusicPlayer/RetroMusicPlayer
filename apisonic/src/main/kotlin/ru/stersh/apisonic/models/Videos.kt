package ru.stersh.apisonic.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VideosResponse(@Json(name = "videos") val videos: VideosObject) : SubsonicResponse

@JsonClass(generateAdapter = true)
data class VideosObject(
    @Json(name = "video") val videos: List<Video>
)

@JsonClass(generateAdapter = true)
data class Video(
    @Json(name = "album") val album: String,
    @Json(name = "bitRate") val bitRate: Int,
    @Json(name = "bookmarkPosition") val bookmarkPosition: Int,
    @Json(name = "contentType") val contentType: String,
    @Json(name = "created") val created: String,
    @Json(name = "duration") val duration: Int,
    @Json(name = "id") val id: String,
    @Json(name = "isDir") val isDir: Boolean,
    @Json(name = "isVideo") val isVideo: Boolean,
    @Json(name = "originalHeight") val originalHeight: Int,
    @Json(name = "originalWidth") val originalWidth: Int,
    @Json(name = "parent") val parent: String,
    @Json(name = "path") val path: String,
    @Json(name = "playCount") val playCount: Int,
    @Json(name = "size") val size: Int,
    @Json(name = "suffix") val suffix: String,
    @Json(name = "title") val title: String,
    @Json(name = "type") val type: String
)
