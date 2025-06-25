package com.example.mileagetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mileagetracker.ui.screens.history.HistoryScreen
import com.example.mileagetracker.ui.screens.tracker.TrackerScreen
import com.example.mileagetracker.ui.theme.MileageTrackerTheme
import com.example.mileagetracker.utils.annotations.HorizontalScreenPreview
import com.example.mileagetracker.utils.annotations.VerticalScreenPreview
import kotlinx.coroutines.launch

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
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Open)
    val scope = rememberCoroutineScope()
    val navController = rememberNavController()
    val menuItems = listOf(Screen.Tracker, Screen.History)
    var selectedMenu by rememberSaveable { mutableStateOf(Screen.Tracker) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text(
                    stringResource(R.string.app_name),
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                    modifier = Modifier.padding(12.dp)
                )
                HorizontalDivider()
                Spacer(modifier = Modifier.height(24.dp))
                menuItems.forEach { item ->
                    NavigationDrawerItem(
                        label = { Text(item.name) },
                        selected = item == selectedMenu,
                        onClick = {
                            navController.navigate(item.name) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                            selectedMenu = item
                            scope.launch { drawerState.close() }
                        },
                        icon = { Icon(item.icon, contentDescription = null) }
                    )
                }
            }
        }) {
        Scaffold(topBar = {
            TopAppBar(
                title = { Text(selectedMenu.name) },
                navigationIcon = {
                    IconButton(onClick = { scope.launch { drawerState.open() } }) {
                        Icon(selectedMenu.icon, contentDescription = "Menu")
                    }
                })
        }) { padding ->
            NavHost(
                navController = navController,
                startDestination = Screen.Tracker.name,
                modifier = modifier.padding(padding)
            ) {
                composable(Screen.Tracker.name) {
                    TrackerScreen(modifier = modifier)
                }
                composable(Screen.History.name) {
                    HistoryScreen(modifier = modifier)
                }
            }

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

enum class Screen(val icon: ImageVector) {
    Tracker(icon = Icons.Default.Star), History(icon = Icons.Default.Refresh)
}