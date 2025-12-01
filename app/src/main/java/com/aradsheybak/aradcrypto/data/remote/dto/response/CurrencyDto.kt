package com.aradsheybak.aradcrypto.data.remote.dto.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CurrencyDto(
    val symbol: String,
    @field:Json(name = "last") val last: String,
    @field:Json(name = "open") val open: String,
    @field:Json(name = "close") val close: String,
    @field:Json(name = "high") val high: String,
    @field:Json(name = "low") val low: String,
    @field:Json(name = "volume") val volume: String,
    @field:Json(name = "sell_total") val sellTotal: String,
    @field:Json(name = "buy_total") val buyTotal: String,
    @field:Json(name = "period") val period: Long,
    @field:Json(name = "deal") val deal: String
)