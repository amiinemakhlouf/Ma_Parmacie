package esprims.gi2.ma_pharmacie.presentation.reminder.show_reminder

import android.widget.TextView
import esprims.gi2.ma_pharmacie.presentation.reminder.show_reminder.model.Reminder

interface ReminderCallback {

    fun navigateToDetailsScreen(reminder: Reminder)

}
