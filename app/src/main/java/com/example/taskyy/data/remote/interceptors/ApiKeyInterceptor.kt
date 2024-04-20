package com.example.taskyy.data.remote.interceptors

import com.example.taskyy.domain.repository.UserPreferences
import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterceptor(
    private val apiKey: String,
    private val userPreferences: UserPreferences
): Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(
            chain
                .request()
                .newBuilder()
                .addHeader("x-api-key", apiKey)
                .addHeader("token", userPreferences.getUserToken())
                .build()
        )
    }
}