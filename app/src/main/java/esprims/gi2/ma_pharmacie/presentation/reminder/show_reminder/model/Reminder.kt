package esprims.gi2.ma_pharmacie.presentation.reminder.show_reminder.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
open class Reminder(
    var id:Int?=null,
    var medicationName: String?=null,
    var dose: String?=null,
    var reminderTime: String?=null,
    var personName: String?=null,
    var startDate: String?=null,
    var endDate: String?=null,
    var moment: Int?=null,
    var days: String?=null,
    var type: String?=null,
    var userEmail: String? = "",
    var image: String? = null,
    var isDelegated:Boolean=false,
    var source:String?=null,
    var statu:Int?=0


):Parcelable
