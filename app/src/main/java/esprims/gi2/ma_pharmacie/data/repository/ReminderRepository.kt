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

            true ->{
                for(res in result.body()!!){

                    Log.d("ResminderRepository",res.userEmail!!)
                    Log.d("ResminderRepository",res.reminderTime!!)
                    Log.d("ResminderRepository",res.isDelegated!!.toString())
                }
                Result.Success(result.body())

            }

            else-> {
                Log.d("ReminderRepository","  "+result.errorBody().toString())

                Result.Error(result.errorBody().toString())


            }
        }
    }

    suspend fun  getReminderById(id:Int):Result<Reminder>{

        val res=reminderService.getReminderById(id)
        if(res.isSuccessful)
        {
            return  Result.Success(res.body())
        }

        return Result.Error(res.errorBody().toString()!!)

    }

     suspend  fun updateReminderById(id: Int,reminder: Reminder):Result<Reminder> {

       val res= reminderService.updateReminderById(id=id,reminder)

        if(res.isSuccessful)
        {
            return  esprims.gi2.ma_pharmacie.presentation.shared.Result.Success(reminder)
        }

        return  esprims.gi2.ma_pharmacie.presentation.shared.Result.Error()

    }

}