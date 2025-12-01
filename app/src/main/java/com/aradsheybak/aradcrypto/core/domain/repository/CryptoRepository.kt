package com.aradsheybak.aradcrypto.core.domain.repository

import com.aradsheybak.aradcrypto.core.domain.entity.Currency
import kotlinx.coroutines.flow.Flow

interface CryptoRepository {
    fun getRealTimeCurrencies(): Flow<List<Currency>>
    fun connect()
    fun disconnect()
}