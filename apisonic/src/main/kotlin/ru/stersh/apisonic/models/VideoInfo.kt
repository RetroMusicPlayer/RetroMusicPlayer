package ru.stersh.apisonic.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VideoInfoResponse(@Json(name = "videoInfo") val videoInfo: VideoInfo) : SubsonicResponse

@JsonClass(generateAdapter = true)
data class VideoInfo(
    @Json(name = "audioTrack") val audioTrack: List<AudioTrack>,
    @Json(name = "id") val id: String
) {
    @JsonClass(generateAdapter = true)
    data class AudioTrack(
        @Json(name = "id") val id: String,
        @Json(name = "languageCode") val languageCode: String,
        @Json(name = "name") val name: String
    )
}

