package com.aradsheybak.aradcrypto.core.domain.entity

import kotlin.coroutines.EmptyCoroutineContext.get

data class Currency(
    val symbol: String,
    val last: String,
    val open: String,
    val close: String,
    val high: String,
    val low: String,
    val volume: String,
    val sellTotal: String,
    val buyTotal: String,
    val period: Long,
    val deal: String,
    val timestamp: Long = System.currentTimeMillis()){
        val lastPrice: Double
        get() = last.toDoubleOrNull() ?: 0.0

        val openPrice: Double
        get() = open.toDoubleOrNull() ?: 0.0

        val change: Double
        get() = lastPrice - openPrice

        val changePercent: Double
        get() = if (openPrice != 0.0) (change / openPrice) * 100 else 0.0

        val isPositive: Boolean
        get() = change >= 0
    }

