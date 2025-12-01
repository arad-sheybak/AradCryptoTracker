package com.aradsheybak.aradcrypto.di

import com.aradsheybak.aradcrypto.core.domain.repository.CryptoRepository
import com.aradsheybak.aradcrypto.core.domain.usecase.ConnectWebSocketUseCase
import com.aradsheybak.aradcrypto.core.domain.usecase.DisconnectWebSocketUseCase
import com.aradsheybak.aradcrypto.core.domain.usecase.GetRealTimeCurrenciesUseCase
import com.aradsheybak.aradcrypto.data.repository.CryptoRepositoryImpl
import org.koin.dsl.module

val domainModule = module {
    // Repository
    single<CryptoRepository> {
        get<CryptoRepositoryImpl>()
    }

    // UseCases
    factory { GetRealTimeCurrenciesUseCase(get()) }
    factory { ConnectWebSocketUseCase(get()) }
    factory { DisconnectWebSocketUseCase(get()) }
}