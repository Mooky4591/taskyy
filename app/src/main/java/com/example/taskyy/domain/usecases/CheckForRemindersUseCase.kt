package com.example.taskyy.domain.usecases

import com.example.taskyy.domain.error.DataError
import com.example.taskyy.domain.error.Result
import com.example.taskyy.domain.repository.AgendaRepository
import com.example.taskyy.ui.objects.Reminder
import com.example.taskyy.ui.screens.toMillis
import java.time.LocalDateTime
import javax.inject.Inject

class CheckForRemindersUseCase @Inject constructor(
    private val agendaRepository: AgendaRepository
) {

    suspend fun checkForReminders(dateTime: LocalDateTime): Result<List<Reminder>, DataError.Local> {
        val startDateTime = dateTime.withHour(0).withMinute(0).withSecond(0).withNano(0)
        val endDateTime = dateTime.withHour(23).withMinute(59).withSecond(59).withNano(999_999_999)


        return agendaRepository.getReminders(
            startDate = startDateTime.toMillis(),
            endDate = endDateTime.toMillis()
        )
    }
}