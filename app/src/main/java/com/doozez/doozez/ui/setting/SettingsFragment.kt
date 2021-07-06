package com.doozez.doozez.ui.setting

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.doozez.doozez.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }
}