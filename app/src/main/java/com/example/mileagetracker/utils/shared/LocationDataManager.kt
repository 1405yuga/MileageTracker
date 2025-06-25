package com.example.mileagetracker.utils.shared

import android.location.Location
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Singleton

@Singleton
object LocationDataManager {
    private val _location = MutableStateFlow<Location?>(null)
    val location: StateFlow<Location?> = _location

    fun updateLocation(location: Location) {
        _location.value = location
    }
}