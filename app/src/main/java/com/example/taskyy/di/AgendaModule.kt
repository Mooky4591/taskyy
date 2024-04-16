package com.example.taskyy.di

import android.content.Context

import com.example.taskyy.data.local.data_access_objects.PendingReminderRetryDao
import com.example.taskyy.data.local.data_access_objects.PendingTaskRetryDao
import com.example.taskyy.data.local.data_access_objects.ReminderDao
import com.example.taskyy.data.local.data_access_objects.TaskDao
import com.example.taskyy.data.local.data_access_objects.UserDao
import com.example.taskyy.data.local.room_database.TaskyyDatabase
import com.example.taskyy.data.remote.TaskyyApi
import com.example.taskyy.data.repositories.AgendaRepositoryImpl
import com.example.taskyy.domain.repository.AgendaRepository
import com.example.taskyy.domain.usecases.CheckForRemindersUseCase
import com.example.taskyy.domain.usecases.LogoutUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AgendaModule {

    @Provides
    @Singleton
    fun provideAgendaRepo(
        api: TaskyyApi,
        userDao: UserDao,
        reminderDao: ReminderDao,
        taskDao: TaskDao,
        pendingReminderRetryDao: PendingReminderRetryDao,
        pendingTaskRetryDao: PendingTaskRetryDao,
        userPreferences: UserPreferences,
        pendingReminderRetryDao: PendingReminderRetryDao,

        @ApplicationContext context: Context
    ): AgendaRepository {
        return AgendaRepositoryImpl(
            retrofit = api,
            userDao = userDao,
            reminderDao = reminderDao,
            pendingReminderRetryDao = pendingReminderRetryDao,
            pendingTaskRetryDao = pendingTaskRetryDao,
            taskDao = taskDao,
            userPreferences = userPreferences,
            pendingReminderRetryDao = pendingReminderRetryDao,
            context = context
        )
    }

    @Provides
    @Singleton
    fun provideLogoutUseCase(agendaRepository: AgendaRepository): LogoutUseCase {
        return LogoutUseCase(agendaRepository)
    }

    @Provides
    @Singleton
    fun provideReminderDao(db: TaskyyDatabase): ReminderDao {
        return db.reminderDao()
    }

    @Provides
    @Singleton
    fun provideTaskDao(db: TaskyyDatabase): TaskDao {
        return db.taskDao()
    }

    @Provides
    @Singleton
    fun providePendingReminderRetryDao(db: TaskyyDatabase): PendingReminderRetryDao {
        return db.pendingReminderRetryDao()
    }

    @Provides
    @Singleton
    fun providePendingTaskRetryDao(db: TaskyyDatabase): PendingTaskRetryDao {
        return db.pendingTaskRetryDao()
    }

    @Provides
    @Singleton
    fun provideCheckForRemindersUseCase(agendaRepository: AgendaRepository): CheckForRemindersUseCase {
        return CheckForRemindersUseCase(agendaRepository)
    }
}