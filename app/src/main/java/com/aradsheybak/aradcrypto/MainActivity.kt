package com.aradsheybak.aradcrypto

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import com.aradsheybak.aradcrypto.core.domain.usecase.ConnectWebSocketUseCase
import com.aradsheybak.aradcrypto.core.domain.usecase.DisconnectWebSocketUseCase
import com.aradsheybak.aradcrypto.core.domain.usecase.GetRealTimeCurrenciesUseCase
import com.aradsheybak.aradcrypto.data.remote.websocket.WebSocketService
import com.aradsheybak.aradcrypto.presentation.TestScreen
import com.aradsheybak.aradcrypto.ui.theme.AradCryptoTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
    private val getRealTimeCurrenciesUseCase: GetRealTimeCurrenciesUseCase by inject()
    private val connectWebSocketUseCase: ConnectWebSocketUseCase by inject()
    private val disconnectWebSocketUseCase: DisconnectWebSocketUseCase by inject()
    private val webSocketService: WebSocketService by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AradCryptoTheme {
                TestScreen()
            }
            testWebSocket()
        }
    }

    fun testWebSocket() {
        lifecycleScope.launch {
            Log.d("TEST", "🔌 Checking connection state...")

            // اول connection state رو مشاهده کن
            launch {
                webSocketService.getConnectionState().collect { state ->
                    Log.d("CONNECTION", "State: $state")
                }
            }

            delay(1000)
            connectWebSocketUseCase()

            getRealTimeCurrenciesUseCase().collect { currencies ->
                Log.d("TEST", "✅ Received ${currencies.size} currencies")
                currencies.forEach { currency ->
                    Log.d(
                        "TEST",
                        "💰 ${currency.symbol}: ${currency.last} " +
                                "Change: ${"%.2f".format(currency.changePercent)}%"
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycleScope.launch {
            disconnectWebSocketUseCase()
            Log.d("TEST", "Disconnected from WebSocket")
        }
    }
}
