package com.aradsheybak.aradcrypto.data.remote.websocket

sealed class WebSocketState {
    object Disconnected : WebSocketState()
    object Connecting : WebSocketState()
    object Connected : WebSocketState()
    object Disconnecting : WebSocketState()
    data class Error(val message: String) : WebSocketState()
}