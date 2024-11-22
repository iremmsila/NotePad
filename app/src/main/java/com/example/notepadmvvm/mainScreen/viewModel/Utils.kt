package com.example.notepadmvvm.mainScreen.viewModel

import java.text.SimpleDateFormat
import java.util.*

object Utils {
    fun calculateDaysBetween(dateString: String, format: String = "dd-MM-yyyy"): Long {
        val dateFormat = SimpleDateFormat(format, Locale.getDefault())
        return try {
            val targetDate: Date = dateFormat.parse(dateString) ?: return -1
            val currentDate = Date()
            val diffInMillis: Long = targetDate.time - currentDate.time
            diffInMillis / (1000 * 60 * 60 * 24) // Millis'i gün cinsine çevir
        } catch (e: Exception) {
            -1 // Hata durumunda -1 döndür
        }
    }
}
