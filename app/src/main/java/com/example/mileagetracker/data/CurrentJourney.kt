package com.example.mileagetracker.data

data class CurrentJourney(
    val isActive: Boolean,
    val id: Long,
    val title: String,
    val startTime: Long
)
