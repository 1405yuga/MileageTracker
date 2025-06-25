package com.example.mileagetracker.ui.screens.tracker

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrackerViewModel @Inject constructor(@ApplicationContext context: Context) : ViewModel() {
    private val _localPoints = MutableStateFlow<List<LatLng>>(emptyList())
    val localPoints: StateFlow<List<LatLng>> = _localPoints

    private val _isTracking = MutableStateFlow(false)
    val isTracking: StateFlow<Boolean> = _isTracking

    private var startTime: Long = 0L
    private val _elapsedTime = MutableStateFlow(0L)
    val elapsedTime: StateFlow<Long> = _elapsedTime

    fun startJourney() {
        startTime = System.currentTimeMillis()
        _isTracking.value = true
        // TODO: start foreground
        startElapsedCounter()
    }

    fun stopJourney() {
        // TODO: stop foreground
        _isTracking.value = false
        _localPoints.value = emptyList()
        _elapsedTime.value = 0L
    }

    private fun startElapsedCounter() {
        viewModelScope.launch {
            while (_isTracking.value == true) {
                delay(1000L)
                _elapsedTime.value = (System.currentTimeMillis() - startTime)
            }
        }
    }

    init {
        startJourney()
    }
}