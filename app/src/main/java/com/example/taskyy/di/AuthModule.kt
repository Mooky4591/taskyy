package com.example.taskyy.di

import android.content.Context
import androidx.navigation.NavController
import androidx.room.Room
import com.example.taskyy.R
import com.example.taskyy.data.local.data_access_objects.UserDao
import com.example.taskyy.data.local.room_database.TaskyyDatabase
import com.example.taskyy.data.remote.ApiKeyInterceptor
import com.example.taskyy.data.remote.TaskyyApi
import com.example.taskyy.data.repositories.AuthRepositoryImpl
import com.example.taskyy.domain.usecases.LoginUseCase
import com.example.taskyy.domain.repository.AuthRepository
import com.example.taskyy.domain.usecases.RegisterUseCase
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
object AuthModule {

    @Provides
    @Singleton
    fun provideAuthRepository(userDao: UserDao, api: TaskyyApi): AuthRepository {
        return AuthRepositoryImpl(userDao, api)
    }

    @Provides
    @Singleton
    fun provideNavController(@ApplicationContext context: Context): NavController {
        return NavController(context = context)
    }

    @Provides
    @Singleton
    fun provideLoginUseCase(@ApplicationContext context: Context): LoginUseCase {
        return LoginUseCase(provideAuthRepository(provideUserDao(context), provideRetrofitInstance(context)))
    }

    @Provides
    @Singleton
    fun provideRegisterUseCase(@ApplicationContext context: Context): RegisterUseCase {
        return RegisterUseCase(provideAuthRepository(provideUserDao(context), provideRetrofitInstance(context)))
    }

    @Provides
    @Singleton
    fun provideUserDao(@ApplicationContext context: Context): UserDao {
        return provideRoomDatabase(context).userDao()
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
                .addConverterFactory(MoshiConverterFactory.create(Moshi.Builder().add(KotlinJsonAdapterFactory()).build()))
                .client(provideOkHttpClient(context))
                .build()
                .create(TaskyyApi::class.java)
    }

    @Provides
    @Singleton
    fun provideRoomDatabase(@ApplicationContext context: Context): TaskyyDatabase {
        synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                TaskyyDatabase::class.java,
                "database"
            ).build()
            return instance
        }
    }
}
