package com.aradsheybak.aradcrypto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.aradsheybak.aradcrypto.presentation.main.MainScreen
import com.aradsheybak.aradcrypto.ui.theme.AradCryptoTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AradCryptoTheme {
                MainScreen()

            }
        }
    }


}
