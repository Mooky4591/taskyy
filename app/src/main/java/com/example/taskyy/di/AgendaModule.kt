package com.example.taskyy.di

import android.content.Context
import com.example.taskyy.R
import com.example.taskyy.data.local.data_access_objects.UserDao
import com.example.taskyy.data.local.room_database.TaskyyDatabase
import com.example.taskyy.data.remote.ApiKeyInterceptor
import com.example.taskyy.data.remote.TaskyyApi
import com.example.taskyy.data.repositories.AgendaRepositoryImpl
import com.example.taskyy.domain.repository.AgendaRepository
import com.example.taskyy.domain.usecases.LogoutUseCase
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
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
    fun provideUserDao(db: TaskyyDatabase): UserDao {
        return db.userDao()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(@ApplicationContext context: Context): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(ApiKeyInterceptor(context.getString(R.string.apiKey)))
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofitInstance(httpClient: OkHttpClient): TaskyyApi {
        return Retrofit.Builder()
            .baseUrl("https://tasky.pl-coding.com/")
            .addConverterFactory(
                MoshiConverterFactory.create(
                    Moshi.Builder().add(
                        KotlinJsonAdapterFactory()
                    ).build()
                )
            )
            .client(httpClient)
            .build()
            .create(TaskyyApi::class.java)
    }

    @Provides
    @Singleton
    fun provideLogoutUseCase(agendaRepository: AgendaRepository): LogoutUseCase {
        return LogoutUseCase(agendaRepository)
    }

}