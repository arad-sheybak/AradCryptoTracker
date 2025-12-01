package com.aradsheybak.aradcrypto.data.remote.dto.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SubscribeRequest(
    @field:Json(name = "method") val method: String = "state.subscribe",
    @field:Json(name = "params") val params: List<String>,
    @field:Json(name = "id") val id: Int = 1
)