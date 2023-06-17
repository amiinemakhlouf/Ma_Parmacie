package esprims.gi2.ma_pharmacie.presentation.reminder.show_reminder.model

open class Reminder(
    val medicationName: String,
    val dose: String,
    val reminderTime: String,
    val personName: String,
    val startDate: String,
    val endDate: String,
    val moment: Int,
    var days: String,
    val type: String,
    val userEmail: String = "",
    val image: String? = null

)
