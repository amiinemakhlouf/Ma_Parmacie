package esprims.gi2.ma_pharmacie.presentation.reminder.show_reminder.model

data class ReminderParent(val reminder: Reminder) :Reminder(
    medicationName = reminder.medicationName,
    dose = reminder.dose,
    reminderTime = reminder.reminderTime,
    personName = reminder.personName,
    startDate = reminder.startDate,
    endDate = reminder.endDate,
    moment = reminder.moment,
    userEmail = "amiinemakhlouf@gmail.com",
    days=reminder.days,
    type = reminder.type

){
    public val parentTime=reminderTime
}
