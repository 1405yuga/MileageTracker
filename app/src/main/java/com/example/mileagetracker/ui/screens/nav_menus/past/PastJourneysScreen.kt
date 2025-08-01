package com.example.mileagetracker.ui.screens.nav_menus.past

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.Route
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mileagetracker.data.Summary
import com.example.mileagetracker.utils.annotations.VerticalScreenPreview
import com.example.mileagetracker.utils.extension_functions.toFormattedDistanceInKM
import com.example.mileagetracker.utils.extension_functions.toFormattedTimeString

@Composable
fun PastJourneysScreen(
    summaryList: List<Summary>,
    onItemClick: (summaryId: Long?) -> Unit,
    onDeleteClick: (summaryId: Long?) -> Unit,
    modifier: Modifier = Modifier
) {
    if (summaryList.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("No previous journey!", style = MaterialTheme.typography.titleMedium)
        }
    } else {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(summaryList) { summary ->
                PastJourneyItem(
                    summary = summary,
                    onItemClick = { onItemClick(it) },
                    onDeleteClick = { onDeleteClick(it) })
            }
        }
    }
}

@Composable
fun PastJourneyItem(
    summary: Summary,
    onItemClick: (summaryId: Long?) -> Unit,
    onDeleteClick: (Long?) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        onClick = { onItemClick(summary.id) }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.Place, contentDescription = "Title")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(summary.title, style = MaterialTheme.typography.titleMedium)
                }

                IconButton(
                    onClick = { onDeleteClick(summary.id) },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.Route, contentDescription = "Distance")
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = summary.distanceInMeters.toFormattedDistanceInKM(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.AccessTime, contentDescription = "Time")
                Spacer(modifier = Modifier.width(8.dp))
                val start = summary.startTime.toFormattedTimeString()
                val end = summary.endTime?.toFormattedTimeString()
                Text("$start → $end", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
@VerticalScreenPreview
fun PastJourneysVerticalPreview() {
    PastJourneysScreen(
        summaryList = List(5) { Summary.mock }, onItemClick = {}, onDeleteClick = {})
}