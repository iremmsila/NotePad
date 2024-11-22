package com.example.notepadmvvm.view


import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import com.example.notepadmvvm.IntroScreens
import com.example.notepadmvvm.R
import com.example.notepadmvvm.model.SplashState
import com.example.notepadmvvm.modelview.SplashScreenViewModel
import androidx.lifecycle.viewmodel.compose.viewModel



@Composable
fun SplashScreen(
    viewModel: SplashScreenViewModel = viewModel()
) {
    val splashState by viewModel.splashState.collectAsState()
    val context = LocalContext.current

    when (splashState) {
        SplashState.Loading -> SplashContent()
        SplashState.Completed -> {
            LaunchedEffect(Unit) {
                val activity = context as? Activity
                activity?.let {
                    val intent = Intent(it, IntroScreens::class.java)
                    it.startActivity(intent)
                    it.finish()
                }
            }
        }
    }
}

@Composable
fun SplashContent() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.mipmap.pencil),
            contentDescription = stringResource(R.string.logo),
            modifier = Modifier.size(64.dp)
        )
    }
}
