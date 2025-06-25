package com.example.mileagetracker.utils.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat

object NotificationHelper {
    private const val CHANNEL_ID = "journey_channel"
    private const val CHANNEL_NAME = "Journey Tracking"

    private fun createChannel(context: Context) {
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_LOW // LOW is ideal for background
        ).apply {
            description = "Used for background journey tracking"
        }

        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }

    fun createNotification(context: Context): Notification {
        createChannel(context = context)
        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("Tracking your journey")
            .setContentText("tracking....")
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }
}