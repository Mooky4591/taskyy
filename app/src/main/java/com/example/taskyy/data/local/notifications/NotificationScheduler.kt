package com.example.taskyy.data.local.notifications

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.taskyy.ui.objects.AgendaEventItem

class NotificationScheduler(private val context: Context) {
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    @RequiresApi(Build.VERSION_CODES.S)
    fun scheduleNotification(agendaItem: AgendaEventItem) {
        alarmManager.apply {
            canScheduleExactAlarms()
            setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                agendaItem.alarmType,
                createPendingIntent(agendaItem),
            )
        }
    }

    fun cancelAllScheduledNotifications() {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancelAll()
    }

    fun cancelScheduledNotificationAndPendingIntent(agendaItem: AgendaEventItem) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(agendaItem.eventId.hashCode())
        createPendingIntent(agendaItem).cancel()
    }

    private fun createPendingIntent(agendaItem: AgendaEventItem): PendingIntent {
        val notificationID = agendaItem.eventId.hashCode()
        val intent =
            Intent(context, NotificationBroadcastReceiver::class.java)
                .apply {
                    putExtra("AGENDA_TYPE", agendaItem.eventType.name)
                    putExtra("NOTIFICATION_ID", notificationID)
                    putExtra("AGENDA_ID", agendaItem.eventId)
                    putExtra("TITLE", agendaItem.title)
                    putExtra("DESCRIPTION", agendaItem.description)
                }
        return PendingIntent.getBroadcast(
            context,
            notificationID,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
    }
}