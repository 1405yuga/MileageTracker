package com.example.mileagetracker.utils.extension_functions

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Long.toFormattedTimeString(): String{
    if (this == 0L) return "On Going"
    val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm:ss", Locale.getDefault())
    return sdf.format(Date(this))
}

fun Float.toFormattedDistanceInKM(): String{
    return String.format("%.2f km", this / 1000)
}