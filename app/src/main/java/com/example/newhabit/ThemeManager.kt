package com.example.newhabit

import androidx.appcompat.app.AppCompatDelegate

// Usamos um 'object' para criar um Singleton, garantindo que a lógica
// de tema seja acessada de um único ponto.
object ThemeManager {

    private const val LIGHT_MODE = "light"
    private const val DARK_MODE = "dark"
    private const val SYSTEM_DEFAULT = "system"

    fun applyTheme(themeValue: String) {
        when (themeValue) {
            LIGHT_MODE -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            DARK_MODE -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            else -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
    }
}
