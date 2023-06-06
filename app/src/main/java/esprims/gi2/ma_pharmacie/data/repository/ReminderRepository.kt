package esprims.gi2.ma_pharmacie.data.repository

import esprims.gi2.ma_pharmacie.data.remote.reminderService.ReminderService
import esprims.gi2.ma_pharmacie.presentation.reminder.show_reminder.model.Reminder
import esprims.gi2.ma_pharmacie.presentation.shared.Constants
import javax.inject.Inject
import esprims.gi2.ma_pharmacie.presentation.shared.Result as Result

class ReminderRepository @Inject constructor(
    private val reminderService: ReminderService
) {

    suspend fun saveReminder(reminder: Reminder):Result<Reminder>{

        
        val result= reminderService.saveReminder(reminder,Constants.JWT)
        return when(result.isSuccessful){

            true -> {
                Result.Success(data = result.body())
            }
            else ->Result.Error()
        }
    }

    suspend fun getAllReminders(jwt:String):Result<List<Reminder>>{

        val result=reminderService.getAllReminders(jwt)

        return when(result.isSuccessful){

            true -> Result.Success(result.body())

            else-> {
                Result.Error(result.errorBody().toString())
            }
        }
    }

}