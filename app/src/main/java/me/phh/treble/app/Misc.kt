package me.phh.treble.app

import android.content.Context
import android.content.SharedPreferences
import android.os.ServiceManager
import android.os.SystemProperties
import android.util.Log
import androidx.preference.PreferenceManager

import vendor.ims.zenmotion.V1_0.IZenMotion;

import vendor.mediatek.hardware.agolddaemon.IAgoldDaemon

object Misc: EntryStartup {
    val spListener = SharedPreferences.OnSharedPreferenceChangeListener { sp, key ->
        when(key) {
            MiscSettings.biometricstrong -> {
                val value = sp.getBoolean(key, false)
                SystemProperties.set("persist.sys.phh.biometricstrong", if (value) "true" else "false")
            }
            MiscSettings.treatVirtualSensorsAsReal -> {
                val value = sp.getBoolean(key, false)
                SystemProperties.set("persist.sys.phh.virtual_sensors_are_real", if (value) "1" else "0")
            }
            MiscSettings.launcher3 -> {
                val value = sp.getBoolean(key, false)
                SystemProperties.set("persist.sys.phh.launcher3", if (value) "true" else "false")
            }
            MiscSettings.disableSaeUpgrade -> {
                val value = sp.getBoolean(key, false)
                SystemProperties.set("persist.sys.phh.wifi_disable_sae", if (value) "true" else "false")
            }
            MiscSettings.storageFUSE -> {
                val value = sp.getBoolean(key, false)
                Log.d("PHH", "Setting storageFUSE to $value")
                SystemProperties.set("persist.sys.fflag.override.settings_fuse", if (!value) "true" else "false")
            }
            MiscSettings.disableDisplayDozeSuspend -> {
                val value = sp.getBoolean(key, true)
                SystemProperties.set("persist.sys.phh.disable_display_doze_suspend", if (value) "true" else "false")
            }
            MiscSettings.disableExpensiveRenderingMode -> {
                val value = sp.getBoolean(key, false)
                SystemProperties.set("persist.sys.phh.disable_expensive_rendering_mode", if (value) "1" else "0")
            }
            MiscSettings.securize -> {
                val value = sp.getBoolean(key, false)
                SystemProperties.set("persist.sys.phh.securize", if (value) "1" else "0")
            }
            MiscSettings.unihertzdt2w -> {
                val value = sp.getBoolean(key, false)
                try {
                    val binder = android.os.Binder.allowBlocking(
                        ServiceManager.waitForDeclaredService(IAgoldDaemon.DESCRIPTOR + "/default"));
                    val instance = IAgoldDaemon.Stub.asInterface(binder);
                    val ret = instance.SendMessageToIoctl(100, 0, if(value) 1 else 0, if(value) 1 else 0)
                    Log.d("PHH", "Setting agold touch mode returned $ret")
                } catch(t: Throwable) {
                    Log.d("PHH", "Setting agold touch mode failed", t)
                }
            }
            MiscSettings.dt2w -> {
                // Let's try all known dt2w
                val value = sp.getBoolean(key, false)
                val asusSvc = try { IZenMotion.getService() } catch(e: Exception) { null }
                if(asusSvc != null) {
                    asusSvc.setDclickEnable(if(value) 1 else 0)
                }
            }
        }
    }

    override fun startup(ctxt: Context) {
        Log.d("PHH", "Loading Misc fragment")

        val sp = PreferenceManager.getDefaultSharedPreferences(ctxt)
        sp.registerOnSharedPreferenceChangeListener(spListener)

        // Refresh parameters on boot
        spListener.onSharedPreferenceChanged(sp, MiscSettings.storageFUSE)
        spListener.onSharedPreferenceChanged(sp, MiscSettings.unihertzdt2w)
        spListener.onSharedPreferenceChanged(sp, MiscSettings.dt2w)
    }
}
