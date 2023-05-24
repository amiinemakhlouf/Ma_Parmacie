package esprims.gi2.ma_pharmacie.data.entity

data class Pharmacy(
    val name:String,
    val isOpen:Boolean,
    val closeTime:String,
    val phoneNumber:String,
    val  usersReviews:Float,
    val distanceFromUser:String

)
