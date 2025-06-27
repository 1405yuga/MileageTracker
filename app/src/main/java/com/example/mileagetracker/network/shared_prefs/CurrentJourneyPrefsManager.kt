package com.example.mileagetracker.network.shared_prefs

import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.mileagetracker.data.CurrentJourney
import com.google.gson.Gson
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrentJourneyPrefsManager @Inject constructor(private val sharedPreferences: SharedPreferences) {

    private val KEY_ACTIVE_JOURNEY = "active_journey"
    fun saveJourney(currentJourney: CurrentJourney) {
        sharedPreferences.edit {
            putString(KEY_ACTIVE_JOURNEY, Gson().toJson(currentJourney))
        }
    }

    fun getJourney(): CurrentJourney? {
        val json = sharedPreferences.getString(KEY_ACTIVE_JOURNEY, null)
        return json?.let { Gson().fromJson(it, CurrentJourney::class.java) }
    }

    fun clearJourney() {
        sharedPreferences.edit { remove(KEY_ACTIVE_JOURNEY) }
    }
}