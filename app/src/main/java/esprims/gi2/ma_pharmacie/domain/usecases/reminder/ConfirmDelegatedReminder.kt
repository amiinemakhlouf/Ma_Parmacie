package esprims.gi2.ma_pharmacie.domain.usecases.reminder

import esprims.gi2.ma_pharmacie.data.repository.ReminderRepository
import esprims.gi2.ma_pharmacie.presentation.reminder.show_reminder.model.Reminder
import esprims.gi2.ma_pharmacie.presentation.shared.Result
import javax.inject.Inject

class ConfirmDelegatedReminder @Inject constructor(
    val reminderReposiotry:ReminderRepository
) {

    suspend  operator fun invoke(reminder:Reminder): Result<Reminder> {

        return reminderReposiotry.saveReminder(reminder)

    }
}