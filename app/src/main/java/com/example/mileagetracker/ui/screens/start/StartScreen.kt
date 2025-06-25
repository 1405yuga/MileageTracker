package com.example.mileagetracker.ui.screens.start

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.mileagetracker.utils.annotations.HorizontalScreenPreview
import com.example.mileagetracker.utils.annotations.VerticalScreenPreview

@Composable
fun StartScreen(onStart: () -> Unit, modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Button(onClick = {
// TODO: check location & internet permissions
            onStart()
        }) {
            Text(text = "Start Journey")
        }
    }
}

@VerticalScreenPreview
@Composable
fun TrackerScreenVertical() {
    StartScreen(onStart = {})
}

@HorizontalScreenPreview
@Composable
fun TrackerScreenHorizontal() {
    StartScreen(onStart = {})
}