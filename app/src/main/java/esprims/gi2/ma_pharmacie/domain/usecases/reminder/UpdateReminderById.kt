package esprims.gi2.ma_pharmacie.domain.usecases.reminder

import esprims.gi2.ma_pharmacie.data.repository.ReminderRepository
import esprims.gi2.ma_pharmacie.presentation.reminder.show_reminder.model.Reminder
import esprims.gi2.ma_pharmacie.presentation.shared.Result
import javax.inject.Inject

class UpdateReminderById @Inject constructor (
    val reminderRepository: ReminderRepository
        )
{

    suspend operator fun invoke(id:Int,reminder:Reminder): Result<Reminder> {

        return reminderRepository.updateReminderById(id,reminder)
    }

}