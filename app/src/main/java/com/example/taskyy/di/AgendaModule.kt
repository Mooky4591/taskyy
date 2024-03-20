package com.example.taskyy.di

import com.example.taskyy.data.local.data_access_objects.UserDao
import com.example.taskyy.data.remote.TaskyyApi
import com.example.taskyy.data.repositories.AgendaRepositoryImpl
import com.example.taskyy.domain.repository.AgendaRepository
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
    fun providesAgendaRepo(api: TaskyyApi, userDao: UserDao): AgendaRepository {
        return AgendaRepositoryImpl(api, userDao)
    }

    @Provides
    @Singleton
    fun provideLogoutUseCase(agendaRepository: AgendaRepository): LogoutUseCase {
        return LogoutUseCase(agendaRepository)
    }

}