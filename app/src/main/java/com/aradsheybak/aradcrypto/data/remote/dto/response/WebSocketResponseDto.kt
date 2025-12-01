package com.aradsheybak.aradcrypto.data.remote.dto.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WebSocketResponseDto(
    @field:Json(name = "method") val method: String?,
    @field:Json(name = "params") val params: List<Map<String, CurrencyDto>>?,
    @field:Json(name = "error") val error: String?,
    @field:Json(name = "result") val result: Map<String, Any>?,
    @field:Json(name = "id") val id: Int?
)