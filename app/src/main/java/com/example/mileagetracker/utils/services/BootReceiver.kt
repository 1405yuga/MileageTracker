package com.example.mileagetracker.utils.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.example.mileagetracker.network.shared_prefs.CurrentJourneyPrefsManager

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        p1?.let {
            p0?.let {
                if (p1.action == Intent.ACTION_BOOT_COMPLETED) {
                    val sharedPref =
                        p0.getSharedPreferences("current_journey_pref", Context.MODE_PRIVATE)
                    val currentJourney =
                        CurrentJourneyPrefsManager(sharedPreferences = sharedPref).getJourney()
                    if (currentJourney?.isActive == true) {
                        val intent = Intent(p0, ForegroundTrackingService::class.java)
                        intent.action = "ACTION_START"
                        ContextCompat.startForegroundService(p0 , intent)
                    }
                }
            }
        }
    }
}