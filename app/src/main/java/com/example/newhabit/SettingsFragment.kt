package com.example.newhabit

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.example.newhabit.data.local.datastore.SettingsDataStore

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.preferenceDataStore = SettingsDataStore(requireContext())
        setPreferencesFromResource(R.xml.settings_preferences, rootKey)
    }
}