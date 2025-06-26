package com.example.mileagetracker.ui.screens.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mileagetracker.data.Summary
import com.example.mileagetracker.ui.screens.nav_menus.NavMenusScreen
import com.example.mileagetracker.ui.screens.summary.SummaryScreen
import com.example.mileagetracker.ui.screens.tracker.TrackerScreen
import com.example.mileagetracker.ui.theme.MileageTrackerTheme
import com.example.mileagetracker.utils.annotations.HorizontalScreenPreview
import com.example.mileagetracker.utils.annotations.VerticalScreenPreview
import com.example.mileagetracker.utils.screen_state.ScreenState
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContent {
            MileageTrackerTheme {
                Surface {
                    MileageTrackerApp()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MileageTrackerApp(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val viewModel: MainViewModel = hiltViewModel()
    val screenState by viewModel.screenState.collectAsState()

    when (val state = screenState) {
        is ScreenState.Error -> {
            // TODO: error screen
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.systemBars),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = state.message ?: "Something went wrong",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        is ScreenState.Loaded -> {
            NavHost(
                navController = navController,
                startDestination = Screen.NavMenu.name,
                modifier = Modifier.fillMaxSize()
            ) {
                composable(Screen.NavMenu.name) {
                    NavMenusScreen(
                        onStart = { journeyName ->
                            navController.navigate("${Screen.Tracker.name}/$journeyName")
                        },
                        modifier = modifier
                    )
                }

                composable(
                    route = "${Screen.Tracker.name}/{journeyName}",
                    arguments = listOf(navArgument("journeyName") {
                        defaultValue = "Untitled Journey"
                    })
                ) { backStackEntry ->
                    val journeyText: String =
                        backStackEntry.arguments?.getString("journeyName")
                            .takeIf { !(it.isNullOrBlank()) }
                            ?: "Untitled"
                    TrackerScreen(
                        journeyText = journeyText,
                        viewModel = hiltViewModel(),
                        goToSummaryScreen = { summary ->
                            val summaryString = Gson().toJson(summary)
                            navController.navigate("${Screen.Summary.name}/$summaryString")
                        },
                        modifier = modifier
                    )
                }

                composable(
                    route = "${Screen.Summary.name}/{summaryJson}",
                    arguments = listOf(
                        navArgument(
                            "summaryJson"
                        ) {})
                ) { backStackEntry ->
                    val summary: Summary? = Gson().fromJson(
                        backStackEntry.arguments?.getString("summaryJson"),
                        Summary::class.java
                    )
                    SummaryScreen(summary = summary, modifier = modifier)
                }
            }
        }

        is ScreenState.Loading -> {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.systemBars),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is ScreenState.PreLoad -> {}
    }
}

@VerticalScreenPreview
@Composable
fun MileageTrackerVertical() {
    MileageTrackerApp()
}

@HorizontalScreenPreview
@Composable
fun MileageTrackerHorizontal() {
    MileageTrackerApp()
}

enum class Screen {
    NavMenu, Tracker, Summary
}