package com.aradsheybak.aradcrypto.di

import com.aradsheybak.aradcrypto.core.domain.repository.CryptoRepository
import com.aradsheybak.aradcrypto.data.mapper.CurrencyMapper
import com.aradsheybak.aradcrypto.data.remote.websocket.CoinExWebSocketClient
import com.aradsheybak.aradcrypto.data.remote.websocket.WebSocketService
import com.aradsheybak.aradcrypto.data.repository.CryptoRepositoryImpl
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import org.koin.dsl.module
import java.util.concurrent.TimeUnit

val dataModule = module {
    // Moshi
    single {
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    // OkHttpClient with WebSocket support
    single {
        OkHttpClient.Builder()
            .pingInterval(30, TimeUnit.SECONDS) // holding connection
            .build()
    }

    // WebSocket Client & Service
    single { CoinExWebSocketClient(get()) }
    single { WebSocketService(get(), get()) }

    // Mapper
    factory { CurrencyMapper() }

    // Repository
    single<CryptoRepository> { CryptoRepositoryImpl(get(), get()) }
}