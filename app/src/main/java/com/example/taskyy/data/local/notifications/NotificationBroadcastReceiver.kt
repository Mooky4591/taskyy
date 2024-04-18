package com.example.taskyy.data.local.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class NotificationBroadcastReceiver() :
    BroadcastReceiver() {
    override fun onReceive(
        context: Context,
        intent: Intent,
    ) {
        // Retrieve details from Intent and show notification
        val notificationId = intent.getIntExtra("NOTIFICATION_ID", 0)
        val agendaType = intent.getStringExtra("AGENDA_TYPE") ?: ""
        val agendaId = intent.getStringExtra("AGENDA_ID") ?: ""
        val title = intent.getStringExtra("TITLE") ?: "Reminder"
        val description = intent.getStringExtra("DESCRIPTION") ?: "You have a reminder"

        TaskyNotificationService().showNotification(
            context = context,
            notificationId = notificationId,
            title = title,
            description = description,
            agendaId = agendaId,
            agendaType = agendaType,
        )
    }
}