package com.example.mileagetracker.ui.screens.summary

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mileagetracker.data.Summary
import com.example.mileagetracker.utils.annotations.HorizontalScreenPreview
import com.example.mileagetracker.utils.annotations.VerticalScreenPreview
import com.example.mileagetracker.utils.extension_functions.toFormattedTimeString
import com.example.mileagetracker.utils.helper.MapScreen

@Composable
fun SummaryScreen(summary: Summary?, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        if (summary != null) {
            Text(
                text = summary.title.ifBlank { "Untitled" },
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Text(
                text = "Route Overview",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                MapScreen(points = summary.points)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Trip Summary",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text("Start Time: ${summary.startTime.toFormattedTimeString()}")
                summary.endTime?.let { Text("End Time: ${summary.endTime.toFormattedTimeString()}") }
                Text("Total Distance: ${summary.distanceInMeters} m")
                Text("Total Points: ${summary.points.size}")
            }
        } else {
            Text("No Data found")
        }
    }
}

@Composable
@VerticalScreenPreview
fun SummaryScreenVertical() {
    SummaryScreen(summary = Summary.mock)
}

@Composable
@HorizontalScreenPreview
fun HorizontalScreenPreview() {
    SummaryScreen(summary = Summary.mock)
}