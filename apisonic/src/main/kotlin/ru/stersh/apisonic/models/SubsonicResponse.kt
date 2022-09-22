package ru.stersh.apisonic.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

interface SubsonicResponse

@JsonClass(generateAdapter = true)
data class Response<T : SubsonicResponse>(
    @Json(name = "subsonic-response") val subsonicResponse: T
)


@JsonClass(generateAdapter = true)
data class EmptyResponse(
    @Json(name = "status") val status: String,
    @Json(name = "version") val version: String
) : SubsonicResponse


@JsonClass(generateAdapter = true)
data class LicenseResponse(
    @Json(name = "license") val license: License
) : SubsonicResponse

@JsonClass(generateAdapter = true)
data class License(
    @Json(name = "email") val email: String,
    @Json(name = "trialExpires") val trialExpires: String,
    @Json(name = "valid") val valid: Boolean
)


