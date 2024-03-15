package com.example.taskyy.domain.usecases

import com.example.taskyy.domain.repository.AgendaRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val agendaRepository: AgendaRepository
) {

    suspend fun logout(): Boolean {
        return agendaRepository.logout()
    }

}