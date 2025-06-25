package com.example.mileagetracker.ui.screens.tracker

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun TrackerScreen(
    journeyText: String,
    viewModel: TrackerViewModel,
    modifier: Modifier = Modifier
) {
    val elapsedTime by viewModel.elapsedTime.collectAsState()
    val localPoints by viewModel.localPoints.collectAsState()
    val isTracking by viewModel.isTracking.collectAsState()

    Column(modifier = modifier.fillMaxSize()) {
        Text(journeyText, fontWeight = FontWeight.Bold)
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
        MapScreen(points = localPoints)
    }
}

@Composable
fun MapScreen(points: List<LatLng>) {
    val cameraPositionState = rememberCameraPositionState()

    LaunchedEffect(points.lastOrNull()) {
        points.lastOrNull()?.let { latestLoc ->
            cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(latestLoc, 24f))

        }
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