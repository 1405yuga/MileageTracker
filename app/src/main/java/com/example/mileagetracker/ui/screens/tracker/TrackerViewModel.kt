package com.example.mileagetracker.ui.screens.tracker

import android.content.Context
import android.content.Intent
import android.location.Location
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mileagetracker.data.CurrentJourney
import com.example.mileagetracker.data.JourneyData
import com.example.mileagetracker.data.PointsData
import com.example.mileagetracker.data.Summary
import com.example.mileagetracker.network.repositoy.JourneyRepository
import com.example.mileagetracker.network.repositoy.PointsRepository
import com.example.mileagetracker.network.shared_prefs.CurrentJourneyPrefsManager
import com.example.mileagetracker.utils.services.ForegroundTrackingService
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrackerViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    private val journeyRepository: JourneyRepository,
    private val pointsRepository: PointsRepository,
    private val currentJourneyPrefsManager: CurrentJourneyPrefsManager
) : ViewModel() {
    private val _localPoints = MutableStateFlow<List<LatLng>>(emptyList())
    val localPoints: StateFlow<List<LatLng>> = _localPoints

    private val _isTracking = MutableStateFlow(false)
    val isTracking: StateFlow<Boolean> = _isTracking

    private var startTime: Long = 0L
    private var endTime: Long = 0L
    private var journeyId: Long? = null

    private var elapsedJob: Job? = null

    fun startJourney(title: String, onComplete: (Summary) -> Unit) {
        startTime = System.currentTimeMillis()
        _isTracking.value = true
        startForegroundService()
        addJourney(title = title, startTime, onSuccess = { id ->
            currentJourneyPrefsManager.saveJourney(
                currentJourney =
                    CurrentJourney(
                        isActive = true,
                        id = id,
                        title = title,
                        startTime = startTime
                    )
            )
            onComplete(
                Summary(
                    id = id,
                    title = title,
                    points = emptyList(),
                    distanceInMeters = 0.0f,
                    startTime = startTime,
                    endTime = null
                )
            )
        })
    }

    fun stopJourney(onComplete: (summaryId: Long, endTime: Long) -> Unit) {
        viewModelScope.launch {
            if (journeyId != null) {
                endTime = System.currentTimeMillis()
                journeyRepository.updateEndTime(journeyId = journeyId!!, endTime = endTime)
                stopForegroundService()
                _isTracking.value = false
                _localPoints.value = emptyList()
                elapsedJob?.cancel()
                elapsedJob = null
                onComplete(journeyId!!, endTime)
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

    fun onNewLocation(location: Location, onSuccess: (Long) -> Unit) {
        if (journeyId != null) {
            viewModelScope.launch {
                val updated = _localPoints.value.toMutableList()
//                pointsRepository.insertPointData(
//                    point = PointsData(
//                        journeyId = journeyId!!,
//                        latitude = location.latitude,
//                        longitude = location.longitude
//                    )
//                )
                updated.add(LatLng(location.latitude, location.longitude))
                _localPoints.value = updated
                onSuccess(journeyId!!)
            }
        } else {
            Log.d(this.javaClass.simpleName, "Journey id null")
            // TODO: give error
        }
    }

    fun addJourney(title: String, startTime: Long, onSuccess: (journeyId: Long) -> Unit) {
        viewModelScope.launch {
            journeyRepository.insertJourney(
                journey = JourneyData(
                    title = title,
                    startTime = startTime
                ),
                onSuccess = { id ->
                    journeyId = id
                    onSuccess(id)
                },
                onFailure = {}
            )
        }
    }
}