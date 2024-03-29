package esprims.gi2.ma_pharmacie.domain.usecases.reminder

import esprims.gi2.ma_pharmacie.data.remote.reminderService.ReminderService
import esprims.gi2.ma_pharmacie.data.repository.ReminderRepository
import esprims.gi2.ma_pharmacie.presentation.reminder.show_reminder.model.Reminder
import esprims.gi2.ma_pharmacie.presentation.shared.Result
import javax.inject.Inject

class AddReminderUseCase @Inject constructor (
    val reminderRepo: ReminderRepository
        )
{

    public suspend operator fun invoke(reminder: Reminder): Result<Reminder> {

        return reminderRepo.saveReminder(reminder)

    }

}