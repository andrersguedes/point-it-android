package com.andrersguedes.pointit.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalLocale
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun Long.toFormattedDateTime(): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm", LocalLocale.current.platformLocale)
    return formatter.format(Date(this))
}