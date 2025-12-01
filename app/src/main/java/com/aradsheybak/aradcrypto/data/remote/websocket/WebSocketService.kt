package com.aradsheybak.aradcrypto.data.remote.websocket

import android.util.Log
import com.aradsheybak.aradcrypto.data.remote.dto.request.SubscribeRequest
import com.aradsheybak.aradcrypto.data.remote.dto.response.CurrencyDto
import com.aradsheybak.aradcrypto.data.remote.dto.response.WebSocketResponseDto
import com.squareup.moshi.Moshi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onStart

class WebSocketService(
    private val webSocketClient: CoinExWebSocketClient,
    private val moshi: Moshi
) {
    private val subscribeAdapter = moshi.adapter(SubscribeRequest::class.java)
    private val responseAdapter = moshi.adapter(WebSocketResponseDto::class.java)

    fun connect() {
        webSocketClient.connect()
    }

    fun disconnect() {
        webSocketClient.disconnect()
    }

    fun subscribeToCurrencies(symbols: List<String>) {
        val request = SubscribeRequest(params = symbols)
        val json = subscribeAdapter.toJson(request)
        webSocketClient.sendMessage(json)
    }

    fun getCurrencyStream(): Flow<List<CurrencyDto>> {
        return webSocketClient.messages
            .mapNotNull { message ->
                Log.d("WEBSOCKET_RAW", "📨 Raw message: $message")
                parseWebSocketMessage(message).also { result ->
                    Log.d("WEBSOCKET_PARSED", "✅ Parsed ${result?.size ?: 0} currencies")
                }
            }
            .onStart {
                Log.d("WEBSOCKET", "🎬 Starting currency stream")
            }
    }

    private fun parseWebSocketMessage(message: String): List<CurrencyDto>? {
        return try {
            val response = responseAdapter.fromJson(message)
            if (response?.method == "state.update") {
                response.params?.firstOrNull()?.map { (symbol, dto) ->
                    dto.copy(symbol = symbol)
                }
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    fun getConnectionState(): StateFlow<WebSocketState> {
        return webSocketClient.connectionState
    }
}