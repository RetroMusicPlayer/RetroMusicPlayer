package ru.stersh.apisonic.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MusicFoldersResponse(@Json(name = "musicFolders") val musicFolders: MusicFolders) : SubsonicResponse

@JsonClass(generateAdapter = true)
data class MusicFolders(
    @Json(name = "musicFolder") val musicFolders: List<MusicFolder>
)

@JsonClass(generateAdapter = true)
data class MusicFolder(
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name: String
)