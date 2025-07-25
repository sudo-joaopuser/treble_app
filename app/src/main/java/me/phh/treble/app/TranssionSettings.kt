package me.phh.treble.app

import android.app.Fragment
import android.content.Context
import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceFragment
import android.util.Log
import android.view.View
import android.widget.ListView

object TranssionSettings : Settings {
    val usbOtg = "key_transsion_usb_otg"
    val dt2w = "key_transsion_dt2w"

    override fun enabled(context: Context): Boolean {
        val isTranssion = Tools.vendorFp.startsWith("Infinix/") ||
        Tools.vendorFp.startsWith("TECNO/") ||
        Tools.vendorFp.startsWith("Itel/")
        Log.d("PHH", "TranssionSettings enabled() called, isTranssion = $isTranssion")
        return isTranssion
    }
}

class TranssionSettingsFragment : PreferenceFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.pref_transsion)

        if (TranssionSettings.enabled(context)) {
            Log.d("PHH", "Loading Transsion fragment ${TranssionSettings.enabled(context)}")

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
