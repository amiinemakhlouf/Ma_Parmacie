package esprims.gi2.ma_pharmacie.domain.usecases.reminder

import esprims.gi2.ma_pharmacie.data.repository.ReminderRepository
import esprims.gi2.ma_pharmacie.presentation.reminder.show_reminder.model.Reminder
import  esprims.gi2.ma_pharmacie.presentation.shared.Result as Result

class GetAllRemindersUseCase(
   private val reminderRepository: ReminderRepository
)
{
    suspend fun invoke(jwt:String):Result<List<Reminder>>{


        return  reminderRepository.getAllReminders(jwt)



    }
}