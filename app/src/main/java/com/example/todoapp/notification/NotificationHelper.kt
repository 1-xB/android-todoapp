package com.example.todoapp.notification

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.todoapp.R
import com.example.todoapp.ui.MainActivity


class NotificationHelper(private val context: Context) {

    fun showTaskNotification(title: String, description: String? = null) {
        val channelId = "task_notification_channel"
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)  // Kanały powiadomieć są wymagane od Androida 8
        {
            val channel = NotificationChannel(channelId, "Scheduled Notifications", NotificationManager.IMPORTANCE_HIGH) // Utworzenie kanału powiadomień
            notificationManager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(context, channelId)
            .setContentTitle(title)
            .setContentText(description ?: "No description provided")
            .setSmallIcon(R.drawable.ic_notification)
            .setAutoCancel(true)

        notificationManager.notify(1001, builder.build())

    }
}