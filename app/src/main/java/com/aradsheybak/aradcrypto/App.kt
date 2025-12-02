package com.aradsheybak.aradcrypto

import android.app.Application
import com.aradsheybak.aradcrypto.di.dataModule
import com.aradsheybak.aradcrypto.di.domainModule
import com.aradsheybak.aradcrypto.di.presentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App: Application(){
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(
                domainModule,
                dataModule,
                presentationModule
                )
        }
    }
}