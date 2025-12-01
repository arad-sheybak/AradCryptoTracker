package com.aradsheybak.aradcrypto.core.domain.usecase

import com.aradsheybak.aradcrypto.core.domain.repository.CryptoRepository

class DisconnectWebSocketUseCase(
    private val repository: CryptoRepository
) {
    suspend operator fun invoke() {
        repository.disconnect()
    }
}