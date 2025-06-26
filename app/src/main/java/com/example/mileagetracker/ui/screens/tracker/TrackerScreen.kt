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
import androidx.compose.ui.text.font.FontWeight
import com.example.mileagetracker.data.Summary
import com.example.mileagetracker.ui.screens.main.MainViewModel
import com.example.mileagetracker.utils.helper.MapScreen
import com.example.mileagetracker.utils.shared.LocationDataManager
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull

@Composable
fun TrackerScreen(
    journeyText: String,
    viewModel: TrackerViewModel,
    mainViewModel: MainViewModel,
    goToSummaryScreen: (summary: Summary) -> Unit,
    modifier: Modifier = Modifier
) {
    val localPoints by viewModel.localPoints.collectAsState()
    val isTracking by viewModel.isTracking.collectAsState()
    LaunchedEffect(Unit) {
        LocationDataManager.location
            .filterNotNull()
            .collectLatest { location ->
                viewModel.onNewLocation(location = location, onSuccess = { journeyId ->
                    mainViewModel.updatePointsInSummaryList(
                        summaryId = journeyId,
                        point = LatLng(location.latitude, location.longitude)
                    )
                })
            }
    }

    Column(modifier = modifier.fillMaxSize()) {
        Text(journeyText, fontWeight = FontWeight.Bold)
        Text(text = "Local points : $localPoints")
        Row {
            Button(
                onClick = {
                    viewModel.startJourney(
                        title = journeyText,
                        onComplete = { summary -> mainViewModel.addNewToList(summary = summary) },
                    )
                },
                enabled = !isTracking
            ) {
                Text(text = "Start Journey")
            }

            Button(onClick = {
                viewModel.stopJourney(title = journeyText, onComplete = { summary ->
                    mainViewModel.updateEndTime(
                        summaryId = summary.id,
                        endTime = summary.endTime
                    )
                    goToSummaryScreen(summary)
                })
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