package com.aradsheybak.aradcrypto.presentation.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aradsheybak.aradcrypto.core.domain.entity.Currency
import com.aradsheybak.aradcrypto.core.domain.usecase.ConnectWebSocketUseCase
import com.aradsheybak.aradcrypto.core.domain.usecase.DisconnectWebSocketUseCase
import com.aradsheybak.aradcrypto.core.domain.usecase.GetRealTimeCurrenciesUseCase
import com.aradsheybak.aradcrypto.data.remote.websocket.WebSocketService
import com.aradsheybak.aradcrypto.data.remote.websocket.WebSocketState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.*
import java.text.SimpleDateFormat
import java.util.*

class MainViewModel(
    private val getRealTimeCurrenciesUseCase: GetRealTimeCurrenciesUseCase,
    private val connectWebSocketUseCase: ConnectWebSocketUseCase,
    private val disconnectWebSocketUseCase: DisconnectWebSocketUseCase,
    private val webSocketService: WebSocketService
) : ViewModel(), MainContract.ViewModel {

    private val _state = MutableStateFlow(MainContract.State())
    override val state: StateFlow<MainContract.State> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<MainContract.Effect>()
    override val effect: SharedFlow<MainContract.Effect> = _effect.asSharedFlow()
    private val _currenciesMap = mutableMapOf<String, MainContract.CurrencyUi>()


    init {
        observeConnectionState()
        observeCurrencies()
        connect()
    }

    private fun observeConnectionState() {
        viewModelScope.launch {
            webSocketService.getConnectionState().collect { state ->
                Log.d("VIEWMODEL_CONN", "📡 Connection state: $state")

                when (state) {
                    is WebSocketState.Connected -> {
                        _state.update { it.copy(isConnected = true, isLoading = false) }
                    }
                    is WebSocketState.Connecting -> {
                        _state.update { it.copy(isConnected = false, isLoading = true) }
                    }
                    is WebSocketState.Disconnected -> {
                        _state.update { it.copy(isConnected = false, isLoading = false) }
                    }
                    is WebSocketState.Error -> {
                        _state.update {
                            it.copy(
                                isConnected = false,
                                isLoading = false,
                                error = state.message
                            )
                        }
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun observeCurrencies() {
        viewModelScope.launch {
            getRealTimeCurrenciesUseCase()
                .collect { newCurrencies ->
                    newCurrencies.forEach { currency ->
                        _currenciesMap[currency.symbol] = convertToUi(currency)
                    }

                    _state.update {
                        it.copy(
                            currencies = _currenciesMap.values.toList(),
                            isLoading = false,
                            isConnected = true,
                            lastUpdate = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
                        )
                    }
                }
        }
    }
    private fun convertToUi(currency: Currency): MainContract.CurrencyUi {
        val changePercent = currency.changePercent
        val isPositive = changePercent >= 0
        val changeValue = currency.change

        return MainContract.CurrencyUi(
            id = currency.symbol,
            symbol = currency.symbol,
            name = getCurrencyName(currency.symbol),
            currentPrice = formatPrice(currency.last),
            changePercent = "${if (isPositive) "+" else ""}${"%.2f".format(changePercent)}%",
            changeValue = "${if (isPositive) "+" else ""}${"%.2f".format(changeValue)}",
            isPositive = isPositive,
            iconRes = getCurrencyIcon(currency.symbol)
        )
    }

    private fun getCurrencyName(symbol: String): String {
        return when {
            symbol.contains("BTC") -> "Bitcoin"
            symbol.contains("ETH") -> "Ethereum"
            symbol.contains("ADA") -> "Cardano"
            symbol.contains("USDT") -> symbol.replace("USDT", "")
            else -> symbol
        }
    }

    private fun formatPrice(price: String): String {
        val value = price.toDoubleOrNull() ?: 0.0
        return if (value >= 1000) {
            "$${"%.0f".format(value)}"
        } else {
            "$${"%.2f".format(value)}"
        }
    }

    private fun getCurrencyIcon(symbol: String): Int {
        return when {
            symbol.contains("BTC") -> android.R.drawable.star_big_on
            symbol.contains("ETH") -> android.R.drawable.star_big_on
            symbol.contains("ADA") -> android.R.drawable.star_big_on
            else -> android.R.drawable.star_big_on
        }
    }

    override fun processIntent(intent: MainContract.Intent) {
        when (intent) {
            MainContract.Intent.Connect -> connect()
            MainContract.Intent.Disconnect -> disconnect()
            MainContract.Intent.RetryConnection -> retryConnection()
            is MainContract.Intent.OnCurrencyClick -> onCurrencyClick(intent.currencyId)
            MainContract.Intent.Refresh -> refresh()
            MainContract.Intent.ClearError -> clearError()
            else -> {}
        }
    }

    private fun connect() {
        _state.update { it.copy(isLoading = true, isConnected = false) }
        viewModelScope.launch {
            try {
                connectWebSocketUseCase()
                _effect.emit(MainContract.Effect.ShowToast("Connected successfully"))
            } catch (e: Exception) {
                _state.update { it.copy(error = "Connection failed: ${e.message}") }
            }
        }
    }

    private fun disconnect() {
        viewModelScope.launch {
            disconnectWebSocketUseCase()
            _state.update { it.copy(isConnected = false, currencies = emptyList()) }
            _effect.emit(MainContract.Effect.ShowToast("Disconnected"))
        }
    }

    private fun retryConnection() {
        clearError()
        connect()
    }

    private fun onCurrencyClick(currencyId: String) {
        viewModelScope.launch {
            _effect.emit(MainContract.Effect.NavigateToDetail(currencyId))
        }
    }

    private fun refresh() {
        connect()
    }

    private fun clearError() {
        _state.update { it.copy(error = null) }
    }

    override fun onCleared() {
        super.onCleared()
        disconnect()
    }
}