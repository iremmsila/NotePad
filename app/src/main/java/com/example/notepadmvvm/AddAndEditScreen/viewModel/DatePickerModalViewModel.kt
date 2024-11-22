package com.example.notepadmvvm.AddAndEditScreen.viewModel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DatePickerViewModel : ViewModel() {
    private val _selectedDate = MutableLiveData<String?>()
    val selectedDate: LiveData<String?> = _selectedDate

    private val _showDatePicker = MutableLiveData(false)
    val showDatePicker: LiveData<Boolean> = _showDatePicker

    fun setDate(dateInMillis: Long?) {
        val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        _selectedDate.value = dateInMillis?.let { sdf.format(Date(it)) }
    }

    fun toggleDatePicker() {
        _showDatePicker.value = !_showDatePicker.value!!
    }
}
