package com.example.taskyy.domain.repository

interface AgendaRepository {
    suspend fun logout(): Boolean
}