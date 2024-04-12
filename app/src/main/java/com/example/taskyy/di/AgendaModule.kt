package com.example.taskyy.di

import com.example.taskyy.data.local.data_access_objects.AgendaActivityDao
import com.example.taskyy.data.local.data_access_objects.PendingReminderRetryDao
import com.example.taskyy.data.local.data_access_objects.UserDao
import com.example.taskyy.data.local.room_database.TaskyyDatabase
import com.example.taskyy.data.remote.TaskyyApi
import com.example.taskyy.data.repositories.AgendaRepositoryImpl
import com.example.taskyy.domain.repository.AgendaRepository
import com.example.taskyy.domain.repository.UserPreferences
import com.example.taskyy.domain.usecases.CheckForRemindersUseCase
import com.example.taskyy.domain.usecases.LogoutUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AgendaModule {

    @Provides
    @Singleton
    fun providesAgendaRepo(
        api: TaskyyApi,
        userDao: UserDao,
        agendaDao: AgendaActivityDao,
        userPreferences: UserPreferences,
        pendingReminderRetryDao: PendingReminderRetryDao
    ): AgendaRepository {
        return AgendaRepositoryImpl(
            retrofit = api,
            userDao = userDao,
            agendaDao = agendaDao,
            userPreferences = userPreferences,
            pendingReminderRetryDao = pendingReminderRetryDao
        )
    }

    @Provides
    @Singleton
    fun provideLogoutUseCase(agendaRepository: AgendaRepository): LogoutUseCase {
        return LogoutUseCase(agendaRepository)
    }

    @Provides
    @Singleton
    fun provideAgendaActivityDao(db: TaskyyDatabase): AgendaActivityDao {
        return db.agendaDao()
    }

    @Provides
    @Singleton
    fun providePendingReminderRetryDao(db: TaskyyDatabase): PendingReminderRetryDao {
        return db.pendingReminderRetryDao()
    }

    @Provides
    @Singleton
    fun provideCheckForRemindersUseCase(agendaRepository: AgendaRepository): CheckForRemindersUseCase {
        return CheckForRemindersUseCase(agendaRepository)
    }
}