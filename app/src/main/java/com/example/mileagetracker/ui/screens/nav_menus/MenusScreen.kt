package com.example.mileagetracker.ui.screens.nav_menus

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddLocationAlt
import androidx.compose.material.icons.outlined.Map
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mileagetracker.R
import com.example.mileagetracker.data.Summary
import com.example.mileagetracker.network.repositoy.JourneyRepository
import com.example.mileagetracker.network.repositoy.PointsRepository
import com.example.mileagetracker.ui.screens.main.MainViewModel
import com.example.mileagetracker.ui.screens.nav_menus.new_journey.NewJourneyScreen
import com.example.mileagetracker.ui.screens.nav_menus.past.PastJourneysScreen
import com.example.mileagetracker.utils.annotations.VerticalScreenPreview
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenusScreen(
    onStart: (journeyTitle: String) -> Unit,
    goToSummaryScreen: (summary: Summary) -> Unit,
    mainViewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val menuItems = listOf(NavMenusScreen.NewJourney, NavMenusScreen.PastJourneys)
    var selectedMenu by rememberSaveable { mutableStateOf(NavMenusScreen.NewJourney) }
    val summaryList by mainViewModel.summaryList.collectAsState()

    ModalNavigationDrawer(drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.fillMaxHeight(),
                drawerShape = RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = stringResource(R.string.app_name),
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier
                            .padding(vertical = 24.dp, horizontal = 16.dp)
                    )

                    HorizontalDivider()

                    Spacer(modifier = Modifier.height(16.dp))

                    menuItems.forEach { item ->
                        NavigationDrawerItem(
                            label = {
                                Text(
                                    text = item.label,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            },
                            selected = item == selectedMenu,
                            onClick = {
                                selectedMenu = item
                                scope.launch { drawerState.close() }
                            },
                            icon = {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.label,
                                    tint = if (item == selectedMenu)
                                        MaterialTheme.colorScheme.primary
                                    else
                                        MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            },
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }) {
        Scaffold(topBar = {
            TopAppBar(
                title = { Text(selectedMenu.label) },
                navigationIcon = {
                    IconButton(onClick = { scope.launch { drawerState.open() } }) {
                        Icon(selectedMenu.icon, contentDescription = "Menu")
                    }
                })
        }) { padding ->
            Box(modifier = modifier.padding(padding)) {
                when (selectedMenu) {
                    NavMenusScreen.NewJourney -> {
                        NewJourneyScreen(onStart = { onStart(it) }, modifier = modifier)
                    }

                    NavMenusScreen.PastJourneys -> {
                        PastJourneysScreen(
                            modifier = modifier,
                            summaryList = summaryList,
                            onItemClick = { goToSummaryScreen(it) }
                        )
                    }
                }
            }
        }
    }
}

enum class NavMenusScreen(val label: String, val icon: ImageVector) {
    NewJourney(
        label = "New Journey",
        icon = Icons.Outlined.AddLocationAlt
    ),
    PastJourneys(label = "Past Journeys", icon = Icons.Outlined.Map),
}

//@VerticalScreenPreview
//@Composable
//fun MenuScreenVertical(){
//    MenusScreen(
//        onStart = {},
//        goToSummaryScreen = {},
//        mainViewModel = MainViewModel(
//            journeyRepository = JourneyRepository.mock,
//            pointsRepository = PointsRepository.mock
//        )
//    )
//}