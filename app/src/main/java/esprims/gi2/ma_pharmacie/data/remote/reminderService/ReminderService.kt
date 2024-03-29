package esprims.gi2.ma_pharmacie.data.remote.reminderService

import esprims.gi2.ma_pharmacie.data.entity.User
import esprims.gi2.ma_pharmacie.presentation.reminder.show_reminder.model.Reminder
import retrofit2.Response
import retrofit2.http.*

interface ReminderService {



    @POST("api/reminder/save")
    suspend fun saveReminder(@Body reminder: Reminder,@Header("AUTHORIZATION") value: String,

    ): Response<Reminder>

    @GET("api/reminders")
    suspend fun getAllReminders(@Header("AUTHORIZATION") value: String?=null,@Query("userEmail") userEmail: String):Response<List<Reminder>>

    @GET("/reminders/{id}")
     suspend fun getReminderById(@Path("id") id: Int): Response<Reminder>
    @PUT("/reminders/{id}")
    suspend fun updateReminderById(@Path("id") id: Int, @Body reminder: Reminder): Response<Reminder>


}