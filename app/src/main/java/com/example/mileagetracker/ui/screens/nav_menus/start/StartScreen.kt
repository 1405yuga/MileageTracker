package com.example.mileagetracker.ui.screens.nav_menus.start

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.example.mileagetracker.utils.annotations.HorizontalScreenPreview
import com.example.mileagetracker.utils.annotations.VerticalScreenPreview

@Composable
fun StartScreen(onStart: () -> Unit, modifier: Modifier = Modifier) {
    val context = LocalContext.current

    val permissionsToRequest = mutableListOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.FOREGROUND_SERVICE_LOCATION,
        Manifest.permission.FOREGROUND_SERVICE,
        Manifest.permission.POST_NOTIFICATIONS
    )

    val permissionLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestMultiplePermissions()) { permissionResult ->
            val allGranted = permissionsToRequest.all { permissionResult[it] == true }
            if (allGranted) {
                onStart()
            } else {
                Toast.makeText(context, "Please grant all permissions", Toast.LENGTH_SHORT).show()
            }
        }

    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Button(onClick = {
            val allGranted = permissionsToRequest.all {
                ContextCompat.checkSelfPermission(
                    context,
                    it
                ) == PackageManager.PERMISSION_GRANTED
            }

            if (allGranted) {
                onStart()
            } else {
                permissionLauncher.launch(permissionsToRequest.toTypedArray())
            }
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