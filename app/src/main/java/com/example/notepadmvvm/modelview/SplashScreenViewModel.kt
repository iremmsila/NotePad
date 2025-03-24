package com.example.notepadmvvm.modelview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notepadmvvm.model.SplashState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SplashScreenViewModel : ViewModel() {
    private val _splashState = MutableStateFlow<SplashState>(SplashState.Loading)
    val splashState: StateFlow<SplashState> = _splashState

    init {
        viewModelScope.launch {
            delay(2000) // 2 saniye bekleme
            _splashState.value = SplashState.Completed
        }
    }
}