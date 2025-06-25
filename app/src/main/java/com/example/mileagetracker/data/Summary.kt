package com.example.mileagetracker.data

import com.google.android.gms.maps.model.LatLng

data class Summary(
    val title: String,
    val points: List<LatLng>,
    val distanceInMeters: Float,
    val startTime: Long,
    val endTime: Long
) {
    companion object {
        val mock = Summary(
            title = "Untitled title",
            points = listOf(
                LatLng(19.0760, 72.8777),
                LatLng(19.0765, 72.8782),
                LatLng(19.0770, 72.8790),
                LatLng(19.0775, 72.8798),
                LatLng(19.0780, 72.8805),
                LatLng(19.0785, 72.8810),
                LatLng(19.0790, 72.8818),
                LatLng(19.0795, 72.8825),
                LatLng(19.0800, 72.8830)
            ),
            distanceInMeters = 10.2f,
            startTime = 0L,
            endTime = 100L
        )
    }
}
