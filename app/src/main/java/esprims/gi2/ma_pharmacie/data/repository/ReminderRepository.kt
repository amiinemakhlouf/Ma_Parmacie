package esprims.gi2.ma_pharmacie.data.repository

import android.util.Log
import esprims.gi2.ma_pharmacie.data.remote.reminderService.ReminderService
import esprims.gi2.ma_pharmacie.presentation.reminder.show_reminder.model.Reminder
import esprims.gi2.ma_pharmacie.presentation.shared.Constants
import javax.inject.Inject
import esprims.gi2.ma_pharmacie.presentation.shared.Result as Result

class ReminderRepository @Inject constructor(
    private val reminderService: ReminderService
) {

    suspend fun saveReminder(reminder: Reminder):Result<Reminder>{

        Log.d("my jwt", Constants.JWT)
        val result= reminderService.saveReminder(reminder,Constants.JWT)
        return when(result.isSuccessful){

            true -> {
                Result.Success(data = result.body())
            }
            else ->Result.Error()
        }
    }

    suspend fun getAllReminders(jwt:String):Result<List<Reminder>>{

        val result=reminderService.getAllReminders(jwt,Constants.userEmail)
        Log.d("ReminderRepository","mel 08     "+ Constants.userEmail)


        return when(result.isSuccessful){

            true -> Result.Success(result.body())

            else-> {
                Log.d("ReminderRepository","  "+result.errorBody().toString())

                Result.Error(result.errorBody().toString())


            }
        }
    }

}