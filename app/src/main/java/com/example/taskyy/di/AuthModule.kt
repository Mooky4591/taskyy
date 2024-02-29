package com.example.taskyy.di

import android.content.Context
import com.example.taskyy.data.local.data_access_objects.UserDao
import com.example.taskyy.data.local.room_database.TaskyyDatabase
import com.example.taskyy.data.repositories.AuthRepositoryImpl
import com.example.taskyy.domain.LoginUseCase
import com.example.taskyy.domain.repository.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
@Module

@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun provideAuthRepository(@ApplicationContext context: Context) : AuthRepository {
        return AuthRepositoryImpl()
    }

    @Provides
    @Singleton
    fun provideLoginUseCase(): LoginUseCase {
        return LoginUseCase()
    }

    @Provides
    @Singleton
    fun provideUserDao(@ApplicationContext context: Context): UserDao {
        return TaskyyDatabase.getDatabase(context).userDao()
    }
}
