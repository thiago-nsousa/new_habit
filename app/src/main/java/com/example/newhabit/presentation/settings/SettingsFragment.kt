package com.example.newhabit.presentation.settings

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.newhabit.data.local.datastore.SettingsDataStore
import com.example.newhabit.presentation.auth.AuthActivity
import com.google.firebase.auth.FirebaseAuth
import androidx.preference.ListPreference
import com.example.newhabit.R
import kotlinx.coroutines.launch

class SettingsFragment : PreferenceFragmentCompat() {
    private lateinit var settingsDataStore: SettingsDataStore

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        settingsDataStore = SettingsDataStore(requireContext())
        preferenceManager.preferenceDataStore = settingsDataStore
        setPreferencesFromResource(R.xml.settings_preferences, rootKey)

        findPreference<Preference>("logout_preference")?.setOnPreferenceClickListener {
            FirebaseAuth.getInstance().signOut()
            navigateToAuthActivity()
            true // Retorna true para indicar que o clique foi tratado
        }

        findPreference<ListPreference>("theme_preference")?.setOnPreferenceChangeListener { _, newValue ->
            // Lança uma coroutine para salvar o novo valor de forma assíncrona
            lifecycleScope.launch {
                settingsDataStore.putString(THEME_KEY, newValue as String)
            }
            true // Retorna true para confirmar que a UI da preferência pode ser atualizada
        }
    }

    private fun navigateToAuthActivity() {
        // Cria um Intent para a AuthActivity e limpa o histórico de navegação
        val intent = Intent(requireActivity(), AuthActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        requireActivity().finish()
    }

    companion object {
        val THEME_KEY = "theme_preference"
    }

}



/*
Usando SharedPreferences legado:

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.preferenceDataStore = SettingsDataStore(requireContext())
        setPreferencesFromResource(R.xml.settings_preferences, rootKey)

        // Encontrar a preferência de logout pelo seu 'key'
        val logoutPreference: Preference? = findPreference("logout_preference")

        logoutPreference?.setOnPreferenceClickListener {
            // Aqui você chamaria a lógica de logout do seu ViewModel
            // viewModel.logout()

            // Exemplo de como chamar o logout diretamente (sem ViewModel para simplificar)
            // FirebaseAuth.getInstance().signOut()

            navigateToAuthActivity()
            true // Retorna true para indicar que o clique foi tratado
        }
    }

    private fun navigateToAuthActivity() {
        // Cria um Intent para a AuthActivity e limpa o histórico de navegação
        val intent = Intent(requireActivity(), AuthActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        requireActivity().finish()
    }
}
 */