package com.example.todoapp.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class NotificationReceiver : BroadcastReceiver() // BroadcastReceiver - nasłuchiwacz zdarzeń systemowych, który reaguje na określone intencje (np. powiadomienia)
{
    override fun onReceive(context: Context, intent: Intent?) { // Intent - obiekt reprezentujący zdarzenie, które zostało odebrane, czyli np. powiadomienie o zadaniu do wykonania
        val title = intent?.getStringExtra("title") ?: "default title"
        val description = intent?.getStringExtra("description") ?: "default description"

        val helper = NotificationHelper(context)
        helper.showTaskNotification(
            title = title,
            description = description
        )
    }


}