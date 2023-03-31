package esprims.gi2.ma_pharmacie.presentation.reminder.show_reminder.model

data class Reminder(
    val medicationName:String,
    val dose:Int,
    val reminderTime:String,
    val personName:String
)
