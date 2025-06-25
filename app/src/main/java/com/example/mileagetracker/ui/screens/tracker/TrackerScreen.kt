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
import androidx.compose.ui.text.font.FontWeight
import com.example.mileagetracker.data.Summary
import com.example.mileagetracker.utils.composable.MapScreen

@Composable
fun TrackerScreen(
    journeyText: String,
    viewModel: TrackerViewModel,
    goToSummaryScreen: (summary: Summary) -> Unit,
    modifier: Modifier = Modifier
) {
    val elapsedTime by viewModel.elapsedTime.collectAsState()
    val localPoints by viewModel.localPoints.collectAsState()
    val isTracking by viewModel.isTracking.collectAsState()

    Column(modifier = modifier.fillMaxSize()) {
        Text(journeyText, fontWeight = FontWeight.Bold)
        Text(text = "Local points : $localPoints")
        Row {
            Button(onClick = { viewModel.startJourney() }, enabled = !isTracking) {
                Text(text = "Start Journey")
            }

            Button(onClick = {
                viewModel.stopJourney(title = journeyText)
                goToSummaryScreen(viewModel.summary)
            }, enabled = isTracking) {
                Text(text = "Stop Journey")
            }
        }
        MapScreen(points = localPoints)
    }
}

//@Composable
//@VerticalScreenPreview
//fun TrackerScreenVertical(){
//    TrackerScreen(
//        journeyText = "Untitle journey",
//        viewModel = TrackerViewModel(),
//    )
//}