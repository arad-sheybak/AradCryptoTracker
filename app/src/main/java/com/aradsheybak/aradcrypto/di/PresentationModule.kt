package com.aradsheybak.aradcrypto.di

import com.aradsheybak.aradcrypto.presentation.main.MainViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {

    viewModel { MainViewModel(get(), get(), get(), get()) }

    // for the nex version, when details screen added
    // viewModel { DetailViewModel(get()) }


}