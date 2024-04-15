package com.example.taskyy.data.remote

import com.example.taskyy.data.local.room_entity.agenda_entities.EventEntity
import com.example.taskyy.data.local.room_entity.agenda_entities.ReminderEntity
import com.example.taskyy.data.local.room_entity.agenda_entities.TaskEntity
import com.example.taskyy.data.remote.data_transfer_objects.RegisterUserDTO
import com.example.taskyy.data.remote.data_transfer_objects.ReminderDTO
import com.example.taskyy.data.remote.data_transfer_objects.TaskDTO
import com.example.taskyy.data.remote.response_objects.LoginUserResponse
import com.example.taskyy.domain.objects.Login
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface TaskyyApi {
    @POST("/register")
    suspend fun registerUser(@Body body: RegisterUserDTO)

    @POST("/login")
    suspend fun loginUser(@Body body: Login): LoginUserResponse

    @POST("/reminder")
    fun createReminder(@Body body: ReminderDTO): retrofit2.Call<Void>

    @GET("/authenticate")
    fun checkTokenIsValid(): retrofit2.Call<Void>

    @GET("/logout")
    suspend fun logoutUser(): retrofit2.Call<Void>

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
    fun createTask(@Body body: TaskDTO): retrofit2.Call<Void>

    @PUT("/task")
    fun updateTask(@Body body: TaskDTO): retrofit2.Call<Void>

    @GET("/task")
    fun getTask(@Query("key") key: String, @Query("task_id") taskId: String): retrofit2.Response<TaskEntity>

    @DELETE("/task")
    fun deleteTask(@Query("task_id") taskId: String): retrofit2.Call<Void>

    @PUT("/reminder")
    fun updateReminder(@Body body: ReminderDTO): retrofit2.Call<Void>

    @GET("/reminder")
    fun getReminder(@Query("key") key: String, @Query("reminder_id") reminderId: String): retrofit2.Response<ReminderEntity>

    @DELETE("/reminder")
    fun deleteReminder(@Query("reminder_id") reminderId: String): retrofit2.Call<Void>
}