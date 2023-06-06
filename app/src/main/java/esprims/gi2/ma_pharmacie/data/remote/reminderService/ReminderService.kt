package esprims.gi2.ma_pharmacie.data.remote.reminderService

import esprims.gi2.ma_pharmacie.data.entity.User
import esprims.gi2.ma_pharmacie.presentation.reminder.show_reminder.model.Reminder
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ReminderService {



    @POST("api/reminder/save")
    suspend fun saveReminder(@Body reminder: Reminder,@Header("AUTHORIZATION") value: String): Response<Reminder>

    @GET("api/reminders")
    suspend fun getAllReminders(@Header("AUTHORIZATION") value: String):Response<List<Reminder>>

}