package com.example.mileagetracker.utils.helper

import android.location.Location
import com.google.android.gms.maps.model.LatLng

object HelperFunctions {
    fun calculateDistance(pointsList: List<LatLng>): Float {
        if (pointsList.size < 2) return 0f
        var total = 0f
        for (i in 0 until pointsList.size - 1) {
            val loc1 = Location("").apply {
                latitude = pointsList[i].latitude
                longitude = pointsList[i].longitude
            }

            val loc2 = Location("").apply {
                latitude = pointsList[i + 1].latitude
                longitude = pointsList[i + 1].longitude
            }

            total += loc1.distanceTo(loc2)

        }
        return total
    }
}