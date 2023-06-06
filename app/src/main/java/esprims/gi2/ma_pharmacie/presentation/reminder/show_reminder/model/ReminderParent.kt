package esprims.gi2.ma_pharmacie.presentation.reminder.show_reminder.model

data class ReminderParent(val reminder: Reminder) :Reminder(
    medicationName = reminder.medicationName,
    dose = reminder.dose,
    reminderTime = reminder.reminderTime,
    personName = reminder.personName,
    userEmail = "amiinemakhlouf@gmail.com"
){
    public val parentTime=reminderTime
}
