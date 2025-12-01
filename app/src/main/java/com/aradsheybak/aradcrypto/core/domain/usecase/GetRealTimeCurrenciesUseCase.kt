package com.aradsheybak.aradcrypto.core.domain.usecase

import com.aradsheybak.aradcrypto.core.domain.entity.Currency
import com.aradsheybak.aradcrypto.core.domain.repository.CryptoRepository
import kotlinx.coroutines.flow.Flow

class GetRealTimeCurrenciesUseCase(
    private val repository: CryptoRepository
) {
    operator fun invoke(): Flow<List<Currency>> =
        repository.getRealTimeCurrencies()
}