package com.example.taskyy.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.example.taskyy.App
import com.example.taskyy.R
import com.example.taskyy.data.local.data_access_objects.EventDao
import com.example.taskyy.data.local.data_access_objects.ReminderDao
import com.example.taskyy.data.local.data_access_objects.TaskDao
import com.example.taskyy.data.local.data_access_objects.UserDao
import com.example.taskyy.data.local.room_database.TaskyyDatabase
import com.example.taskyy.data.remote.TaskyyApi
import com.example.taskyy.data.remote.interceptors.ApiKeyInterceptor
import com.example.taskyy.data.repositories.AuthRepositoryImpl
import com.example.taskyy.data.repositories.UserPreferencesImpl
import com.example.taskyy.domain.error.PasswordValidator
import com.example.taskyy.domain.repository.AuthRepository
import com.example.taskyy.domain.repository.UserPreferences
import com.example.taskyy.domain.usecases.LoginUseCase
import com.example.taskyy.domain.usecases.RegisterUseCase
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun provideAuthRepository(
        userDao: UserDao,
        eventDao: EventDao,
        reminderDao: ReminderDao,
        taskDao: TaskDao,
        api: TaskyyApi,
        userPreferences: UserPreferences
    ): AuthRepository {
        return AuthRepositoryImpl(userDao, eventDao, taskDao, reminderDao, api, userPreferences)
    }

    @Provides
    @Singleton
    fun provideLoginUseCase(authRepository: AuthRepository): LoginUseCase {
        return LoginUseCase(authRepository)
    }

    @Provides
    @Singleton
    fun provideRegisterUseCase(
        authRepository: AuthRepository,
        passwordValidator: PasswordValidator
    ): RegisterUseCase {
        return RegisterUseCase(authRepository, passwordValidator)
    }

    @Provides
    @Singleton
    fun provideUserDao(db: TaskyyDatabase): UserDao {
        return db.userDao()
    }

    @Provides
    @Singleton
    fun provideUserPreferences(sharedPreferences: SharedPreferences): UserPreferences {
        return UserPreferencesImpl(sharedPreferences)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        @ApplicationContext context: Context,
        userPreferences: UserPreferences
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(ApiKeyInterceptor(context.getString(R.string.apiKey), userPreferences))
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofitInstance(httpClient: OkHttpClient): TaskyyApi {
        return Retrofit.Builder()
            .baseUrl("https://tasky.pl-coding.com/")
            .addConverterFactory(
                MoshiConverterFactory.create(
                    Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                )
            )
            .client(httpClient)
            .build()
            .create(TaskyyApi::class.java)
    }

    @Provides
    @Singleton
    fun provideRoomDatabase(@ApplicationContext context: Context): TaskyyDatabase {
        synchronized(this) {
            return Room.databaseBuilder(
                context.applicationContext,
                TaskyyDatabase::class.java,
                "database"
            )
                .fallbackToDestructiveMigration()
                .build()
        }
    }

    @Provides
    @Singleton
    fun providePasswordValidator(): PasswordValidator {
        return PasswordValidator()
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideApplicationScope(app: Application): CoroutineScope {
        return (app as App).applicationScope
    }
}
