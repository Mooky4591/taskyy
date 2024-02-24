package com.example.taskyy.di

import com.example.taskyy.data.repository.RegisterRepositoryImpl
import com.example.taskyy.domain.repository.RegisterRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RegisterModule {

    @Provides
    @Singleton
    fun provideRegisterRepository(): RegisterRepository {
        return RegisterRepositoryImpl()
    }
}