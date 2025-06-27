package com.example.mileagetracker.utils.services

import android.Manifest
import android.app.Service
import android.content.Intent
import android.location.Location
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresPermission
import com.example.mileagetracker.data.CurrentJourney
import com.example.mileagetracker.data.PointsData
import com.example.mileagetracker.network.repositoy.PointsRepository
import com.example.mileagetracker.network.shared_prefs.CurrentJourneyPrefsManager
import com.example.mileagetracker.utils.notification.NotificationHelper
import com.example.mileagetracker.utils.shared.LocationDataManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ForegroundTrackingService : Service() {

    @Inject
    lateinit var pointsRepository: PointsRepository

    @Inject
    lateinit var currentJourneyPrefsManager: CurrentJourneyPrefsManager
    private lateinit var fusedClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private var currentJourney: CurrentJourney? = null

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int
    ): Int {

        currentJourney = currentJourneyPrefsManager.getJourney()
        when (intent?.action) {
            "ACTION_START" -> startTracking()
            "ACTION_STOP" -> {
                stopTracking()
                stopSelf()
            }
        }
        return START_STICKY
    }

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    private fun startTracking() {
        currentJourney?.let { currentJourney ->
            Log.d(this.javaClass.name, "Foreground service called..")
            Log.d(this.javaClass.name, "Current journey : $currentJourney")
            startForeground(1, NotificationHelper.createNotification(this))
            Log.d(this.javaClass.name, "Foreground service started..")
            fusedClient = LocationServices.getFusedLocationProviderClient(this)
            val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000)
                .setMinUpdateDistanceMeters(10f)
                .build()

            locationCallback = object : LocationCallback() {
                override fun onLocationResult(p0: LocationResult) {
                    p0.lastLocation?.let {
                        sendLocationToViewModel(currentJourney.id, it)
                    }
                }
            }

            fusedClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        }
    }

    private fun stopTracking() {
        try {
            currentJourneyPrefsManager.clearJourney()
            fusedClient.removeLocationUpdates(locationCallback)
            Log.d(this.javaClass.name, "Removed location callback")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun sendLocationToViewModel(journeyId: Long, location: Location) {
        Log.d(this.javaClass.simpleName, "Recieved point : $location")
        CoroutineScope(Dispatchers.IO).launch {
            Log.d(this.javaClass.simpleName, "Current id : $journeyId")
            pointsRepository.insertPointData(
                PointsData(
                    journeyId = journeyId,
                    latitude = location.latitude,
                    longitude = location.longitude
                )
            )
            LocationDataManager.updateLocation(location = location)
        }
    }

    override fun onBind(p0: Intent?): IBinder? = null
}