package com.example.mileagetracker.ui.screens.tracker

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
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
    goToSummaryScreen: (summaryId: Long?) -> Unit,
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

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Card(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            MapScreen(points = localPoints)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = journeyText,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            Text(
                text = "Tracking ${localPoints.size} points",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = {
                    viewModel.startJourney(
                        title = journeyText,
                        onComplete = { summary -> mainViewModel.addNewToList(summary) }
                    )
                },
                enabled = !isTracking,
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Start",
                    modifier = Modifier.padding(end = 4.dp)
                )
                Text("Start")
            }

            Button(
                onClick = {
                    viewModel.stopJourney { summaryId, endTime ->
                        mainViewModel.updateEndTime(summaryId, endTime)
                        goToSummaryScreen(summaryId)
                    }
                },
                enabled = isTracking,
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Default.Stop,
                    contentDescription = "Stop",
                    modifier = Modifier.padding(end = 4.dp)
                )
                Text("Stop")
            }
        }
    }
}

//@Composable
//@VerticalScreenPreview
//fun TrackerScreenVertical() {
//    val context = LocalContext.current
//
//    TrackerScreen(
//        journeyText = "Untitle journey",
//        viewModel = TrackerViewModel(
//            context = context,
//            journeyRepository = JourneyRepository.mock,
//            pointsRepository = PointsRepository.mock
//        ),
//        mainViewModel = MainViewModel(
//            journeyRepository = JourneyRepository.mock,
//            pointsRepository = PointsRepository.mock
//        ),
//        goToSummaryScreen = {},
//    )
//}