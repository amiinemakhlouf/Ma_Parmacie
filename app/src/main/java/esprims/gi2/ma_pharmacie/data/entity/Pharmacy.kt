package esprims.gi2.ma_pharmacie.data.entity

data class Pharmacy(
    val name: String,
    val streetName: String,
    val workingHourStart: String?=null,
    val workingHourEnd: String?=null,
    val phoneNumber: String,
    val rate: Float,
    val longitude: Double,
    val latitude: Double,
    var isOpen:Boolean
)
