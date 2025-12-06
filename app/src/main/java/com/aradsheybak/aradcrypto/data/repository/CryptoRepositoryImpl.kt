package com.aradsheybak.aradcrypto.data.repository

import android.util.Log
import com.aradsheybak.aradcrypto.core.domain.entity.Currency
import com.aradsheybak.aradcrypto.core.domain.repository.CryptoRepository
import com.aradsheybak.aradcrypto.data.mapper.CurrencyMapper
import com.aradsheybak.aradcrypto.data.remote.websocket.WebSocketService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.collections.emptyList

class CryptoRepositoryImpl(
    private val webSocketService: WebSocketService,
    private val currencyMapper: CurrencyMapper
) : CryptoRepository {


    override fun getRealTimeCurrencies(): Flow<List<Currency>> {
        return webSocketService.getCurrencyStream()
            .map { dtos ->
                Log.d("REPOSITORY", "📨 Received ${dtos.size} DTOs")
                currencyMapper.dtoListToEntityList(dtos)
            }
            .onStart {
                Log.d("REPOSITORY", "🚀 Starting currency stream")
                emit(emptyList())
            }
            .stateIn(
                scope = CoroutineScope(Dispatchers.Default),
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )
    }

    override fun connect() {
        Log.d("REPOSITORY", "🔗 Connecting...")
        webSocketService.connect()

        CoroutineScope(Dispatchers.IO).launch {
            delay(2000)
            Log.d("REPOSITORY", "📡 Subscribing to currencies...")
            webSocketService.subscribeToCurrencies(listOf("BTCUSDT", "ETHUSDT", "ADAUSDT"))
        }
    }

    override fun disconnect() {
        webSocketService.disconnect()
    }
}