package com.example.mileagetracker.ui.screens.tracker

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun TrackerScreen(viewModel: TrackerViewModel, modifier: Modifier = Modifier) {
    val elapsedTime by viewModel.elapsedTime.collectAsState()
    val localPoints by viewModel.localPoints.collectAsState()
    val isTracking by viewModel.isTracking.collectAsState()

    Column(modifier = modifier.fillMaxSize()) {
        Text(text = "Elapsed time : $elapsedTime")
        Text(text = "Local points : $localPoints")
        Row {
            Button(onClick = { viewModel.startJourney() }, enabled = !isTracking) {
                Text(text = "Start Journey")
            }

            Button(onClick = { viewModel.stopJourney() }, enabled = isTracking) {
                Text(text = "Stop Journey")
            }
        }
        val sampleLatLngList = listOf(
            LatLng(19.0760, 72.8777), // Mumbai - Start Point
            LatLng(19.0765, 72.8782),
            LatLng(19.0770, 72.8790),
            LatLng(19.0775, 72.8798),
            LatLng(19.0780, 72.8805), // Mid Point
            LatLng(19.0785, 72.8810),
            LatLng(19.0790, 72.8818),
            LatLng(19.0795, 72.8825),
            LatLng(19.0800, 72.8830)  // End Point
        )
        MapScreen(points = sampleLatLngList) // TODO: replace with localpoints 
    }
}

@Composable
fun MapScreen(points: List<LatLng>) {
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(points.lastOrNull() ?: LatLng(0.0, 0.0), 16f)
    }
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        properties = MapProperties(isMyLocationEnabled = true),
        uiSettings = MapUiSettings(zoomControlsEnabled = true)
    ) {
        if (points.size > 1) {
            Polyline(points = points, color = Color.Blue, width = 5f)
        }
    }
}