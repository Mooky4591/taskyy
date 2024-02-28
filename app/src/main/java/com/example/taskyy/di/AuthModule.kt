package com.example.taskyy.di

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.taskyy.data.data_access_objects.UserDao
import com.example.taskyy.data.repository.AuthRepositoryImpl
import com.example.taskyy.data.room_database.TaskyyDatabase
import com.example.taskyy.domain.LoginUseCase
import com.example.taskyy.domain.repository.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
@Module

@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun provideAuthRepository() : AuthRepository {
        return AuthRepositoryImpl()
    }

    @Provides
    @Singleton
    fun provideLoginUseCase(): LoginUseCase {
        return LoginUseCase()
    }

    @Composable
    @Provides
    @Singleton
    fun provideUserDao(): UserDao {
        //this doesn't work since it needs the composable annotation. How to get the context here?
        return TaskyyDatabase.getDatabase(LocalContext.current).userDao()
    }
}
