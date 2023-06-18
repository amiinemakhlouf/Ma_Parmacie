package esprims.gi2.ma_pharmacie.presentation.reminder.show_reminder.model

open class Reminder(
    var medicationName: String?=null,
    var dose: String?=null,
    var reminderTime: String?=null,
    var personName: String?=null,
    var startDate: String?=null,
    var endDate: String?=null,
    var moment: Int?=null,
    var days: String?=null,
    var type: String?=null,
    val userEmail: String? = "",
    var image: String? = null

)
