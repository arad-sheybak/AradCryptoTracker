package com.aradsheybak.aradcrypto.presentation.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aradsheybak.aradcrypto.presentation.components.CurrencyItem
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel
import com.aradsheybak.aradcrypto.R
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel = koinViewModel(),
    onCurrencyClick: (String) -> Unit = {}
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    // Collect effects
    LaunchedEffect(key1 = Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is MainContract.Effect.ShowToast -> {
                    snackbarHostState.showSnackbar(effect.message)
                }

                is MainContract.Effect.NavigateToDetail -> {
                    onCurrencyClick(effect.currencyId)
                }

                is MainContract.Effect.ShowErrorDialog -> {
                    // Handle error dialog
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Arad Crypto Tracker",
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    // Connection status
                    ConnectionStatus(state.isConnected, state.lastUpdate)

                    // Refresh button
                    IconButton(
                        onClick = { viewModel.processIntent(MainContract.Intent.Refresh) }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_refresh),
                            contentDescription = "Refresh"
                        )
                    }
                }
            )
        },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White)
                .padding(paddingValues)
        ) {
            when {
                state.isLoading -> {
                    LoadingState()
                }

                state.error != null -> {
                    ErrorState(
                        error = state.error!!,
                        onRetry = { viewModel.processIntent(MainContract.Intent.RetryConnection) }
                    )
                }

                state.currencies.isEmpty() -> {
                    EmptyState(
                        isConnected = state.isConnected,
                        onConnect = { viewModel.processIntent(MainContract.Intent.Connect) }
                    )
                }

                else -> {
                    CurrencyList(
                        currencies = state.currencies,
                        onCurrencyClick = { currencyId ->
                            viewModel.processIntent(MainContract.Intent.OnCurrencyClick(currencyId))
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun ConnectionStatus(isConnected: Boolean, lastUpdate: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(end = 16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .background(
                    color = if (isConnected) Color(0xFF4CAF50) else Color(0xFFF44336),
                    shape = CircleShape
                )
        )

        if (isConnected && lastUpdate.isNotEmpty()) {
            Text(
                text = "Updated: $lastUpdate",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            Text(
                text = if (isConnected) "Connected" else "Disconnected",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun LoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CircularProgressIndicator()
            Text("Connecting to WebSocket...")
        }
    }
}

@Composable
private fun ErrorState(error: String, onRetry: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(32.dp)
        ) {
            Text(
                text = "⚠️",
                fontSize = 48.sp,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Connection Error",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Text(
                text = error,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Button(onClick = onRetry) {
                Text("Retry Connection")
            }
        }
    }
}

@Composable
private fun EmptyState(isConnected: Boolean, onConnect: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(32.dp)
        ) {
            Text(
                text = "📈",
                fontSize = 48.sp,
                textAlign = TextAlign.Center
            )

            Text(
                text = if (isConnected) "Waiting for data..." else "Not Connected",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Text(
                text = if (isConnected)
                    "WebSocket connected. Waiting for cryptocurrency data..."
                else
                    "Connect to start receiving real-time prices",
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (!isConnected) {
                Button(onClick = onConnect) {
                    Text("Connect Now")
                }
            }
        }
    }
}

@Composable
private fun CurrencyList(
    currencies: List<MainContract.CurrencyUi>,
    onCurrencyClick: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(
            items = currencies,
            key = { it.id }
        ) { currency ->
            CurrencyItem(
                currency = currency,
                onClick = { onCurrencyClick(currency.id) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MaterialTheme {
        MainScreen()
    }
}