package com.aradsheybak.aradcrypto.core.domain.usecase

import android.util.Log
import com.aradsheybak.aradcrypto.core.domain.entity.Currency
import com.aradsheybak.aradcrypto.core.domain.repository.CryptoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach

class GetRealTimeCurrenciesUseCase(
    private val repository: CryptoRepository
) {
    operator fun invoke(): Flow<List<Currency>> {
        Log.d("USECASE", "🔄 UseCase invoked")
        return repository.getRealTimeCurrencies()
            .onEach { currencies ->
                Log.d("USECASE", "📊 Emitting ${currencies.size} currencies")
            }
    }
}