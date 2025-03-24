package com.example.notepadmvvm.modelview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notepadmvvm.model.repository.OnboardingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OnboardingViewModel(private val repository: OnboardingRepository) : ViewModel() {

    private val _showOnboarding = MutableStateFlow(repository.isOnboardingShown())
    val showOnboarding: StateFlow<Boolean> = _showOnboarding

    val onboardPages = repository.getOnboardPages() // Repository’den veri çekiliyor.

    fun completeOnboarding() {
        viewModelScope.launch {
            repository.setOnboardingShown()
            _showOnboarding.value = false
        }
    }
}
