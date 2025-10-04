package com.example.newhabit

import android.app.Application
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ServiceLifecycleDispatcher
import androidx.lifecycle.coroutineScope
import com.example.newhabit.data.local.datastore.SettingsDataStore
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/*
    Annotate the Application class to trigger Hilt code generation.
    Injects the application context into dependencies that need it.
 */

/*
    Usando DataStore para persistência de preferências
 */
@HiltAndroidApp
class MyApplication : Application(), LifecycleOwner {
    private val dispatcher = ServiceLifecycleDispatcher(this)
    private lateinit var settingsDataStore: SettingsDataStore

    override val lifecycle: Lifecycle
        get() = dispatcher.lifecycle

    override fun onCreate() {
        super.onCreate()
        settingsDataStore = SettingsDataStore(this)

        // Lança uma coroutine no escopo do ciclo de vida da Application
        lifecycle.coroutineScope.launch {
            settingsDataStore.themeFlow.collect { savedTheme ->
                // Este bloco será executado na inicialização e toda vez
                // que o valor do tema for alterado no DataStore.
                ThemeManager.applyTheme(savedTheme)
            }
        }
    }
}

/*
    Usando sharedPreferences legado:

@HiltAndroidApp
class MyApplication : Application(), SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onCreate() {
        super.onCreate()
        // Registra um listener para ouvir mudanças nas preferências em tempo real
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        sharedPreferences.registerOnSharedPreferenceChangeListener(this)

        // Aplica o tema salvo assim que o app inicia
        val savedTheme = sharedPreferences.getString("theme_preference", "system")
        ThemeManager.applyTheme(savedTheme ?: "system")
    }

    // Este método é chamado toda vez que uma preferência é alterada
    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == "theme_preference") {
            val newTheme = sharedPreferences?.getString(key, "system")
            ThemeManager.applyTheme(newTheme ?: "system")
        }
    }
}
 */