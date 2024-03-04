package com.example.taskyy.data.remote

import com.example.taskyy.data.local.room_entity.EventEntity
import com.example.taskyy.data.local.room_entity.ReminderEntity
import com.example.taskyy.data.local.room_entity.TaskEntity
import com.example.taskyy.data.local.room_entity.UserEntity
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface TaskyyApi {
    @POST("/register")
    suspend fun registerUser(@Body body: UserEntity)

    @POST("/login")
    fun loginUser(): retrofit2.Response<LoginUserResponse>

  //  @GET("/authenticate")
   // fun checkTokenIsValid(@Query("key") key: String): retrofit2.Response<TokenResponse>

    @GET("/logout")
    fun logoutUser(@Query("key") key: String)

    //@GET("/agenda")
    //fun getAgenda(@Query("key") key: String, @Query("timezone") timeZone: TimeZone, @Query("time") time: Time): retrofit2.Response<AgendaResponse>

    @POST("/syncAgenda")
    fun syncAgenda(@Query("key") key: String)

    //@GET("/fullAgenda")
    //fun getFulLAgenda(@Query("key") key: String): retrofit2.Response<AgendaResponse>

    @Multipart()
    fun createEvent(@Query("key") key: String): retrofit2.Response<EventEntity>

    @GET("event")
    fun getEvent(@Query("key") key: String, @Query("event_id") eventId: String): retrofit2.Response<EventEntity>

    @DELETE("/event")
    fun deleteEvent(@Query("key") key: String, @Query("event_id") eventId: String): retrofit2.Response<EventEntity>

    @Multipart()
    fun updateEvent(@Query("key") key: String): retrofit2.Response<EventEntity>

    //@GET("/attendee")
    //fun getAttendee(@Query("key") key: String, @Query("email") email: String): retrofit2.Response<AttendeeResponse>

    @DELETE("/attendee")
    fun deleteAttendee(@Query("key") key: String, @Query("event_id") eventId: String)

    @POST("/task")
    fun createTask(@Query("key") key: String)

    @PUT("/task")
    fun updateTask(@Query("key") key: String)

    @GET("/task")
    fun getTask(@Query("key") key: String, @Query("task_id") taskId: String): retrofit2.Response<TaskEntity>

    @DELETE("/task")
    fun deleteTask(@Query("key") key: String, @Query("task_id") taskId: String)

    @POST("/reminder")
    fun createReminder(@Query("key") key: String)

    @PUT("/reminder")
    fun updateReminder(@Query("key") key: String)

    @GET("/reminder")
    fun getReminder(@Query("key") key: String, @Query("reminder_id") reminderId: String): retrofit2.Response<ReminderEntity>

    @DELETE("/reminder")
    fun deleteReminder(@Query("key") key: String, @Query("reminder_id") reminderId: String)
}