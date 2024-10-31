package com.example.marketplacepuj.core.utils

import android.icu.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateFormat {
    fun getCurrentDateFormatted(pattern: String = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"): String {
        val currentDate = Date()
        val formatter = SimpleDateFormat(pattern, Locale.ENGLISH)
        return formatter.format(currentDate)
    }

    fun getDateFormatted(dateString: String = "dd/MM/yyyy"): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        val date = inputFormat.parse(dateString)
        return outputFormat.format(date!!)
    }

}