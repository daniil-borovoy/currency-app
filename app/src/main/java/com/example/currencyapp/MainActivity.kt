package com.example.currencyapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.currencyapp.repositories.CurrencyRepository
import com.example.currencyapp.repositories.CurrencyResponse
import com.example.currencyapp.ui.theme.CurrencyAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CurrencyAppTheme {
                App()
            }
        }
    }
}

@Composable
fun CurrencyList() {
    var currencyResponse: CurrencyResponse? by remember { mutableStateOf(null) }

    // Fetch currency data on initial composition
    LaunchedEffect(Unit) {
        val currencyRepository = CurrencyRepository()
        try {
            val response = currencyRepository.fetchCurrencyData()
            if (response.isSuccess) {
                currencyResponse = response.getOrNull()
            } else {
                // Handle the error case here
            }
        } catch (e: Exception) {
            // Handle the exception here
        }
    }

    // Check if the data is available and not null
    currencyResponse?.let { response ->
        Column (modifier = Modifier
            .verticalScroll(rememberScrollState()), Arrangement.Center){
            for ((currencyCode, exchangeRate) in response.exchangeRates) {
                CurrencyItem(currencyCode, exchangeRate)
            }
        }
    }
}

@Composable
fun CurrencyItem(currencyCode: String, exchangeRate: Double) {
    // Display currency code and exchange rate here
    Text(text = "$currencyCode: $exchangeRate")
}

@Composable
fun App() {
    CurrencyList()
}