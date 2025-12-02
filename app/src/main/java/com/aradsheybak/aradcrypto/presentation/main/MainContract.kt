package com.aradsheybak.aradcrypto.presentation.main

import androidx.annotation.DrawableRes
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

object MainContract {

    data class State(
        val currencies: List<CurrencyUi> = emptyList(),
        val isLoading: Boolean = false,
        val error: String? = null,
        val isConnected: Boolean = false,
        val lastUpdate: String = ""
    )

    data class CurrencyUi(
        val id: String,
        val symbol: String,
        val name: String,
        val currentPrice: String,
        val changePercent: String,
        val changeValue: String,
        val isPositive: Boolean,
        @field:DrawableRes val iconRes: Int
    )

    sealed class Intent {
        // Connection
        object Connect : Intent()
        object Disconnect : Intent()
        object RetryConnection : Intent()

        // UI Actions
        data class OnCurrencyClick(val currencyId: String) : Intent()
        object Refresh : Intent()
        object ClearError : Intent()

        // Settings
        object OpenSettings : Intent()
    }

    sealed class Effect {
        data class ShowToast(val message: String) : Effect()
        data class NavigateToDetail(val currencyId: String) : Effect()
        data class ShowErrorDialog(val message: String) : Effect()
    }

    // ViewModel Interface
    interface ViewModel {
        val state: StateFlow<State>
        val effect: SharedFlow<Effect>

        fun processIntent(intent: Intent)
    }
}