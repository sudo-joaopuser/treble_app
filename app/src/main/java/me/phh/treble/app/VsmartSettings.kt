package me.phh.treble.app

import android.app.Fragment
import android.content.Context
import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceFragment
import android.util.Log
import android.view.View
import android.widget.ListView

object VsmartSettings : Settings {
    val dt2w = "key_vsmart_dt2w"

    override fun enabled(context: Context): Boolean {
        val isVsmart = Tools.vendorFp.startsWith("vsmart/")
        Log.d("PHH", "VsmartSettings enabled() called, isVsmart = $isVsmart")
        return isVsmart
    }
}

class VsmartSettingsFragment : PreferenceFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.pref_vsmart)

        if (VsmartSettings.enabled(context)) {
            Log.d("PHH", "Loading Vsmart fragment ${VsmartSettings.enabled(context)}")

            // Example of how to bind preference if needed:
            findPreference(VsmartSettings.dt2w)?.let {
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Apply consistent visual settings
        val listView = view.findViewById<ListView>(android.R.id.list)
        listView?.apply {
            divider = null
            dividerHeight = 0
            clipToPadding = true
            setPadding(32, paddingTop, 32, paddingBottom)
        }
    }
}
