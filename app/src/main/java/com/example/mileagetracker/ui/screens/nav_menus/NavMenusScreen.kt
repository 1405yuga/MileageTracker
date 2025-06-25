package com.example.mileagetracker.ui.screens.nav_menus

import androidx.compose.foundation.layout.Box
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
import com.example.mileagetracker.R
import com.example.mileagetracker.ui.screens.history.HistoryScreen
import com.example.mileagetracker.ui.screens.start.StartScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavMenusScreen(onStart: () -> Unit, modifier: Modifier = Modifier) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Open)
    val scope = rememberCoroutineScope()
    val menuItems = listOf(NavMenusScreen.Start, NavMenusScreen.History)
    var selectedMenu by rememberSaveable { mutableStateOf(NavMenusScreen.Start) }

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
            Box(modifier = modifier.padding(padding)) {
                when (selectedMenu) {
                    NavMenusScreen.Start -> {
                        StartScreen(onStart = { onStart() }, modifier = modifier)
                    }

                    NavMenusScreen.History -> {
                        HistoryScreen(modifier = modifier)
                    }
                }
            }

        }
    }

}

enum class NavMenusScreen(val icon: ImageVector) {
    Start(icon = Icons.Default.Star), History(icon = Icons.Default.Refresh),
}