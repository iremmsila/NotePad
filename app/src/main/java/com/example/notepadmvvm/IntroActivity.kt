package com.example.notepadmvvm

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.notepadmvvm.model.repository.OnboardingRepository
import com.example.notepadmvvm.modelview.OnboardingViewModel
import com.example.notepadmvvm.ui.theme.NotepadMVVMTheme
import com.example.notepadmvvm.view.OnboardScreen


class IntroScreens : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repository = OnboardingRepository(applicationContext)
        val viewModel = OnboardingViewModel(repository)

        setContent {
            NotepadMVVMTheme {
                OnboardScreen(
                    viewModel = viewModel,
                    navController = rememberNavController(),
                    onFinish = {
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
                )
            }
        }
    }
}
