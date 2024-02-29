package com.example.taskyy.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

val api: TaskyyApi by lazy {
    Retrofit.Builder()
        .baseUrl("https://tasky.pl-coding.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(TaskyyApi::class.java)
    }
}