package com.example.weatherapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.weatherapp.api.RetrofitInstance
import com.example.weatherapp.model.WeatherResponse
import com.example.weatherapp.ui.theme.WeatherAppTheme
import kotlinx.coroutines.launch
import com.example.weatherapp.BuildConfig



class MainActivity : ComponentActivity() {
    val apiKey = BuildConfig.WEATHER_API_KEY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeatherAppTheme {
                WeatherApp(apiKey)
            }
        }
    }
}

@Composable
fun WeatherApp(apiKey: String) {
    var city by remember {
        mutableStateOf("")
    }
    var weatherInfo by remember {
        mutableStateOf<String?>(null)
    }
    var isLoading by remember {
        mutableStateOf(false)
    }

    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = city,
            onValueChange = { city = it },
            label = { Text("Enter city name") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            isLoading = true
            weatherInfo = null
            scope.launch {
                try {
                    val response = RetrofitInstance.api.getWeatherInfo(city, apiKey)
                    Log.d("weatherAPI","Weatherapi response, $response")
                    weatherInfo = "Weather in ${response.name}: ${response.main.temp}°C"
                } catch (e: Exception) {
                    weatherInfo = "Could not fetch weather for '$city'"
                    Log.d("failedFetch", "failed to fetch weather for city")
                } finally {
                    isLoading = false
                }
            }
        }) {
            Text("Get Weather")
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (isLoading) {
            CircularProgressIndicator()
        } else {
            weatherInfo?.let {
                Text(it, style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}

