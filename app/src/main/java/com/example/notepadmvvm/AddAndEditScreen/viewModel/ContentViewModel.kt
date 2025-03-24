package com.example.notepadmvvm.AddAndEditScreen.viewModel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


class GenericTextFieldViewModel : ViewModel() {
    private val _text = MutableStateFlow("")
    val text: StateFlow<String> = _text.asStateFlow()

    private val _isError = MutableStateFlow(false)
    val isError: StateFlow<Boolean> = _isError.asStateFlow()

    fun onTextChange(newText: String) {
        _text.value = newText
        _isError.value = newText.isBlank()
    }

    fun loadData(existingText: String) {
        _text.value = existingText
        _isError.value = existingText.isBlank()
    }
}
