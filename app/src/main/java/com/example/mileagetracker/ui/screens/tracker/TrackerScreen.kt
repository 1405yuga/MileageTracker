package com.example.mileagetracker.ui.screens.tracker

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier

@Composable
fun TrackerScreen(viewModel: TrackerViewModel, modifier: Modifier = Modifier) {
    val elapsedTime by viewModel.elapsedTime.collectAsState()
    Column {
        Text(text = "Elapsed time : $elapsedTime")
        Button(onClick = { viewModel.stopJourney() }) { Text(text = "Stop Journey") }
    }
}