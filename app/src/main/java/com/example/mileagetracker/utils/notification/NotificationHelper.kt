package com.example.mileagetracker.utils.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.mileagetracker.R
import com.example.mileagetracker.ui.screens.main.MainActivity
import com.example.mileagetracker.ui.screens.main.Screen

object NotificationHelper {
    private const val CHANNEL_ID = "journey_channel"
    private const val CHANNEL_NAME = "Journey Tracking"
    const val PENDING_ACTIVITY_KEY = "navigate_to"
    private var isChannelCreated = false

    private fun createChannel(context: Context) {
        if (isChannelCreated) return
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Used for background journey tracking"
        }

        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
        isChannelCreated = true
    }

    fun createNotification(context: Context): Notification {
        createChannel(context = context)
        val intentToLaunch = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra(PENDING_ACTIVITY_KEY, Screen.Tracker.name)
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intentToLaunch,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("Tracking your journey")
            .setContentText("tracking....")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setOngoing(true)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }
}