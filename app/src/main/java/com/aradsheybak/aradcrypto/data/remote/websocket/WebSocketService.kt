package com.aradsheybak.aradcrypto.data.remote.websocket

import android.util.Log
import com.aradsheybak.aradcrypto.data.remote.dto.request.SubscribeRequest
import com.aradsheybak.aradcrypto.data.remote.dto.response.CurrencyDto
import com.aradsheybak.aradcrypto.data.remote.dto.response.WebSocketResponseDto
import com.squareup.moshi.Moshi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

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

    fun getCurrencyStream(): Flow<Map<String, CurrencyDto>> {
        return webSocketClient.messages
            .mapNotNull { message ->
                parseWebSocketMessage(message)
            }
    }
    private fun parseWebSocketMessage(message: String): Map<String, CurrencyDto>?{
        return try {
            val response = responseAdapter.fromJson(message)

            if (response?.method == "state.update") {
                response.params?.firstOrNull()
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("WS_PARSER", "❌ Parse error: ${e.message}")
            null
        }
    }

    fun getConnectionState(): StateFlow<WebSocketState> {
        return webSocketClient.connectionState
    }
}
data class CurrencyWithSymbol(
    val symbol: String,
    val data: CurrencyDto
)