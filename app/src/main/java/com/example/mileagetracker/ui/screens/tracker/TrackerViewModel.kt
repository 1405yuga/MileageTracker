package com.example.mileagetracker.ui.screens.tracker

import android.content.Context
import android.content.Intent
import android.location.Location
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mileagetracker.data.JourneyData
import com.example.mileagetracker.data.PointsData
import com.example.mileagetracker.data.Summary
import com.example.mileagetracker.network.repositoy.JourneyRepository
import com.example.mileagetracker.network.repositoy.PointsRepository
import com.example.mileagetracker.utils.services.ForegroundTrackingService
import com.example.mileagetracker.utils.shared.LocationDataManager
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrackerViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    private val journeyRepository: JourneyRepository,
    private val pointsRepository: PointsRepository
) : ViewModel() {
    private val _localPoints = MutableStateFlow<List<LatLng>>(emptyList())
    val localPoints: StateFlow<List<LatLng>> = _localPoints

    private val _isTracking = MutableStateFlow(false)
    val isTracking: StateFlow<Boolean> = _isTracking

    private var startTime: Long = 0L
    private var endTime: Long = 0L
    lateinit var summary: Summary
    private var journeyId: Long? = null

    private var elapsedJob: Job? = null

    fun startJourney(title: String) {
        startTime = System.currentTimeMillis()
        _isTracking.value = true
        startForegroundService()
        addJourney(title = title, startTime)
    }

    fun stopJourney(title: String, onComplete: (Summary) -> Unit) {
        viewModelScope.launch {
            if (journeyId != null) {
                endTime = System.currentTimeMillis()
                journeyRepository.updateEndTime(journeyId = journeyId!!, endTime = endTime)
                summary = Summary(
                    id = journeyId!!,
                    title = title,
                    points = _localPoints.value,
                    distanceInMeters = calculateDistance(),
                    startTime = startTime,
                    endTime = endTime
                )
                stopForegroundService()
                _isTracking.value = false
                _localPoints.value = emptyList()
                elapsedJob?.cancel()
                elapsedJob = null
                onComplete(summary)
            } else {
                Log.d(this.javaClass.simpleName, "Journey id null")
                // TODO: give error
            }
        }
    }

    private fun startForegroundService() {
        val intent = Intent(context, ForegroundTrackingService::class.java)
        intent.action = "ACTION_START"
        ContextCompat.startForegroundService(context, intent)
    }

    private fun stopForegroundService() {
        val intent = Intent(context, ForegroundTrackingService::class.java)
        intent.action = "ACTION_STOP"
        ContextCompat.startForegroundService(context, intent)
    }

    fun onNewLocation(location: Location) {
        if (journeyId != null) {
            viewModelScope.launch {
                val updated = _localPoints.value.toMutableList()
                pointsRepository.insertPointData(
                    point = PointsData(
                        journeyId = journeyId!!,
                        latitude = location.latitude,
                        longitude = location.longitude
                    )
                )
                updated.add(LatLng(location.latitude, location.longitude))
                _localPoints.value = updated
            }
        } else {
            Log.d(this.javaClass.simpleName, "Journey id null")
            // TODO: give error
        }
    }

    fun calculateDistance(): Float {
        if (_localPoints.value.size < 2) return 0f
        var total = 0f
        for (i in 0 until _localPoints.value.size - 1) {
            val loc1 = Location("").apply {
                latitude = _localPoints.value[i].latitude
                longitude = _localPoints.value[i].longitude
            }

            val loc2 = Location("").apply {
                latitude = _localPoints.value[i + 1].latitude
                longitude = _localPoints.value[i + 1].longitude
            }

            total += loc1.distanceTo(loc2)

        }
        return total
    }

    fun addJourney(title: String, startTime: Long) {
        viewModelScope.launch {
            journeyRepository.insertJourney(
                journey = JourneyData(
                    title = title,
                    startTime = startTime
                ),
                onSuccess = { id ->
                    // TODO: add in static list
                    journeyId = id
                },
                onFailure = {}
            )
        }
    }

    init {
        viewModelScope.launch {
            LocationDataManager.location
                .filterNotNull()
                .collectLatest { location ->
                    onNewLocation(location)
                }
        }
    }
}