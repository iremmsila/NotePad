package com.example.myapplication

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay

@Composable
fun SplashScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onBackground),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.mipmap.pencil), // Logonun doğru yolunu kontrol edin
            contentDescription = "Logo",
            modifier = Modifier.size(64.dp) // Logonun boyutunu ayarlayın
        )
    }
}

@Composable
fun MainScreen() {
    var isLoading by remember { mutableStateOf(true) }

    if (isLoading) {
        SplashScreen()
        LaunchedEffect(Unit) {
            delay(2000)
            isLoading = false
        }
    } else {
        val navController = rememberNavController()
        // Uygulamanızın ana ekranı burada
        Greeting(navController = navController)
    }
}



