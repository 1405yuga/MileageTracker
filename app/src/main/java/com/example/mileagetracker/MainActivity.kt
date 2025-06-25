package com.example.mileagetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
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
    NavHost(
        navController = navController,
        startDestination = Screen.NavMenu.name,
        modifier = modifier.windowInsetsPadding(WindowInsets.systemBars)
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
            arguments = listOf(navArgument("journeyName") { defaultValue = "Untitled Journey" })
        ) { backStackEntry ->
            val journeyText: String =
                backStackEntry.arguments?.getString("journeyName").takeIf { !(it.isNullOrBlank()) }
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