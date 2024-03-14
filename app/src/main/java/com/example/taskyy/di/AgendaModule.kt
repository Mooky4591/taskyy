package com.example.taskyy.di

import android.content.Context
import com.example.taskyy.R
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
    fun providesAgendaRepo(@ApplicationContext context: Context): AgendaRepository {
        return AgendaRepositoryImpl(provideRetrofitInstance(context))
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
    fun provideRetrofitInstance(@ApplicationContext context: Context): TaskyyApi {
        return Retrofit.Builder()
            .baseUrl("https://tasky.pl-coding.com/")
            .addConverterFactory(
                MoshiConverterFactory.create(
                    Moshi.Builder().add(
                        KotlinJsonAdapterFactory()
                    ).build()))
            .client(provideOkHttpClient(context))
            .build()
            .create(TaskyyApi::class.java)
    }

    @Provides
    @Singleton
    fun provideLogoutUseCase(@ApplicationContext context: Context): LogoutUseCase {
        return LogoutUseCase(providesAgendaRepo(context = context))
    }

}