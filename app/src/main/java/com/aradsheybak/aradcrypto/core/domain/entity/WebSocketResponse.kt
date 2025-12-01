package com.aradsheybak.aradcrypto.core.domain.entity

data class WebSocketResponse(
    val method : String,
    val params:List<Map<String,Currency>>
)
