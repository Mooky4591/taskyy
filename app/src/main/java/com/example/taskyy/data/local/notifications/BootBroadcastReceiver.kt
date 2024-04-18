package com.example.taskyy.data.local.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.example.taskyy.domain.error.Result
import com.example.taskyy.domain.repository.AgendaRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BootBroadcastReceiver(
    private val notificationScheduler: NotificationScheduler,
    private val agendaRepository: AgendaRepository,
) :
    BroadcastReceiver() {
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onReceive(
        context: Context,
        intent: Intent,
    ) {
        if (Intent.ACTION_BOOT_COMPLETED == intent.action) {
            // Call method to re-schedule your notifications
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
                val coroutineScope =
                    CoroutineScope(Dispatchers.IO) // Using IO dispatcher for database operations

                coroutineScope.launch {
                    when (val agendaItems =
                        agendaRepository.getAllAgendaItemsWithFutureNotifications()) {
                        is Result.Success -> {
                            agendaItems.data.forEach { agendaItem ->
                                notificationScheduler.scheduleNotification(agendaItem)
                            }
                        }

                        is Result.Error -> {}
                    }
                }
            }
        }
    }
}