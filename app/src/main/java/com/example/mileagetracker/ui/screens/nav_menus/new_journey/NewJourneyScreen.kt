package com.example.mileagetracker.ui.screens.nav_menus.new_journey

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.mileagetracker.utils.annotations.HorizontalScreenPreview
import com.example.mileagetracker.utils.annotations.VerticalScreenPreview

@Composable
fun NewJourneyScreen(onStart: (String) -> Unit, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var journeyText by rememberSaveable { mutableStateOf("") }

    val permissionsToRequest = mutableListOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.FOREGROUND_SERVICE_LOCATION,
        Manifest.permission.FOREGROUND_SERVICE,
        Manifest.permission.POST_NOTIFICATIONS,
        Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
    )

    val permissionLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestMultiplePermissions()) { permissionResult ->
            val allGranted = permissionsToRequest.all { permissionResult[it] == true }
            if (allGranted) {
                onStart(journeyText)
            } else {
                Toast.makeText(context, "Please grant all permissions", Toast.LENGTH_SHORT).show()
            }
        }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Start New Journey",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        OutlinedTextField(
            value = journeyText,
            onValueChange = { journeyText = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            singleLine = true,
            label = { Text("Journey Name") },
            placeholder = { Text("e.g. Morning Walk") }
        )

        Button(
            onClick = {
                val allGranted = permissionsToRequest.all {
                    ContextCompat.checkSelfPermission(
                        context,
                        it
                    ) == PackageManager.PERMISSION_GRANTED
                }

                if (allGranted) {
                    onStart(journeyText.trim())
                } else {
                    permissionLauncher.launch(permissionsToRequest.toTypedArray())
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            enabled = journeyText.trim().isNotEmpty()
        ) {
            Text("Get Started")
        }
    }
}

@VerticalScreenPreview
@Composable
fun NewJourneyScreenVertical() {
    NewJourneyScreen(onStart = {})
}

@HorizontalScreenPreview
@Composable
fun NewJourneyScreenHorizontal() {
    NewJourneyScreen(onStart = {})
}