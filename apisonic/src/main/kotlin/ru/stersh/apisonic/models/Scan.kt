package ru.stersh.apisonic.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ScanStatusResponse(@Json(name = "scanStatus") val scanStatus: ScanStatus) : SubsonicResponse

@JsonClass(generateAdapter = true)
data class ScanStatus(
    @Json(name = "status") val status: String,
    @Json(name = "count") val count: Int
)