package com.example.mileagetracker.utils.composable

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MapScreen(points: List<LatLng>) {
    val cameraPositionState = rememberCameraPositionState()

    LaunchedEffect(points.lastOrNull()) {
        points.lastOrNull()?.let { latestLoc ->
            cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(latestLoc, 24f))

        }
    }
    GoogleMap(
        modifier = Modifier.fillMaxWidth(),
        cameraPositionState = cameraPositionState,
        properties = MapProperties(isMyLocationEnabled = true),
        uiSettings = MapUiSettings(zoomControlsEnabled = true)
    ) {
        if (points.size > 1) {
            Polyline(points = points, color = Color.Blue, width = 5f)
        }
    }
}