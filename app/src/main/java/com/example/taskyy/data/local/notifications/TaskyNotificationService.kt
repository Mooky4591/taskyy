package com.example.taskyy.data.local.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.taskyy.R
import com.example.taskyy.ui.enums.AgendaItemType

class TaskyNotificationService {
    @RequiresApi(Build.VERSION_CODES.O)
    fun showNotification(
        context: Context,
        notificationId: Int,
        agendaId: String,
        title: String,
        description: String,
        agendaType: String,
    ) {
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val name = "My Notification Channel"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(TASKY_CHANNEL_ID, name, importance).apply {
        }

        notificationManager.createNotificationChannel(channel)
        val uri =
            Uri.parse(
                "${baseUriGenerator(agendaType)}?zoneDateTime=${System.currentTimeMillis()}&isEditingMode=${false}&agendaItemId=$agendaId",
            )
        val intent =
            Intent(Intent.ACTION_VIEW, uri).apply {
                setPackage(context.packageName)
            }

        val pendingIntent =
            PendingIntent.getActivity(
                context,
                1,
                intent,
                PendingIntent.FLAG_IMMUTABLE,
            )

        val notification =
            NotificationCompat.Builder(context, TASKY_CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(description)
                .setSmallIcon(R.drawable.taskyy_foreground)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .build()

        val hasNotificationPermission =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ContextCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.POST_NOTIFICATIONS,
                ) == PackageManager.PERMISSION_GRANTED
            } else {
                true
            }
        if (hasNotificationPermission) {
            notificationManager.notify(notificationId, notification)
        }
    }

    private fun baseUriGenerator(agendaType: String): String {
        return "https://www.myapp.com/${
            when (agendaType) {
                AgendaItemType.TASK_ITEM.name -> {
                    "taskScreen"
                }

                AgendaItemType.EVENT_ITEM.name -> {
                    "eventScreen"
                }

                else -> {
                    "reminderScreen"
                }
            }
        }"
    }

    companion object {
        const val TASKY_CHANNEL_ID = "tasky_channel"
    }
}